package me.idkbemja.aichatapp.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import me.idkbemja.aichatapp.utils.AuthHandler
import me.idkbemja.aichatapp.utils.TokenManager
import android.content.Intent
import android.net.Uri

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    val context = LocalContext.current
    val authHandler = remember { AuthHandler() }
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize().background(Color(0xFF232121))) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "AI Chat Test",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Text(
                text = "Username:",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 48.dp, bottom = 2.dp) // Más padding a la izquierda
                    .align(Alignment.Start)
            )
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                singleLine = true, // Solo una línea
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF5B5757),
                    focusedContainerColor = Color(0xFF5B5757),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White
                ),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(0.85f),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Password:",
                color = Color.White,
                fontSize = 16.sp,
                modifier = Modifier
                    .padding(start = 48.dp, bottom = 2.dp) // Más padding a la izquierda
                    .align(Alignment.Start)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true, // Solo una línea
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF5B5757),
                    focusedContainerColor = Color(0xFF5B5757),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent
                ),
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .padding(horizontal = 32.dp)
                    .fillMaxWidth(0.85f),
                textStyle = LocalTextStyle.current.copy(
                    color = Color.White,
                    fontSize = 16.sp
                )
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(
                onClick = {
                    if (username.isNotBlank() && password.isNotBlank()) {
                        isLoading = true
                        scope.launch {
                            val result = authHandler.login(username, password)
                            isLoading = false
                            if (result.token != null) {
                                TokenManager.saveToken(context, result.token)
                                onLoginSuccess()
                            } else {
                                snackbarHostState.showSnackbar(result.errorMessage ?: "Login failed")
                            }
                        }
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar("Por favor, completa ambos campos")
                        }
                    }
                },
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                modifier = Modifier
                    .width(160.dp)
                    .height(48.dp),
                enabled = !isLoading
            ) {
                Text("Login", color = Color.Black, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
            SnackbarHost(hostState = snackbarHostState)
        }
        Text(
            text = "Developed by UrraHosting",
            color = Color.White,
            fontSize = 14.sp,
            textDecoration = TextDecoration.Underline,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 24.dp)
                .clickable {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.urrahost.app"))
                    context.startActivity(intent)
                }
        )
    }
}
