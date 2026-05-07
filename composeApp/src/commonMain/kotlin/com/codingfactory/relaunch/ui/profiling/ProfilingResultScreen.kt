package com.codingfactory.relaunch.ui.profiling

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.data.model.ProfileDimensionDto
import com.codingfactory.relaunch.data.model.ProfilingResponseDto
import com.codingfactory.relaunch.ui.onboarding.BlobsDecoration

@Composable
fun ProfilingResultScreen(
    result: ProfilingResponseDto,
    onStartCoaching: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Center
            ) {

                // ── Titre ─────────────────────────────────────────────────
                Text(
                    text = "Votre profil",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(4.dp))

                Text(
                    text = result.profile.profileType,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(Modifier.height(28.dp))

                // ── Dimensions ────────────────────────────────────────────
                Text(
                    text = "Vos dimensions",
                    fontSize = 14.sp,
                    color = Color.Gray
                )

                Spacer(Modifier.height(12.dp))

                result.dimensions.forEach { dimension ->
                    DimensionRow(dimension = dimension)
                    Spacer(Modifier.height(10.dp))
                }

                Spacer(Modifier.height(28.dp))

                // ── Bouton ────────────────────────────────────────────────
                OutlinedButton(
                    onClick = onStartCoaching,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Commencer mon coaching", fontSize = 16.sp)
                }
            }

            BlobsDecoration(modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}

@Composable
private fun DimensionRow(dimension: ProfileDimensionDto) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = dimension.dimension,
                fontSize = 14.sp,
                color = Color.Black,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = dimension.label,
                fontSize = 12.sp,
                color = Color.Gray
            )
        }

        Spacer(Modifier.height(4.dp))

        LinearProgressIndicator(
            progress = { dimension.score },
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(2.dp)),
            color = Color.Black,
            trackColor = Color(0xFFEEEEEE)
        )
    }
}