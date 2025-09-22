package me.idkbemja.aichatapp.templates

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.launch
import me.idkbemja.aichatapp.utils.AuthHandler
import me.idkbemja.aichatapp.utils.TokenManager
import me.idkbemja.aichatapp.utils.IAChatHandler
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun ChatScreen(onLogout: (() -> Unit)? = null) {
    var messages by remember { mutableStateOf(listOf<ChatMessage>()) }
    var input by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val authHandler = remember { AuthHandler() }
    val iaHandler = remember { IAChatHandler() }
    val scope = rememberCoroutineScope()
    var logoutError by remember { mutableStateOf<String?>(null) }
    var isWaiting by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF232121))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF181717))
                .padding(vertical = 12.dp, horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "AI Chat Test",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
            IconButton(onClick = {
                val token = TokenManager.getToken(context)
                if (token != null) {
                    scope.launch {
                        val result = authHandler.logout(context, token)
                        if (result) {
                            onLogout?.invoke()
                        } else {
                            logoutError = "Error al cerrar sesión. Intenta de nuevo."
                        }
                    }
                }
            }) {
                Icon(Icons.Default.Logout, contentDescription = "Logout", tint = Color.White)
            }
        }
        // Lista de mensajes (scrollable)
        LazyColumn(
            state = listState,
            modifier = Modifier
                .padding(top = 80.dp, bottom = 72.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            item { Spacer(modifier = Modifier.height(8.dp)) }
            items(messages) { msg ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = if (msg.isUser) Arrangement.End else Arrangement.Start
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(16.dp))
                            .background(if (msg.isUser) Color(0xFFB0AAAA) else Color(0xFF3A3737))
                            .padding(12.dp)
                            .widthIn(max = 300.dp)
                    ) {
                        Text(
                            text = msg.text,
                            color = Color.White,
                            fontSize = 15.sp
                        )
                    }
                }
            }
            if (isWaiting) {
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFF3A3737))
                                .padding(12.dp)
                                .widthIn(max = 200.dp)
                        ) {
                            Text(
                                text = "Esperando respuesta...",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .background(Color.Transparent)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF5B5757))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                BasicTextField(
                    value = input,
                    onValueChange = { input = it },
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        fontFamily = androidx.compose.ui.text.font.FontFamily.Default
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            if (input.isNotBlank() && !isWaiting) {
                                val userMsg = input.trim()
                                messages = messages + ChatMessage(userMsg, true)
                                input = ""
                                isWaiting = true
                                val token = TokenManager.getToken(context)
                                keyboardController?.hide()
                                scope.launch {
                                    listState.animateScrollToItem(messages.size)
                                    if (token != null) {
                                        val iaResponse = iaHandler.ask(context, userMsg, token)
                                        isWaiting = false
                                        messages = messages + ChatMessage(iaResponse, false)
                                        listState.animateScrollToItem(messages.size)
                                    } else {
                                        isWaiting = false
                                        messages = messages + ChatMessage("Error: sesión no válida.", false)
                                        listState.animateScrollToItem(messages.size)
                                    }
                                }
                            }
                        }
                    )
                )
                if (input.isEmpty()) {
                    Text(
                        text = "Ask Anything",
                        color = Color(0xFFBDBDBD),
                        fontSize = 16.sp
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {
                    if (input.isNotBlank() && !isWaiting) {
                        val userMsg = input.trim()
                        messages = messages + ChatMessage(userMsg, true)
                        input = ""
                        isWaiting = true
                        val token = TokenManager.getToken(context)
                        keyboardController?.hide()
                        scope.launch {
                            listState.animateScrollToItem(messages.size)
                            if (token != null) {
                                val iaResponse = iaHandler.ask(context, userMsg, token)
                                isWaiting = false
                                messages = messages + ChatMessage(iaResponse, false)
                                listState.animateScrollToItem(messages.size)
                            } else {
                                isWaiting = false
                                messages = messages + ChatMessage("Error: sesión no válida.", false)
                                listState.animateScrollToItem(messages.size)
                            }
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(Color(0xFF5B5757), RoundedCornerShape(24.dp))
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send", tint = Color.White)
            }
        }
        if (logoutError != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                Text(
                    text = logoutError!!,
                    color = Color.Red,
                    modifier = Modifier.padding(top = 64.dp)
                )
            }
        }
    }
}