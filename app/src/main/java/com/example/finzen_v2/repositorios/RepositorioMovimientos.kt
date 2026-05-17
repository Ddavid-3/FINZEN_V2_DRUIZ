package com.example.finzen_v2.repositorios

import android.content.Context
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.utilidades.BaseDatosHelper

class RepositorioMovimientos(context: Context) {

    // INFORMAION : EN ESTA CLASE NO HACERLE MUICHO CASO A O LO Q ESTA COMENTADO PQ ANTES ESTABA EL
    // FIREBASE POR DECIDI HACERLO MEJOR SIN FIREBASE POR QUE NO TENIA SENTIDO HACERLO ASI


    private val baseDatos = BaseDatosHelper(context.applicationContext)

    /*
    Firebase desactivado. La app ahora usa solo SQLite local.

    private val database = FirebaseDatabase
        .getInstance("https://finzen-bab3b-default-rtdb.europe-west1.firebasedatabase.app")
        .reference
    */

    fun guardarMovimiento(movimiento: Movimiento) {
        baseDatos.insertarMovimiento(movimiento)

        /*
        database.child("transacciones").child(movimiento.id).setValue(movimiento)
            .addOnSuccessListener { Log.d("Firebase", "Movimiento copiado en Firebase") }
            .addOnFailureListener { Log.e("Firebase", "Error al copiar: ${it.message}") }
        */
    }

    fun obtenerMovimientos(callback: (List<Movimiento>) -> Unit) {
        callback(baseDatos.obtenerMovimientos())

        /*
        database.child("transacciones").addListenerForSingleValueEvent(...)
        */
    }

    fun eliminarMovimiento(id: String) {
        baseDatos.eliminarMovimiento(id)

        /*
        database.child("transacciones").child(id).removeValue()
        */
    }

    fun actualizarMovimiento(movimiento: Movimiento) {
        baseDatos.actualizarMovimiento(movimiento)

        /*
        database.child("transacciones").child(movimiento.id).setValue(movimiento)
        */
    }
}
