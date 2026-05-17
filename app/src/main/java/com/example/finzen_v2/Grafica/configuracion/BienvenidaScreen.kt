package com.example.finzen_v2.Grafica.configuracion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finzen_v2.utilidades.ConfiguracionApp
import com.example.finzen_v2.utilidades.Rutas

@Composable
fun BienvenidaScreen(navController: NavController) {


    // VARIABLEE

    val context = LocalContext.current
    var nombre by remember { mutableStateOf("") }
    val nombreValido = nombre.trim().isNotEmpty()







    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))

        Text(
            text = "Bienvenido a FINZEN",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Escribe tu nombre") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                ConfiguracionApp.nombreUsuario = nombre.trim()
                ConfiguracionApp.guardar(context)
                navController.navigate(Rutas.TABLERO) {
                    popUpTo(Rutas.BIENVENIDA) { inclusive = true }
                }
            },
            enabled = nombreValido,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Entrar")
        }

        Spacer(modifier = Modifier.weight(1f))
    }
}
