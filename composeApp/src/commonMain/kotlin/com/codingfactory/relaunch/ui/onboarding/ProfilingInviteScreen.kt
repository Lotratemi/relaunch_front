package com.codingfactory.relaunch.ui.onboarding
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*

@Composable
fun ProfilingInviteScreen(
    userName: String,
    onStartProfiling: () -> Unit,
    onSkip: () -> Unit          // → Dashboard directement
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Bienvenue $userName 👋",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        Text(
            text = "Pour personnaliser votre coaching, nous aimerions mieux vous connaître.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))

        Button(
            onClick = onStartProfiling,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Faire le questionnaire maintenant")
        }

        Spacer(Modifier.height(12.dp))

        TextButton(
            onClick = onSkip,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Plus tard", color = MaterialTheme.colorScheme.outline)
        }
    }
}