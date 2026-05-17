package com.example.finzen_v2.Grafica.transacciones

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finzen_v2.modelos.TipoMovimiento
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos

@Composable
fun TransaccionesScreen(navController: NavController, viewModel: ViewModelMovimientos = viewModel()) {
    var cantidad by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var tipo by remember { mutableStateOf(TipoMovimiento.GASTO) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Nuevo Movimiento", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = cantidad,
            onValueChange = { cantidad = it },
            label = { Text("Cantidad (Ejem: 10.50)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoría (Ejem: Comida)") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = descripcion,
            onValueChange = { descripcion = it },
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            RadioButton(selected = tipo == TipoMovimiento.INGRESO, onClick = { tipo = TipoMovimiento.INGRESO })
            Text("Ingreso")
            Spacer(modifier = Modifier.width(16.dp))
            RadioButton(selected = tipo == TipoMovimiento.GASTO, onClick = { tipo = TipoMovimiento.GASTO })
            Text("Gasto")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val num = cantidad.replace(",", ".").toDoubleOrNull()
                if (num != null && categoria.isNotBlank()) {
                    viewModel.agregarMovimiento(num, categoria, descripcion, tipo)
                    // Después de agregar, volvemos automáticamente al tablero
                    navController.navigate(Rutas.TABLERO) {
                        popUpTo(Rutas.TABLERO) { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Movimiento")
        }

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancelar")
        }
    }
}