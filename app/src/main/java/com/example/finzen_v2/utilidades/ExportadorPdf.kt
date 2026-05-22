package com.example.finzen_v2.utilidades

import android.content.ContentValues
import android.content.Context
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import com.example.finzen_v2.modelos.Movimiento
import com.example.finzen_v2.modelos.Presupuesto
import com.example.finzen_v2.modelos.TipoMovimiento
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object ExportadorPdf {
    fun exportar(
        context: Context,
        movimientos: List<Movimiento>,
        presupuestos: List<Presupuesto>
         ): String {
            val documento = PdfDocument()
            val paint = Paint()
            val tituloPaint = Paint()
            tituloPaint.textSize = 20f
            tituloPaint.isFakeBoldText = true
            paint.textSize = 12f

            var paginaNumero = 1
            var pagina = nuevaPagina(documento, paginaNumero)
            var canvas = pagina.canvas
            var y = 50

        fun nuevaLinea(texto: String) {
            if (y > 780) {
                documento.finishPage(pagina)
                paginaNumero++
                pagina = nuevaPagina(documento, paginaNumero)
                canvas = pagina.canvas
                y = 50
            }
            canvas.drawText(texto.take(85), 40f, y.toFloat(), paint)
            y += 22
        }



        val ingresos = movimientos
            .filter { it.tipo == TipoMovimiento.INGRESO }
            .sumOf { it.cantidad }
        val gastos = movimientos
            .filter { it.tipo == TipoMovimiento.GASTO }
            .sumOf { it.cantidad }
        val saldo = ingresos - gastos

        canvas.drawText("Informe FINZEN", 40f, y.toFloat(), tituloPaint)
        y += 35
        nuevaLinea("Usuario: ${ConfiguracionApp.nombreUsuario}")
        nuevaLinea("Fecha: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date())}")
        nuevaLinea("Ingresos: ${"%.2f".format(ingresos)}")
        nuevaLinea("Gastos: ${"%.2f".format(gastos)}")
        nuevaLinea("Saldo: ${"%.2f".format(saldo)}")
        nuevaLinea("")

        nuevaLinea("MOVIMIENTOS")
        if (movimientos.isEmpty()) {
            nuevaLinea("No hay movimientos guardados.")
        } else {
            for (movimiento in movimientos) {
                val signo = if (movimiento.tipo == TipoMovimiento.INGRESO) "+" else "-"
                nuevaLinea(
                    "${movimiento.fecha} | ${movimiento.tipo} | $signo${"%.2f".format(movimiento.cantidad)} | " +
                        "${movimiento.categoria} | ${movimiento.descripcion}"
                )
            }
        }

        nuevaLinea("")
        nuevaLinea("PRESUPUESTOS")
        if (presupuestos.isEmpty()) {
            nuevaLinea("No hay presupuestos guardados.")
        } else {
            for (presupuesto in presupuestos) {
                nuevaLinea(
                    "Limite: ${"%.2f".format(presupuesto.limite)} | " +
                        "Gastado: ${"%.2f".format(presupuesto.gastado)}"
                )
            }
        }

        documento.finishPage(pagina)

        val nombreArchivo = "finzen_informe_${System.currentTimeMillis()}.pdf"
        guardarEnDescargas(context, documento, nombreArchivo)
        documento.close()
        return "Descargas/FINZEN/$nombreArchivo"
    }

    private fun nuevaPagina(documento: PdfDocument, numero: Int): PdfDocument.Page {
        val info = PdfDocument.PageInfo.Builder(595, 842, numero).create()
        return documento.startPage(info)
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    private fun guardarEnDescargas(
        context: Context,
        documento: PdfDocument,
        nombreArchivo: String
    ) {
        val valores = ContentValues().apply {
            put(MediaStore.Downloads.DISPLAY_NAME, nombreArchivo)
            put(MediaStore.Downloads.MIME_TYPE, "application/pdf")
            put(
                MediaStore.Downloads.RELATIVE_PATH,
                Environment.DIRECTORY_DOWNLOADS + "/FINZEN"
            )
        }

        val uri = context.contentResolver.insert(
            MediaStore.Downloads.EXTERNAL_CONTENT_URI,
            valores
        ) ?: throw IllegalStateException("No se pudo crear el PDF en Descargas")

        context.contentResolver.openOutputStream(uri).use { salida ->
            if (salida == null) {
                throw IllegalStateException("No se pudo abrir el PDF para escribir")
            }
            documento.writeTo(salida)
        }
    }
}
