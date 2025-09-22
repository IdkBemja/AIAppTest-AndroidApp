package me.idkbemja.aichatapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Clase utilitaria para guardar, recuperar y borrar el token JWT en SharedPreferences.
 * Uso:
 *   TokenManager.saveToken(context, token);
 *   String token = TokenManager.getToken(context);
 *   TokenManager.clearToken(context);
 */
public class TokenManager {
    private static final String PREFS_NAME = "auth_prefs";
    private static final String TOKEN_KEY = "jwt_token";

    // Guarda el token en SharedPreferences
    public static void saveToken(Context context, String token) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(TOKEN_KEY, token).apply();
    }

    // Recupera el token guardado
    public static String getToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return prefs.getString(TOKEN_KEY, null);
    }

    // Borra el token (por ejemplo, al hacer logout)
    public static void clearToken(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefs.edit().remove(TOKEN_KEY).apply();
    }
}
