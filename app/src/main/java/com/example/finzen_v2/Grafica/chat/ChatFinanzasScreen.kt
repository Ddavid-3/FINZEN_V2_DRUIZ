package com.example.finzen_v2.Grafica.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.finzen_v2.servicios.GeminiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

data class MensajeChat(
    val texto: String,
    val esUsuario: Boolean
)

@Composable
fun ChatFinanzasScreen(navController: NavController) {
    // Lista de mensajes que se ven en pantalla.
    val mensajes = remember {
        mutableStateListOf(
            MensajeChat(
                "Hola, soy la IA de FINZEN. Preguntame sobre ahorro, gastos o presupuestos.",
                false
            )
        )
    }

    // Texto que el usuario esta escribiendo.
    var textoUsuario by remember { mutableStateOf("") }

    // Sirve para desactivar el boton mientras la IA responde.
    var cargando by remember { mutableStateOf(false) }

    // Esto permite lanzar la llamada a Internet sin crear una clase extra.
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Chat financiero",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(mensajes) { mensaje ->
                MensajeItem(mensaje)
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = textoUsuario,
            onValueChange = { textoUsuario = it },
            label = { Text("Escribe tu pregunta") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TextButton(
                onClick = { navController.popBackStack() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Volver")
            }

            Button(
                onClick = {
                    // Guardamos la pregunta sin espacios al principio o final.
                    val pregunta = textoUsuario.trim()
                    if (pregunta.isEmpty()) return@Button

                    // Mostramos el mensaje del usuario en el chat.
                    mensajes.add(MensajeChat(pregunta, true))
                    textoUsuario = ""
                    cargando = true

                    scope.launch {
                        // La llamada a Internet se hace en segundo plano.
                        val respuesta = withContext(Dispatchers.IO) {
                            GeminiService.preguntarFinanzas(pregunta)
                        }

                        // Cuando responde la IA, anadimos su mensaje al chat.
                        mensajes.add(MensajeChat(respuesta, false))
                        cargando = false
                    }
                },
                enabled = !cargando && textoUsuario.trim().isNotEmpty(),
                modifier = Modifier.weight(1f)
            ) {
                Text(if (cargando) "Pensando..." else "Enviar")
            }
        }
    }
}















@Composable
fun MensajeItem(mensaje: MensajeChat) {
    val color = if (mensaje.esUsuario) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.surfaceVariant
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = color)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = if (mensaje.esUsuario) "Tu" else "IA FINZEN",
                style = MaterialTheme.typography.labelMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = mensaje.texto)
        }
    }
}
