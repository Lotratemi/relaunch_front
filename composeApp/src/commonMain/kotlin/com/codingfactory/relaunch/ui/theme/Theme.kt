package com.codingfactory.relaunch.ui.theme

import androidx.compose.ui.graphics.*

val OrangeStart = Color(0xFFFF8C00)
val RedEnd = Color(0xFFFF1A1A)
val AppBackground = Color(0xFFFFFFFF)
val SurfaceVariant = Color(0xFFEEEEEE)
val TextPrimary = Color.Black
val TextWhite = Color.White
val TextSecondary = Color(0xFF4C4C4C)

fun brandGradient(): Brush = Brush.horizontalGradient(listOf(OrangeStart, RedEnd))
fun solidSurface(): Brush = Brush.horizontalGradient(listOf(SurfaceVariant, SurfaceVariant))
