package me.idkbemja.aichatapp.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.Color

// Utilidad mejorada para formatear texto especial en el chat
@Composable
fun formatChatText(text: String): AnnotatedString {
    val builder = AnnotatedString.Builder()
    val lines = text.split("\n")
    for (line in lines) {
        when {
            // Título principal: **Titulo**
            line.matches(Regex("^\\*\\*(.+)\\*\\*$")) -> {
                val title = Regex("^\\*\\*(.+)\\*\\*$").find(line)?.groupValues?.get(1) ?: line
                builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFFB0AAAA)))
                builder.append(title + "\n")
                builder.pop()
            }
            // Subtítulo: ## Subtitulo
            line.startsWith("##") -> {
                builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFFB0AAAA)))
                builder.append(line.removePrefix("## ") + "\n")
                builder.pop()
            }
            // Lista simple: - item
            line.trim().startsWith("- ") -> {
                builder.pushStyle(SpanStyle(fontSize = 15.sp, color = Color.White))
                builder.append("• " + line.trim().removePrefix("- ") + "\n")
                builder.pop()
            }
            // Negrita en línea: **texto**
            line.contains("**") -> {
                var lastIndex = 0
                val regex = Regex("\\*\\*(.*?)\\*\\*")
                val matches = regex.findAll(line)
                for (match in matches) {
                    val start = match.range.first
                    val end = match.range.last + 1
                    if (start > lastIndex) {
                        builder.append(line.substring(lastIndex, start))
                    }
                    val boldText = match.groupValues[1]
                    builder.pushStyle(SpanStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color.White))
                    builder.append(boldText)
                    builder.pop()
                    lastIndex = end
                }
                if (lastIndex < line.length) {
                    builder.append(line.substring(lastIndex))
                }
                builder.append("\n")
            }
            // Texto normal
            else -> {
                builder.pushStyle(SpanStyle(fontSize = 15.sp, color = Color.White))
                builder.append(line + "\n")
                builder.pop()
            }
        }
    }
    return builder.toAnnotatedString()
}
