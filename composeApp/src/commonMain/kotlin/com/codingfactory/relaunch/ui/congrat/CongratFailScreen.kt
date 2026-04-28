package com.codingfactory.relaunch.ui.congrat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.*

@Composable
fun CongratFailScreen(onContinueClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = brandGradient())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
                    .background(Color.White.copy(alpha = 0.2f)), // Effet de halo
                contentAlignment = Alignment.Center
            ) {
                Text("Avatar ici", color = Color.White)
            }

            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                shape = RoundedCornerShape(50.dp),
                color = Color.White,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Courage !",
                    fontWeight = FontWeight.ExtraBold,
                    color = OrangeStart, // Utilise ton orange
                    fontSize = 32.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Tu es sur la bonne voie !\n" + "Tu te rapproches de ton objectif ! !",
                color = Color.White,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(0.8f)
            )

            Button(
                onClick = onContinueClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(50.dp), // Bouton très arrondi
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
            ) {
                Text(
                    text = "Suivant",
                    color = RedEnd,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}