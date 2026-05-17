package com.example.finzen_v2.modelos

// Modelo para Presupuesto
data class Presupuesto(
    val id: String = "",
    val gastado: Double = 0.0,
    val id_usuario: String = "",
    val limite: Double = 0.0
)
