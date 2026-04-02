package com.codingfactory.relaunch.ui.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*

@Composable
fun OnboardingProfileScreen(
    onValidate: (name: String, age: String, sex: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Bienvenue sur", fontSize = 28.sp, color = Color.Black)
                Text(text = "Relaunch", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(Modifier.height(40.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileField(label = "Votre prénom", value = name, onValueChange = { name = it }, modifier = Modifier.weight(2f))
                    ProfileField(label = "Age", value = age, onValueChange = { age = it }, modifier = Modifier.weight(1f))
                    ProfileField(label = "Sexe", value = sex, onValueChange = { sex = it }, modifier = Modifier.weight(1.3f))
                }

                Spacer(Modifier.height(20.dp))

                OutlinedButton(
                    onClick = { if (name.isNotBlank()) onValidate(name, age, sex) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    Text("Valider", fontSize = 16.sp)
                }
            }

            BlobsDecoration(modifier = Modifier.fillMaxWidth().height(220.dp))
        }
    }
}
