package com.codingfactory.relaunch.ui.congrat

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.*

@Composable
fun DaysOfWeekRow() {
    val days = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        days.forEach { day ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = day,
                    color = TextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "Validé",
                    tint = OrangeStart,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}