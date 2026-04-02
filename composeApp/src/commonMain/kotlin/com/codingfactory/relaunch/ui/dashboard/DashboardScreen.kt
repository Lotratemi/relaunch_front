package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.codingfactory.relaunch.ui.theme.*

data class Objective(val id: Int, val title: String, val isCheck : Boolean = false)

@Composable
fun DashboardScreen(userName: String = "") {
    val objectives = remember {
        mutableStateListOf(
            Objective(id = 1, title = "Prendre sa douche (21h)"),
            Objective(id = 2, title = "Se laver les dents (21h25)"),
            Objective(id = 3, title = "Se mettre un réveil (21h30)"),
            Objective(id = 4, title = "Se coucher plus tôt (22h)"),
        )
    }
    var selectedId by remember { mutableStateOf<Int?>(null) }
    var expandedMenuId by remember { mutableStateOf<Int?>(null) }
    val today = 18
    val trackedDays = remember { mutableStateOf(setOf(7, 8, 9, 10, 11, 12, 13, 17)) }
    val wrongTrackedDays = remember { mutableStateOf(setOf(6, 14, 15, 16)) }

    LaunchedEffect(objectives.map { it.isCheck }) {
        val allFinish = objectives.all {it.isCheck}
        val atLeastOne = objectives.any{ it.isCheck}

        if (allFinish) {
            trackedDays.value =  trackedDays.value + today
            wrongTrackedDays.value = wrongTrackedDays.value - today
        } else if (atLeastOne) {

        } else {
            trackedDays.value = trackedDays.value - today
            wrongTrackedDays.value = wrongTrackedDays.value + today
        }
    }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = { BottomBar(selectedTab = 0, onTabSelected = {}) }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding).verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(text = "Dashboard", color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(text = "Relaunch", color = TextPrimary, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text(text = "Tu peux le faire $userName !", color = TextPrimary, fontSize = 16.sp)

            Spacer(Modifier.height(14.dp))

            ProgressBar(objectives = objectives)

            Spacer(Modifier.height(28.dp))

            Text(text = "Objectifs clés", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            objectives.forEachIndexed { index, obj ->
                val isChecked = obj.isCheck
                val isMenuExpanded = expandedMenuId == obj.id

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isChecked) brandGradient() else solidSurface())
                            .clickable {
                                objectives[index] = obj.copy(isCheck = !obj.isCheck)
                                selectedId = if (selectedId == obj.id) null else obj.id
                            }
                            .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isChecked) Color.White else OrangeStart)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(text = obj.title, color = TextPrimary, fontSize = 14.sp, modifier = Modifier.weight(1f))
                        Box {
                            IconButton(onClick = { expandedMenuId = if (isMenuExpanded) null else obj.id }) {
                                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options", tint = TextPrimary)
                            }
                            DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { expandedMenuId = null }) {
                                DropdownMenuItem(text = { Text("Modifier") }, onClick = { expandedMenuId = null })
                                DropdownMenuItem(
                                    text = { Text("Supprimer", color = Color(0xFFFF3B30)) },
                                    onClick = { expandedMenuId = null }
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))
            }

            Text(
                text = "Plus d'objectifs à venir",
                color = TextSecondary,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(28.dp))

            Text(text = "Mon suivi", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            CalendarView(month = 4, year = 2026, trackedDays = trackedDays.value, wrongTrackedDays = wrongTrackedDays.value, objectives = objectives)

            Spacer(Modifier.height(24.dp))
        }
    }
}
