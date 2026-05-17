package com.example.finzen_v2.Grafica.transacciones

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finzen_v2.modelos.TipoMovimiento
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos

@Composable
fun EditarTransaccionScreen(navController: NavController, id: String, viewModel: ViewModelMovimientos = viewModel()) {
    // Buscamos el movimiento por su ID
    val movimientos = viewModel.movimientos.observeAsState(emptyList()).value
    val movimiento = movimientos.find { it.id == id }

    // Si no encontramos el movimiento, volvemos atrás
    if (movimiento == null) {
        LaunchedEffect(Unit) { navController.popBackStack() }
        return
    }

    // Estados para los campos (rellenados con los datos actuales)
    var cantidad by remember { mutableStateOf(movimiento.cantidad.toString()) }
    var categoria by remember { mutableStateOf(movimiento.categoria) }
    var descripcion by remember { mutableStateOf(movimiento.descripcion) }
    var tipo by remember { mutableStateOf(movimiento.tipo) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Editar Movimiento", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Selector de tipo (Ingreso/Gasto)
        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = tipo == TipoMovimiento.INGRESO, onClick = { tipo = TipoMovimiento.INGRESO })
            Text("Ingreso")
            Spacer(modifier = Modifier.width(20.dp))
            RadioButton(selected = tipo == TipoMovimiento.GASTO, onClick = { tipo = TipoMovimiento.GASTO })
            Text("Gasto")
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                val nuevaCantidad = cantidad.replace(",", ".").toDoubleOrNull() ?: 0.0
                // Creamos el movimiento actualizado manteniendo el mismo ID y Fecha
                val movActualizado = movimiento.copy(
                    cantidad = nuevaCantidad,
                    categoria = categoria,
                    descripcion = descripcion,
                    tipo = tipo
                )
                viewModel.editarMovimiento(movActualizado)
                navController.popBackStack() // Volver al tablero
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Cambios")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancelar")
        }
    }
}
