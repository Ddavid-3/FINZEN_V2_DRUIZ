package com.example.finzen_v2.Grafica.configuracion

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finzen_v2.utilidades.BaseDatosHelper
import com.example.finzen_v2.utilidades.ConfiguracionApp
import com.example.finzen_v2.utilidades.ExportadorPdf
import com.example.finzen_v2.utilidades.Rutas
import com.example.finzen_v2.viewmodels.ViewModelMovimientos
import com.example.finzen_v2.viewmodels.ViewModelPresupuestos

@Composable
fun ConfiguracionScreen(
    navController: NavController,
    movimientosViewModel: ViewModelMovimientos,
    presupuestosViewModel: ViewModelPresupuestos
) {
    // variableed
    val context = LocalContext.current
    var nombreTemporal by remember { mutableStateOf(ConfiguracionApp.nombreUsuario) }
    var mostrarConfirmacionReinicio by remember { mutableStateOf(false) }
    val nombreValido = nombreTemporal.trim().isNotEmpty()
    val movimientos = movimientosViewModel.movimientos.observeAsState(emptyList()).value
    val presupuestos by presupuestosViewModel.presupuestos.collectAsState()










    if (mostrarConfirmacionReinicio) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionReinicio = false },
            title = { Text("Reiniciar datos") },
            text = { Text("Se borraran todos los movimientos y presupuestos guardados en local.") },
            confirmButton = {
                Button(
                    onClick = {
                        BaseDatosHelper(context).reiniciarDatosFinancieros()
                        movimientosViewModel.recargarMovimientos()
                        presupuestosViewModel.cargarPresupuestos()
                        mostrarConfirmacionReinicio = false
                        navController.navigate(Rutas.TABLERO) {
                            popUpTo(Rutas.TABLERO) { inclusive = true }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Reiniciar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarConfirmacionReinicio = false }) {
                    Text("Cancelar")
                }
            }
        )
    }








    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Configuracion", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Tu Nombre", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(
            value = nombreTemporal,
            onValueChange = { nombreTemporal = it },
            label = { Text("Escribe tu nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(text = "Color de Fondo", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (color in ConfiguracionApp.opcionesColores) {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .clip(CircleShape)
                        .background(color)
                        .clickable {
                            ConfiguracionApp.colorFondo = color
                        }
                        .then(
                            if (ConfiguracionApp.colorFondo == color) {
                                Modifier.background(Color.Black.copy(alpha = 0.2f))
                            } else {
                                Modifier
                            }
                        )
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        OutlinedButton(
            onClick = {
                try {
                    val ruta = ExportadorPdf.exportar(context, movimientos, presupuestos)
                    Toast.makeText(
                        context,
                        "PDF guardado en $ruta",
                        Toast.LENGTH_LONG
                    ).show()
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "No se pudo exportar el PDF",
                        Toast.LENGTH_LONG
                    ).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Exportar datos a PDF")
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedButton(
            onClick = { mostrarConfirmacionReinicio = true },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            )
        ) {
            Text("Reiniciar datos de dinero")
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                ConfiguracionApp.nombreUsuario = nombreTemporal.trim()
                ConfiguracionApp.guardar(context)
                navController.navigate(Rutas.TABLERO)
            },
            enabled = nombreValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar y Volver")
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = { navController.popBackStack() }) {
            Text("Cancelar")
        }
    }
}
