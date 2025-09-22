package me.idkbemja.aichatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import me.idkbemja.aichatapp.templates.ChatScreen
import me.idkbemja.aichatapp.ui.theme.AIChatAppTheme
import androidx.compose.runtime.*
import me.idkbemja.aichatapp.templates.LoginScreen
import me.idkbemja.aichatapp.utils.TokenManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TokenManager.clearToken(this)
        enableEdgeToEdge()
        setContent {
            AIChatAppTheme {
                var isLoggedIn by remember { mutableStateOf(false) }
                if (isLoggedIn) {
                    ChatScreen(onLogout = { isLoggedIn = false })
                } else {
                    LoginScreen(
                        onLoginSuccess = { isLoggedIn = true }
                    )
                }
            }
        }
    }
}
