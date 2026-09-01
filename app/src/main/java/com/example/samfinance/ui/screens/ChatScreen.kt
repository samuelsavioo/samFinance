package com.example.samfinance.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val db = remember { ChatDatabase.getDatabase(context) }
    val sessionManager = remember { SessionManager(context) }
    val snackbarHostState = remember { SnackbarHostState() }
    
    val userProfile by sessionManager.userProfile.collectAsState(initial = null)
    val dbMessages by db.messageDao().getAllMessages().collectAsState(initial = emptyList())
    
    var messageText by remember { mutableStateOf("") }
    var isTyping by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()

    // Clean error messages from DB on initial load
    LaunchedEffect(Unit) {
        db.messageDao().deleteErrorMessages()
    }

    // Default welcome message if DB is completely empty
    val displayMessages = if (dbMessages.isEmpty()) {
        listOf(MessageEntity(text = "Olá! Sou o SamFinance. Como posso ajudar com suas finanças hoje?", isUser = false))
    } else {
        dbMessages
    }

    // Initialize ChatAgent with history from DB (filtering out errors)
    val chatAgent = remember(dbMessages) {
        val validHistory = dbMessages
            .filter { !it.text.startsWith("Desculpe") && !it.text.startsWith("Unexpected Response") }
            .map { content(role = if (it.isUser) "user" else "model") { text(it.text) } }
        ChatAgent(validHistory)
    }

    LaunchedEffect(displayMessages.size) {
        if (displayMessages.isNotEmpty()) {
            listState.animateScrollToItem(displayMessages.size - 1)
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("SamFinance Chat") },
                actions = {
                    IconButton(onClick = {
                        coroutineScope.launch {
                            db.messageDao().clearChat()
                            snackbarHostState.showSnackbar("Histórico do chat limpo.")
                        }
                    }) {
                        Icon(Icons.Default.Delete, contentDescription = "Limpar Chat")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(displayMessages) { message ->
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
                Spacer(modifier = Modifier.width(8.dp))
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
                            val result = chatAgent.sendMessage(userText, userProfile)
                            
                            isTyping = false
                            
                            result.onSuccess { responseText ->
                                db.messageDao().insertMessage(MessageEntity(text = responseText, isUser = false))
                            }.onFailure { error ->
                                val errorMessage = error.message ?: "Erro desconhecido ao se comunicar com a IA."
                                snackbarHostState.showSnackbar("Erro: $errorMessage")
                            }
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
