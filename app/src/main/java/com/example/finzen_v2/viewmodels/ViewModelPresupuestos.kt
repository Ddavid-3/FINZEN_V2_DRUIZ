package com.example.finzen_v2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.finzen_v2.modelos.Presupuesto
import com.example.finzen_v2.repositorios.RepositorioPresupuestos
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

// ViewModel para presupuestos
class ViewModelPresupuestos(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioPresupuestos(application)

    private val _presupuestos = MutableStateFlow<List<Presupuesto>>(emptyList())
    val presupuestos: StateFlow<List<Presupuesto>> = _presupuestos

    init {
        cargarPresupuestos()
    }





    // CARGAR ELEMININAR Y EL AGREGAR

    fun cargarPresupuestos() {
        repositorio.obtenerPresupuestos { lista ->
            _presupuestos.value = lista
        }
    }

    fun agregarPresupuesto(presupuesto: Presupuesto) {
        repositorio.guardarPresupuesto(presupuesto)
        _presupuestos.value = _presupuestos.value + presupuesto
    }

    fun eliminarPresupuesto(id: String) {
        repositorio.eliminarPresupuesto(id)
        _presupuestos.value = _presupuestos.value.filter { it.id != id }
    }
}
