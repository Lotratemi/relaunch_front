package com.codingfactory.relaunch.ui.theme

import androidx.compose.ui.graphics.*

val OrangeStart = Color(0xFFFF8C00)
val RedEnd = Color(0xFFFF1A1A)
val AppBackground = Color(0xFF1A1A1A)
val SurfaceVariant = Color(0xFF2C2C2E)
val TextPrimary = Color.White
val TextSecondary = Color(0xFF8E8E93)
val TextMuted = Color(0xFF48484A)

fun brandGradient(): Brush = Brush.horizontalGradient(listOf(OrangeStart, RedEnd))
fun solidSurface(): Brush = Brush.horizontalGradient(listOf(SurfaceVariant, SurfaceVariant))
