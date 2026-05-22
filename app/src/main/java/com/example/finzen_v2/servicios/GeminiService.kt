package com.example.finzen_v2.servicios

import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL

object GeminiService {

    ///////////////////////////////////////////////////////////////////////////
    private const val API_KEY = "AIzaSyBqQ53NrPAiVzOYnuVt10GmoSByVf1oaOU"
    private const val MODELO = "gemini-2.5-flash"


    ///////////////////////////////////////////////////////////////////////////////

    fun preguntarFinanzas(pregunta: String): String {
        try {
            // Paso 1: preparamos la URL de Gemini.
            val url = URL(
                "https://generativelanguage.googleapis.com/v1beta/models/$MODELO:generateContent"
            )

            // Paso 2: abrimos la conexion con Internet. /////////////////////////////////////////////////////////////
            val conexion = url.openConnection() as HttpURLConnection
            conexion.requestMethod = "POST"
            conexion.setRequestProperty("Content-Type", "application/json")
            conexion.setRequestProperty("x-goog-api-key", API_KEY)
            conexion.doOutput = true

            // Paso 3: creamos el texto que se le manda a la IA.
            val prompt = """
                Eres el asistente financiero de la app FINZEN.
                Responde en espanol, de forma sencilla y corta.
                Da consejos practicos sobre ahorro, gastos, presupuestos e ingresos.
                No des asesoramiento legal, fiscal o de inversion como si fueras un profesional certificado.

                Pregunta del usuario:
                $pregunta
            """.trimIndent()

            // Paso 4: montamos el JSON que Gemini necesita.
            val body = JSONObject()
                .put(
                    "contents",
                    JSONArray().put(
                        JSONObject()
                            .put("role", "user")
                            .put(
                                "parts",
                                JSONArray().put(JSONObject().put("text", prompt))
                            )
                    )
                )

            // Paso 5: enviamos la pregunta.
            val writer = OutputStreamWriter(conexion.outputStream)
            writer.write(body.toString())
            writer.close()

            // Paso 6: leemos la respuesta de Gemini.
            val codigo = conexion.responseCode
            val stream = if (codigo in 200..299) conexion.inputStream else conexion.errorStream
            val respuesta = BufferedReader(stream.reader()).use { it.readText() }

            if (codigo !in 200..299) {
                return "No pude conectar con la IA. Error $codigo: $respuesta"
            }

            // Paso 7: sacamos solo el texto final de la respuesta.
            return leerTextoRespuesta(respuesta)
        } catch (e: Exception) {
            return "No pude responder ahora mismo. Revisa la conexion a Internet o la clave API."
        }
    }

    private fun leerTextoRespuesta(respuesta: String): String {





        // Convertimos la respuesta en JSON para poder leer sus campos.
        val json = JSONObject(respuesta)




        // Gemini devuelve una lista de respuestas posibles llamadas candidates.
        val candidatos = json.optJSONArray("candidates") ?: return "La IA no devolvio respuesta."
        if (candidatos.length() == 0) return "La IA no devolvio respuesta."



        // Dentro del primer candidate esta el contenido.
        val contenido = candidatos
            .getJSONObject(0)
            .optJSONObject("content")
            ?: return "La IA no devolvio contenido."

        // Dentro del contenido esta el texto.
        val partes = contenido.optJSONArray("parts") ?: return "La IA no devolvio texto."
        if (partes.length() == 0) return "La IA no devolvio texto."



        return partes.getJSONObject(0).optString("text", "La IA no devolvio texto.")
    }
}
