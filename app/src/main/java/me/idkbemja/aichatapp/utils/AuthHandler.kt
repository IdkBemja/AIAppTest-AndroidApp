package me.idkbemja.aichatapp.utils

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.net.URL
import org.json.JSONObject
import javax.net.ssl.HttpsURLConnection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

// Clase para encapsular el resultado del login
// Si es exitoso, token tendrá valor y errorMessage será null
// Si falla, token será null y errorMessage tendrá el mensaje de error
class LoginResult(val token: String?, val errorMessage: String?)

class AuthHandler {
    /**
     * Realiza un login enviando usuario y contraseña por POST a una URL.
     * Es suspend para ejecutarse en background.
     */
    suspend fun login(username: String, password: String): LoginResult = withContext(Dispatchers.IO) {
        val urlString = "https://aichattest.urrahost.app/api/login"
        try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; utf-8")
            conn.doOutput = true

            // Crear el JSON con usuario y contraseña
            val json = JSONObject()
            json.put("username", username)
            json.put("password", password)

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
            if (responseCode == 200) {
                // Login exitoso: extraer el token
                val token = responseJson.optString("token", null)
                if (token != null) {
                    LoginResult(token, null)
                } else {
                    // Si no hay token, algo salió mal
                    LoginResult(null, "Respuesta inesperada del servidor")
                }
            } else {
                // Login fallido: extraer el mensaje de error si existe
                val errorMsg = responseJson.optString("error", responseJson.optString("message", "Error desconocido"))
                LoginResult(null, errorMsg)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Mostrar el mensaje real de la excepción para mejor depuración
            LoginResult(null, "Error de conexión: ${e.toString()}")
        }
    }

    /**
     * Realiza logout enviando el token por POST en background.
     */
    suspend fun logout(context: Context, token: String): Boolean = withContext(Dispatchers.IO) {
        val urlString = "https://aichattest.urrahost.app/api/logout"
        try {
            val url = URL(urlString)
            val conn = url.openConnection() as HttpsURLConnection
            conn.requestMethod = "POST"
            // Agregar el token JWT en el header Authorization
            conn.setRequestProperty("Authorization", "Bearer $token")
            conn.doOutput = true

            val responseCode = conn.responseCode
            if (responseCode == 200) {
                // Si el logout es exitoso, eliminamos el token guardado
                TokenManager.clearToken(context)
                true
            } else {
                false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}