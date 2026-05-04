package com.codingfactory.relaunch.ui.coach

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
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
import com.codingfactory.relaunch.ui.theme.TextPrimary

data class ChatMessage(val text: String, val isUser: Boolean)

@Composable
fun CoachChatScreen(
    viewModel: CoachViewModel,
    userName: String,
    onComplete: () -> Unit
) {
    val state by viewModel.uiState.collectAsState()
    var inputText by remember { mutableStateOf("") }
    val listState = rememberLazyListState()

    val messages = state.messages
    val hasMessages = messages.isNotEmpty()
    val isTyping = state.isTyping
    val isResponding = state.isResponding
    val showConfirm = state.showConfirm

    val totalItems = messages.size + (if (isTyping) 1 else 0) + (if (showConfirm) 1 else 0)
    LaunchedEffect(messages.size, isTyping, showConfirm) {
        if (totalItems > 0) listState.animateScrollToItem(totalItems - 1)
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
                    Text("Bienvenue sur", fontSize = 22.sp, color = TextPrimary)
                    Text("Relaunch", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
                } else {
                    Text("Votre Coach", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = TextPrimary)
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
                                    onClick = { viewModel.declineObjectives() },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(24.dp),
                                    border = BorderStroke(1.dp, Color(0xFFBBBBBB)),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Gray)
                                ) {
                                    Text("Non", fontSize = 15.sp)
                                }
                                Button(
                                    onClick = { viewModel.confirmObjectives(onComplete) },
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
                visible = viewModel.canShowInput() && !isResponding,
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
                        IconButton(onClick = {
                            viewModel.sendMessage(inputText)
                            inputText = ""
                        }) {
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
