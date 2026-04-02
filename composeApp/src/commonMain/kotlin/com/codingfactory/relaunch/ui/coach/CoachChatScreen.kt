package com.codingfactory.relaunch.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.OrangeStart

data class ChatMessage(val text: String, val isUser: Boolean)

private fun coachResponses(name: String) = listOf(
    listOf(
        "Ne t'inquiète pas $name, on va trouver une solution réalisable !",
        "As tu déjà identifié le ou les éléments qui rabaissent ton moral ?"
    ),
    listOf(
        "Je comprends. Le boulot c'est toujours un puit sans fond de fatigue.",
        "À quelle heure te couches tu et à quelle heure te réveilles tu ?"
    ),
    listOf(
        "Je vois le problème ! Un manque de sommeil peut tout dérégler.",
        "Je te propose de commencer par te coucher à 22h. Je vais créer cet objectif pour toi !"
    )
)

@Composable
fun CoachChatScreen(
    userName: String,
    onComplete: () -> Unit
) {
    val responses = remember(userName) { coachResponses(userName) }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    var inputText by remember { mutableStateOf("") }
    var responseStep by remember { mutableStateOf(0) }
    val listState = rememberLazyListState()

    val hasMessages = messages.isNotEmpty()
    val isConversationDone = responseStep >= responses.size

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) listState.animateScrollToItem(messages.lastIndex)
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isBlank()) return
        messages.add(ChatMessage(text, isUser = true))
        inputText = ""
        if (responseStep < responses.size) {
            responses[responseStep].forEach { messages.add(ChatMessage(it, isUser = false)) }
            responseStep++
        }
    }

    Scaffold(containerColor = Color.White) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .imePadding()
        ) {
            Column(modifier = Modifier.padding(horizontal = 24.dp).padding(top = 40.dp, bottom = 16.dp)) {
                if (!hasMessages) {
                    Text("Bienvenue sur", fontSize = 22.sp, color = Color.Black)
                    Text("Relaunch", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                } else {
                    Text("Votre Coach", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = Color.Black)
                    Text("Mettez en place votre premier objectif clé", fontSize = 14.sp, color = Color.Gray)
                }
            }

            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { msg -> ChatBubble(msg) }
                if (isConversationDone) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Button(
                            onClick = onComplete,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = OrangeStart)
                        ) {
                            Text("Voir mon tableau de bord", color = Color.White, fontSize = 15.sp)
                        }
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Parlez d'un de vos problèmes à notre coach", fontSize = 13.sp) },
                    shape = RoundedCornerShape(24.dp),
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Color(0xFFDDDDDD),
                        unfocusedBorderColor = Color(0xFFDDDDDD)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Box(
                    modifier = Modifier.size(46.dp).clip(CircleShape).background(OrangeStart),
                    contentAlignment = Alignment.Center
                ) {
                    IconButton(onClick = { sendMessage() }) {
                        Icon(imageVector = Icons.Default.Send, contentDescription = "Envoyer", tint = Color.White, modifier = Modifier.size(20.dp))
                    }
                }
            }
        }
    }
}
