package com.codingfactory.relaunch.ui.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BlobsDecoration(modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            val leftRadius = h * 0.58f
            val leftCenter = Offset(w * 0.18f, h)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFF7033), Color(0xFFCC2200)),
                    center = Offset(leftCenter.x, leftCenter.y - leftRadius * 0.45f),
                    radius = leftRadius
                ),
                radius = leftRadius,
                center = leftCenter
            )

            val rightRadius = h * 0.92f
            val rightCenter = Offset(w * 0.72f, h)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(Color(0xFFFFBB33), Color(0xFFFF6600)),
                    center = Offset(rightCenter.x, rightCenter.y - rightRadius * 0.45f),
                    radius = rightRadius
                ),
                radius = rightRadius,
                center = rightCenter
            )
        }

        Text(
            text = "🐿",
            fontSize = 52.sp,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, bottom = 68.dp)
        )
    }
}
