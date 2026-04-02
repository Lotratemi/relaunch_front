package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.*

@Composable
internal fun CalendarView(month: Int, year: Int, trackedDays: Set<Int>, wrongTrackedDays: Set<Int>, objectives: List<Objective>, today: Int = 18) {
    val dayHeaders = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")
    val firstDay = firstDayOfWeek(month, year)
    val totalDays = daysInMonth(month, year)

    Box(
        modifier = Modifier.fillMaxWidth().border(1.dp, SurfaceVariant, RoundedCornerShape(16.dp)).clip(RoundedCornerShape(16.dp)).background(AppBackground).padding(12.dp)
    ) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                dayHeaders.forEach { label ->
                    Text(
                        text = label,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            val totalCells = firstDay + totalDays
            val rows = (totalCells + 6) / 7

            for (row in 0 until rows) {
                Row(modifier = Modifier.fillMaxWidth()) {
                    for (col in 0 until 7) {
                        val day = row * 7 + col - firstDay + 1
                        Box(
                            modifier = Modifier.weight(1f).aspectRatio(1f).padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            when {
                                day < 1 -> {}
                                day > totalDays -> {}
                                day == today -> {
                                    val checkCount = objectives.count {it.isCheck}
                                    val completionRatio = if (objectives.isEmpty()) 0f else checkCount.toFloat() / objectives.size

                                    Box(
                                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        if (checkCount > 0){
                                            Box(modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp))
                                                .background(
                                                    OrangeStart.copy(alpha = completionRatio.coerceAtLeast(0.2f))),
                                            )
                                        }
                                        Text(text = day.toString(), color = Color.White, fontWeight = FontWeight.Normal)

                                    }

                                }
                                day in trackedDays -> {
                                    DayBox(day, OrangeStart)
                                }
                                day in wrongTrackedDays -> {
                                    DayBox(day, RedEnd)
                                }
                                else -> {
                                    Text(
                                        text = day.toString(),
                                        color = Color.White,
                                        fontSize = 13.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

internal fun firstDayOfWeek(month: Int, year: Int): Int {
    val t = intArrayOf(0, 3, 2, 5, 0, 3, 5, 1, 4, 6, 2, 4)
    val y = if (month < 3) year - 1 else year
    return (y + y / 4 - y / 100 + y / 400 + t[month - 1] + 1) % 7
}

@Composable
fun DayBox(day: Int, color: Color) {
    Box(
        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(8.dp)).background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(text = day.toString(), color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

internal fun daysInMonth(month: Int, year: Int): Int = when (month) {
    1, 3, 5, 7, 8, 10, 12 -> 31
    4, 6, 9, 11 -> 30
    2 -> if (year % 4 == 0 && (year % 100 != 0 || year % 400 == 0)) 29 else 28
    else -> 0
}
