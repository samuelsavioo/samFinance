package com.example.samfinance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.samfinance.ai.ChatAgent
import com.example.samfinance.data.ChatDatabase
import com.example.samfinance.data.MessageEntity
import com.example.samfinance.data.SessionManager
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.launch

data class Message(val text: String, val isUser: Boolean)

@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { ChatDatabase.getDatabase(context) }
    val sessionManager = remember { SessionManager(context) }
    
    val userProfile by sessionManager.userProfile.collectAsState(initial = null)
    val dbMessages by db.messageDao().getAllMessages().collectAsState(initial = emptyList())
    
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Initialize ChatAgent with history from DB
    val chatAgent = remember(dbMessages) {
        val history = dbMessages.map { 
            content(role = if (it.isUser) "user" else "model") { text(it.text) }
        }
        ChatAgent(history)
    }

    LaunchedEffect(dbMessages.size) {
        if (dbMessages.isNotEmpty()) {
            listState.animateScrollToItem(dbMessages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(dbMessages) { message ->
                ChatBubble(Message(message.text, message.isUser))
            }
            if (isTyping) {
                item {
                    Text(
                        "SamFinance está pensando...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Gray
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = messageText,
                onValueChange = { messageText = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Digite sua dúvida...") },
                shape = RoundedCornerShape(24.dp),
                enabled = !isTyping
            )
            IconButton(
                enabled = !isTyping && messageText.isNotBlank(),
                onClick = {
                    val userText = messageText
                    messageText = ""
                    isTyping = true
                    
                    coroutineScope.launch {
                        // Save user message to DB
                        db.messageDao().insertMessage(MessageEntity(text = userText, isUser = true))
                        
                        // Get AI response
                        val response = chatAgent.sendMessage(userText, userProfile)
                        
                        // Save AI response to DB
                        db.messageDao().insertMessage(MessageEntity(text = response ?: "Erro na resposta", isUser = false))
                        
                        isTyping = false
                    }
                }
            ) {
                Icon(
                    Icons.Default.Send, 
                    contentDescription = "Enviar", 
                    tint = if (isTyping) Color.Gray else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ChatBubble(message: Message) {
    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val color = if (message.isUser) MaterialTheme.colorScheme.primary else Color.LightGray
    val textColor = if (message.isUser) Color.White else Color.Black

    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = alignment) {
        Surface(
            color = color,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = message.text,
                modifier = Modifier.padding(12.dp),
                color = textColor
            )
        }
    }
}
