package com.example.finzen_v2.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.modelos.TipoMovimiento
import com.example.finzen_v2.repositorios.RepositorioMovimientos
import java.text.SimpleDateFormat
import java.util.*

class ViewModelMovimientos(application: Application) : AndroidViewModel(application) {

    private val repositorio = RepositorioMovimientos(application)
    private val _movimientos = MutableLiveData<List<Movimiento>>()
    val movimientos: LiveData<List<Movimiento>> = _movimientos

    init { // en init imporntae
        // Escuchamos los movimientos una sola vez al iniciar el ViewModel ,IMPORTANTE Q ANTES SE ME LIOOO
        repositorio.obtenerMovimientos { lista ->
            _movimientos.value = lista
        }
    }




    fun agregarMovimiento(cantidad: Double, categoria: String, descripcion: String, tipo: TipoMovimiento) {
        val formato = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val fechaHoy = formato.format(Date())

        val nuevoMovimiento = Movimiento(
            id = UUID.randomUUID().toString(),
            cantidad = cantidad,
            categoria = categoria,
            fecha = fechaHoy,
            descripcion = descripcion,
            tipo = tipo,
            id_usuario = "1"
        )
        _movimientos.value = listOf(nuevoMovimiento) + _movimientos.value.orEmpty()
        repositorio.guardarMovimiento(nuevoMovimiento)
    }












    // ELIMINAR , EIDTAR Y RECARGAR


    fun eliminarMovimiento(id: String) {
        _movimientos.value = _movimientos.value.orEmpty().filter { it.id != id }
        repositorio.eliminarMovimiento(id)
    }

    fun editarMovimiento(movimiento: Movimiento) {
        _movimientos.value = _movimientos.value.orEmpty().map {
            if (it.id == movimiento.id) movimiento else it
        }
        repositorio.actualizarMovimiento(movimiento)
    }

    fun recargarMovimientos() {
        repositorio.obtenerMovimientos { lista ->
            _movimientos.value = lista
        }
    }
}
