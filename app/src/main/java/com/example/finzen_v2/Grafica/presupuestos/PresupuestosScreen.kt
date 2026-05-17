package com.example.finzen_v2.Grafica.presupuestos

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.finzen_v2.modelos.Presupuesto
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelPresupuestos
import java.util.UUID

@Composable
fun PresupuestosScreen(
    navController: NavController,
    viewModel: ViewModelPresupuestos = viewModel()
) {
    var limite by remember { mutableStateOf("") }
    val presupuestos by viewModel.presupuestos.collectAsState()


    // Escuchamos los cambios en la lista de presupuestos
    // ------- x p x --------- no hacer caso a esto
    // Actualizamos la lista de presupuestos

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Presupuestos", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = limite,
            onValueChange = { limite = it },
            label = { Text("Limite mensual") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val lim = limite.replace(",", ".").toDoubleOrNull()
                if (lim != null) {
                    val presupuesto = Presupuesto(
                        id = UUID.randomUUID().toString(),
                        limite = lim,
                        gastado = 0.0,
                        id_usuario = "1"
                    )
                    viewModel.agregarPresupuesto(presupuesto)
                    limite = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Agregar Presupuesto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (presupuestos.isEmpty()) {
            Text("No hay presupuestos creados.")
        } else {
            presupuestos.forEach { presupuesto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Limite: $${"%.2f".format(presupuesto.limite)}",
                            modifier = Modifier.weight(1f)
                        )
                        OutlinedButton(
                            onClick = { viewModel.eliminarPresupuesto(presupuesto.id) }
                        ) {
                            Text("Eliminar")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate(Rutas.TABLERO) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al Tablero")
        }
    }
}
