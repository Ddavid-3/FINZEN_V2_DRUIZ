package com.example.finzen_v2.Grafica.tablero

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.modelos.TipoMovimiento
import com.example.finzen_v2.utilidades.ConfiguracionApp
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos
import androidx.compose.foundation.layout.statusBarsPadding
@Composable
fun TableroScreen(navController: NavController, viewModel: ViewModelMovimientos = viewModel()) {

    // varaible
    val movimientos = viewModel.movimientos.observeAsState(emptyList()).value






    // saldo
    var saldoTotal = 0.0
    for (m in movimientos) {
        if (m.tipo == TipoMovimiento.INGRESO) {
            saldoTotal = saldoTotal + m.cantidad
        } else {
            saldoTotal = saldoTotal - m.cantidad
        }
    }
















    // Usamos Surface para que el fonndo cambie segun la configuracion
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = ConfiguracionApp.colorFondo
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp), // EN 24 MEJOR NO SE COCHA TANTO CON ARRIBA
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Saludo personalizado con el nombre de la configuracionN
            Text(
                text = "Hola, ${ConfiguracionApp.nombreUsuario}",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            // Tarjeta de Saldo
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "Saldo Total", style = MaterialTheme.typography.titleMedium)
                    Text(
                        text = "$${"%.2f".format(saldoTotal)}", 
                        style = MaterialTheme.typography.headlineLarge
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Últimos movimientos:", 
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Lista de movimientos
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items = movimientos.take(10)) { movimiento -> 
                    MovimientoItem(
                        movimiento = movimiento, 
                        onEliminar = { viewModel.eliminarMovimiento(movimiento.id) }, 
                        onEditar = { navController.navigate("editar/${movimiento.id}") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            
            // Botones de navegacion
            Button(onClick = { navController.navigate(Rutas.TRANSACCIONES) }, modifier = Modifier.fillMaxWidth()) {
                Text("Añadir Movimiento")
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Boton para abrir el chat de finanzas con IA.
            Button(onClick = { navController.navigate(Rutas.CHAT_FINANZAS) }, modifier = Modifier.fillMaxWidth()) {
                Text("Chat IA de finanzas")
            }

            Spacer(modifier = Modifier.height(8.dp))
            
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = { navController.navigate(Rutas.ESTADISTICAS) }, modifier = Modifier.weight(1f)) {
                    Text("Estadísticas")
                }
                Button(onClick = { navController.navigate(Rutas.PRESUPUESTOS) }, modifier = Modifier.weight(1f)) {
                    Text("Presupuestos")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedButton(
                onClick = { navController.navigate(Rutas.CONFIGURACION) }, 
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Configuración")
            }
        }
    }
}

@Composable
fun MovimientoItem(movimiento: Movimiento, onEliminar: () -> Unit, onEditar: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(text = movimiento.descripcion, style = MaterialTheme.typography.bodyLarge)
                Text(text = "${movimiento.categoria} • ${movimiento.fecha}", style = MaterialTheme.typography.bodySmall)
            }
            
            val colorPrecio = if (movimiento.tipo == TipoMovimiento.INGRESO) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            val signo = if (movimiento.tipo == TipoMovimiento.INGRESO) "+" else "-"
            
            Text(
                text = "$signo$${"%.2f".format(movimiento.cantidad)}",
                color = colorPrecio,
                style = MaterialTheme.typography.bodyLarge
            )
            
            IconButton(onClick = onEditar) {
                Text("✎")
            }
            
            IconButton(onClick = onEliminar) {
                Text("✕", color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
