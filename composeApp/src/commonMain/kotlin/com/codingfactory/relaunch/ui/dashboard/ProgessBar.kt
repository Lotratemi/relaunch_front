package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.runtime.getValue
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.codingfactory.relaunch.ui.theme.SurfaceVariant
import com.codingfactory.relaunch.ui.theme.TextPrimary
import com.codingfactory.relaunch.ui.theme.brandGradient


@Composable
fun ProgressBar(objectives: List<Objective>, modifier: Modifier = Modifier) {
    // Calcul de la progression cible
    val progressTarget = remember(objectives.map { it.isCheck }) {
        if (objectives.isEmpty()) 0f
        else objectives.count { it.isCheck}.toFloat() / objectives.size
    }

    // Animation de la valeur
    val animatedProgress by animateFloatAsState(
        targetValue = progressTarget,
        animationSpec = tween(durationMillis = 600, easing = FastOutSlowInEasing),
        label = "BarProgress"
    )

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .height(18.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(SurfaceVariant)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(animatedProgress) // Utilise la valeur animée
                    .clip(RoundedCornerShape(9.dp))
                    .background(brandGradient())
            )
        }
        Spacer(Modifier.width(12.dp))
        Icon(
            imageVector = Icons.Default.RocketLaunch,
            contentDescription = "Launch Rocket",
            tint = TextPrimary
        )
    }
}