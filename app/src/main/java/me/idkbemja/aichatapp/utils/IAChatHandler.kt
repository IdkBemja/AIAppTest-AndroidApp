package me.idkbemja.aichatapp.utils

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class IAChatHandler {
    /**
     * Envía una pregunta al endpoint de IA y retorna la respuesta como String.
     * @param context Contexto para obtener el token si es necesario.
     * @param question Pregunta del usuario.
     * @param token JWT para autenticación.
     * @return Respuesta de la IA o mensaje de error.
     */
    suspend fun ask(context: Context, question: String, token: String): String = withContext(Dispatchers.IO) {
        val urlString = "https://aichattest.urrahost.app/api/ask"
        try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; utf-8")
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.doOutput = true

            // Crear el JSON con la pregunta
            val json = JSONObject()
            json.put("question", question)

            val outputWriter = OutputStreamWriter(conn.outputStream)
            outputWriter.write(json.toString())
            outputWriter.flush()
            outputWriter.close()

            val responseCode = conn.responseCode
            val inputStream = if (responseCode in 200..299) conn.inputStream else conn.errorStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            val responseJson = JSONObject(response.toString())
            responseJson.optString("answer", responseJson.optString("response", "Error: respuesta inesperada del servidor"))
        } catch (e: Exception) {
            e.printStackTrace()
            "Error de conexión: ${e.toString()}"
        }
    }
}
