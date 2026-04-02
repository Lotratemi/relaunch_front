package com.codingfactory.relaunch.ui.coach

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.codingfactory.relaunch.ui.theme.OrangeStart
import androidx.compose.ui.unit.*
import kotlinx.coroutines.delay

@Composable
internal fun ChatBubble(message: ChatMessage) {
    var visible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { visible = true }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
            animationSpec = tween(300),
            initialOffsetY = { it / 2 }
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
        ) {
            if (message.isUser) {
                Box(
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .background(OrangeStart, RoundedCornerShape(20.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(message.text, fontSize = 14.sp, color = Color.White)
                }
            } else {
                Box(
                    modifier = Modifier
                        .widthIn(max = 260.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Text(message.text, fontSize = 14.sp, color = Color.Black)
                }
            }
        }
    }
}

@Composable
internal fun TypingBubble() {
    var dotCount by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(400)
            dotCount = (dotCount % 3) + 1
        }
    }

    Row(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(
                                color = if (i < dotCount) Color(0xFF888888) else Color(0xFFCCCCCC),
                                shape = CircleShape
                            )
                    )
                }
            }
        }
    }
}
