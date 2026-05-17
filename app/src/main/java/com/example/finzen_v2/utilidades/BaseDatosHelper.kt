package com.example.finzen_v2.utilidades

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.modelos.Presupuesto
import com.example.finzen_v2.modelos.TipoMovimiento


class BaseDatosHelper(context: Context) : SQLiteOpenHelper(context, "finzen.db", null, 4) {

    override fun onCreate(db: SQLiteDatabase) {
        crearTablas(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        crearTablas(db)
        agregarColumnaSiFalta(db, "movimientos", "id_usuario", "TEXT DEFAULT ''")
    }



    // TABLAS PARA LA INFO DEL DUEÑO DE LA APP Q SE LE GUARDEN LAS COAS EN EL MOVIL
    private fun crearTablas(db: SQLiteDatabase) {
        val crearMovimientos = """
            CREATE TABLE movimientos (
                id TEXT PRIMARY KEY,
                cantidad REAL,
                categoria TEXT,
                fecha TEXT,
                descripcion TEXT,
                tipo TEXT,
                id_usuario TEXT
            )
        """.trimIndent()
        val crearPresupuestos = """
            CREATE TABLE IF NOT EXISTS presupuestos (
                id TEXT PRIMARY KEY,
                gastado REAL,
                id_usuario TEXT,
                limite REAL
            )
        """.trimIndent()
        db.execSQL(crearMovimientos.replace("CREATE TABLE", "CREATE TABLE IF NOT EXISTS"))
        db.execSQL(crearPresupuestos)
    }






    // agregat
    private fun agregarColumnaSiFalta(
        db: SQLiteDatabase,
        tabla: String,
        columna: String,
        definicion: String
    ) {
        try {
            db.execSQL("ALTER TABLE $tabla ADD COLUMN $columna $definicion")
        } catch (_: SQLiteException) {
            // Ya existe.
        }
    }







    fun insertarMovimiento(movimiento: Movimiento) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", movimiento.id)
            put("cantidad", movimiento.cantidad)
            put("categoria", movimiento.categoria)
            put("fecha", movimiento.fecha) // Guardamos el String directamente
            put("descripcion", movimiento.descripcion)
            put("tipo", movimiento.tipo.name)
            put("id_usuario", movimiento.id_usuario)
        }
        db.insertWithOnConflict(
            "movimientos",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }










    fun obtenerMovimientos(): List<Movimiento> {
        val db = readableDatabase
        val cursor = db.query("movimientos", null, null, null, null, null, "fecha DESC")
        val lista = mutableListOf<Movimiento>()
        while (cursor.moveToNext()) {
            val movimiento = Movimiento(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                cantidad = cursor.getDouble(cursor.getColumnIndexOrThrow("cantidad")),
                categoria = cursor.getString(cursor.getColumnIndexOrThrow("categoria")),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow("fecha")), // Leemos el String
                descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion")),
                tipo = TipoMovimiento.valueOf(cursor.getString(cursor.getColumnIndexOrThrow("tipo"))),
                id_usuario = cursor.getString(cursor.getColumnIndexOrThrow("id_usuario"))
            )
            lista.add(movimiento)
        }
        cursor.close()
        db.close()
        return lista
    }





    fun eliminarMovimiento(id: String) {
        val db = writableDatabase
        db.delete("movimientos", "id = ?", arrayOf(id))
        db.close()
    }







    fun actualizarMovimiento(movimiento: Movimiento) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("cantidad", movimiento.cantidad)
            put("categoria", movimiento.categoria)
            put("fecha", movimiento.fecha)
            put("descripcion", movimiento.descripcion)
            put("tipo", movimiento.tipo.name)
            put("id_usuario", movimiento.id_usuario)
        }
        db.update("movimientos", values, "id = ?", arrayOf(movimiento.id))
        db.close()
    }

    fun insertarPresupuesto(presupuesto: Presupuesto) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("id", presupuesto.id)
            put("gastado", presupuesto.gastado)
            put("id_usuario", presupuesto.id_usuario)
            put("limite", presupuesto.limite)
        }
        db.insertWithOnConflict(
            "presupuestos",
            null,
            values,
            SQLiteDatabase.CONFLICT_REPLACE
        )
        db.close()
    }











    fun obtenerPresupuestos(): List<Presupuesto> {
        val db = readableDatabase
        val cursor = db.query("presupuestos", null, null, null, null, null, null)
        val lista = mutableListOf<Presupuesto>()
        while (cursor.moveToNext()) {
            val presupuesto = Presupuesto(
                id = cursor.getString(cursor.getColumnIndexOrThrow("id")),
                gastado = cursor.getDouble(cursor.getColumnIndexOrThrow("gastado")),
                id_usuario = cursor.getString(cursor.getColumnIndexOrThrow("id_usuario")),
                limite = cursor.getDouble(cursor.getColumnIndexOrThrow("limite"))
            )
            lista.add(presupuesto)
        }
        cursor.close()
        db.close()
        return lista
    }







    fun eliminarPresupuesto(id: String) {
        val db = writableDatabase
        db.delete("presupuestos", "id = ?", arrayOf(id))
        db.close()
    }













    fun reiniciarDatosFinancieros() {
        val db = writableDatabase
        db.delete("movimientos", null, null)
        db.delete("presupuestos", null, null)
        db.close()
    }
}
