package com.example.finzen_v2.repositorios

import com.example.finzen_v2.modelos.Meta

class RepositorioMetas {

    // INFORMAION : EN ESTA CLASE NO HACERLE MUICHO CASO A O LO Q ESTA COMENTADO PQ ANTES ESTABA EL
    // FIREBASE POR DECIDI HACERLO MEJOR SIN FIREBASE POR QUE NO TENIA SENTIDO HACERLO ASI


    /*
    Firebase desactivado. DDATO : Si quiero usar metas, conviene crear
    sus metodos locales en BaseDatosHelper igual que movimientos y presupuestos.

    private val database = FirebaseDatabase
        .getInstance("https://finzen-bab3b-default-rtdb.europe-west1.firebasedatabase.app/")
        .reference
    */

    fun guardarMeta(meta: Meta) {
        /*
        database.child("metas").child(meta.id).setValue(meta)
        */
    }

    fun obtenerMetas(callback: (List<Meta>) -> Unit) {
        callback(emptyList())

        /*
        database.child("metas").addValueEventListener(...)
        */
    }

    fun eliminarMeta(id: String) {
        /*
        database.child("metas").child(id).removeValue()
        */
    }
}
