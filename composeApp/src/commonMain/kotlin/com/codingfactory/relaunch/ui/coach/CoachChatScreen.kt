package com.codingfactory.relaunch.ui.coach

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.OrangeStart
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
    var isTyping by remember { mutableStateOf(false) }
    var isResponding by remember { mutableStateOf(false) }
    var showConfirm by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val hasMessages = messages.isNotEmpty()

    val totalItems = messages.size + (if (isTyping) 1 else 0) + (if (showConfirm) 1 else 0)
    LaunchedEffect(totalItems) {
        if (totalItems > 0) listState.animateScrollToItem(totalItems - 1)
    }

    fun sendMessage() {
        val text = inputText.trim()
        if (text.isBlank() || isResponding) return
        messages.add(ChatMessage(text, isUser = true))
        inputText = ""
        if (responseStep < responses.size) {
            val currentResponses = responses[responseStep]
            val isLast = responseStep == responses.size - 1
            responseStep++
            isResponding = true
            scope.launch {
                delay(600)
                isTyping = true
                delay(1200)
                isTyping = false
                messages.add(ChatMessage(currentResponses[0], isUser = false))
                if (currentResponses.size > 1) {
                    delay(500)
                    isTyping = true
                    delay(1000)
                    isTyping = false
                    messages.add(ChatMessage(currentResponses[1], isUser = false))
                }
                if (isLast) {
                    delay(400)
                    showConfirm = true
                }
                isResponding = false
            }
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

                if (isTyping) {
                    item { TypingBubble() }
                }

                if (showConfirm) {
                    item {
                        Spacer(Modifier.height(8.dp))
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                "Es-tu prêt(e) à découvrir ton tableau de bord ?",
                                fontSize = 15.sp,
                                color = Color.Black,
                                textAlign = TextAlign.Center
                            )
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {},
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBBBBB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                                ) {
                                    Text("Non", fontSize = 15.sp)
                                }
                                Button(
                                    onClick = onComplete,
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = OrangeStart)
                                ) {
                                    Text("Oui", color = Color.White, fontSize = 15.sp)
                                }
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(
                visible = responseStep < responses.size && !isResponding,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { it }
                )
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it },
                        placeholder = {
                            Text(
                                "Parlez d'un de vos problèmes à notre coach",
                                fontSize = 13.sp
                            )
                        },
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
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Send,
                                contentDescription = "Envoyer",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
