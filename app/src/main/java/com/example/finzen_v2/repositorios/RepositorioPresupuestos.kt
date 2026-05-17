package com.example.finzen_v2.repositorios

import android.content.Context
import com.example.finzen_v2.modelos.Presupuesto
import com.example.finzen_v2.utilidades.BaseDatosHelper

class RepositorioPresupuestos(context: Context) {


    // INFORMAION : EN ESTA CLASE NO HACERLE MUICHO CASO A O LO Q ESTA COMENTADO PQ ANTES ESTABA EL
    // FIREBASE POR DECIDI HACERLO MEJOR SIN FIREBASE POR QUE NO TENIA SENTIDO HACERLO ASI


    private val baseDatos = BaseDatosHelper(context.applicationContext)

    /*
    Firebase desactivado. La app ahora usa solo SQLite local.

    private val database = FirebaseDatabase
        .getInstance("https://finzen-bab3b-default-rtdb.europe-west1.firebasedatabase.app")
        .reference
    */

    fun guardarPresupuesto(presupuesto: Presupuesto) {
        baseDatos.insertarPresupuesto(presupuesto)

        /*
        database.child("presupuestos").child(presupuesto.id).setValue(presupuesto)
        */
    }

    fun obtenerPresupuestos(callback: (List<Presupuesto>) -> Unit) {
        callback(baseDatos.obtenerPresupuestos())

        /*
        database.child("presupuestos").addListenerForSingleValueEvent(...)
        */
    }

    fun eliminarPresupuesto(id: String) {
        baseDatos.eliminarPresupuesto(id)

        /*
        database.child("presupuestos").child(id).removeValue()
        */
    }
}
