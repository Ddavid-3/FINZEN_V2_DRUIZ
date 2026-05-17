package com.example.finzen_v2.utilidades

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb


 // Esta clase es una "Caja Global" para la configuración.
 // Usamos 'object' para que sea accesible desde cualquier pantalla.

object ConfiguracionApp {
    private const val PREFERENCIAS = "configuracion_app"
    private const val CLAVE_NOMBRE = "nombre_usuario"
    private const val CLAVE_COLOR = "color_fondo"

    var nombreUsuario by mutableStateOf("")
    var nombrePedido by mutableStateOf(false)
    var colorFondo by mutableStateOf(Color.White)



    // Opciones de colores simples para la clase
    val opcionesColores = listOf(
        Color.White,
        Color(0xFFE3F2FD), // Azul suave
        Color(0xFFF1F8E9), // Verde suave
        Color(0xFFFFFDE7), // Amarillo suave
        Color(0xFFF3E5F5)  // Morado suave
    )










    // CARGADA Y GUARDADA DE LAS PREFERENCIAS

    fun cargar(context: Context) {
        val prefs = context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE)
        nombrePedido = prefs.contains(CLAVE_NOMBRE)
        nombreUsuario = prefs.getString(CLAVE_NOMBRE, "") ?: ""
        colorFondo = Color(prefs.getInt(CLAVE_COLOR, Color.White.toArgb()))
    }

    fun guardar(context: Context) {
        context.getSharedPreferences(PREFERENCIAS, Context.MODE_PRIVATE)
            .edit()
            .putString(CLAVE_NOMBRE, nombreUsuario)
            .putInt(CLAVE_COLOR, colorFondo.toArgb())
            .apply()
        nombrePedido = true
    }
}
