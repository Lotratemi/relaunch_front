package com.codingfactory.relaunch.ui.coach

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingfactory.relaunch.ui.theme.OrangeStart

@Composable
internal fun ChatBubble(message: ChatMessage) {
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
