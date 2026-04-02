package com.codingfactory.relaunch.ui.onboarding

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingProfileScreen(
    viewModel: OnboardingViewModel,
    isLoading: Boolean = false,
    error: String? = null
) {
    var name by remember { mutableStateOf("") }
    var mail by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var sex by remember { mutableStateOf("") }
    var sexExpanded by remember { mutableStateOf(false) }
    var showAutreDialog by remember { mutableStateOf(false) }
    var autreInput by remember { mutableStateOf("") }
    val sexOptions = listOf("Homme", "Femme", "Non-binaire", "Autre")

    Box(modifier = Modifier.fillMaxSize().background(Color.White)) {
        Column(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(text = "Bienvenue sur", fontSize = 28.sp, color = Color.Black)
                Text(text = "Relaunch", fontSize = 36.sp, fontWeight = FontWeight.Bold, color = Color.Black)

                Spacer(Modifier.height(32.dp))

                ProfileField(
                    label = "Votre prénom",
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    filter = { input -> input.filter { it.isLetter() || it == ' ' || it == '-' || it == '\'' } }
                )

                Spacer(Modifier.height(14.dp))

                ProfileField(
                    label = "Adresse e-mail",
                    value = mail,
                    onValueChange = { mail = it },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                )

                Spacer(Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ProfileField(
                        label = "Âge",
                        value = age,
                        onValueChange = { age = it },
                        modifier = Modifier.weight(1f),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        filter = { input -> input.filter { it.isDigit() }.take(3) }
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Genre", fontSize = 11.sp, color = Color.Gray)
                        Spacer(Modifier.height(2.dp))
                        ExposedDropdownMenuBox(
                            expanded = sexExpanded,
                            onExpandedChange = { sexExpanded = it }
                        ) {
                            OutlinedTextField(
                                value = sex,
                                onValueChange = {},
                                readOnly = true,
                                singleLine = true,
                                trailingIcon = {
                                    Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                                },
                                shape = RoundedCornerShape(24.dp),
                                modifier = Modifier.fillMaxWidth().menuAnchor(),
                                textStyle = TextStyle(fontSize = 14.sp, color = Color.Black),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedTextColor = Color.Black,
                                    unfocusedTextColor = Color.Black,
                                    focusedBorderColor = Color.Black,
                                    unfocusedBorderColor = Color(0xFFBBBBBB),
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    cursorColor = Color.Black
                                )
                            )
                            ExposedDropdownMenu(
                                expanded = sexExpanded,
                                onDismissRequest = { sexExpanded = false }
                            ) {
                                sexOptions.forEach { option ->
                                    DropdownMenuItem(
                                        text = { Text(option, fontSize = 14.sp) },
                                        onClick = {
                                            sexExpanded = false
                                            if (option == "Autre") {
                                                autreInput = ""
                                                showAutreDialog = true
                                            } else {
                                                sex = option
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(24.dp))

                OutlinedButton(
                    onClick = {
                        if (name.isNotBlank() && mail.isNotBlank()) {
                            viewModel.createUser(name, mail, age, sex)
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    enabled = !isLoading,
                    border = BorderStroke(1.dp, Color.Black),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Black)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = Color.Black
                        )
                    } else {
                        Text("Valider", fontSize = 16.sp)
                    }
                }

                if (error != null) {
                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = error,
                        fontSize = 12.sp,
                        color = Color(0xFFFF3B30)
                    )
                }
            }

            BlobsDecoration(modifier = Modifier.fillMaxWidth().height(220.dp))
        }

        if (showAutreDialog) {
            AlertDialog(
                onDismissRequest = { showAutreDialog = false },
                title = { Text("Précisez votre genre", fontWeight = FontWeight.Bold) },
                text = {
                    OutlinedTextField(
                        value = autreInput,
                        onValueChange = { autreInput = it },
                        placeholder = { Text("Votre genre", fontSize = 14.sp) },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Black,
                            unfocusedBorderColor = Color(0xFFBBBBBB),
                            cursorColor = Color.Black
                        )
                    )
                },
                confirmButton = {
                    TextButton(onClick = {
                        if (autreInput.isNotBlank()) sex = autreInput
                        showAutreDialog = false
                    }) {
                        Text("Confirmer", color = Color.Black)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showAutreDialog = false }) {
                        Text("Annuler", color = Color.Gray)
                    }
                }
            )
        }
    }
}
