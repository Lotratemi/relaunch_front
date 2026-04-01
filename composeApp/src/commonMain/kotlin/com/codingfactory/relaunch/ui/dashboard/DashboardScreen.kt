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

data class Objective(val id: Int, val title: String)

@Composable
fun DashboardScreen(userName: String = "Philippe") {
    val objectives = remember {
        listOf(Objective(id = 1, title = "Se coucher plus tôt (22h)"))
    }

    var selectedId by remember { mutableStateOf<Int?>(null) }
    var expandedMenuId by remember { mutableStateOf<Int?>(null) }
    val trackedDays = remember { setOf(7, 8, 9, 10, 11, 12, 13, 14) }

    Scaffold(
        containerColor = AppBackground,
        bottomBar = { BottomBar(selectedTab = 0, onTabSelected = {}) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(Modifier.height(16.dp))

            Text(text = "Dashboard", color = TextSecondary, fontSize = 14.sp)
            Spacer(Modifier.height(4.dp))
            Text(text = "Relaunch", color = TextPrimary, fontSize = 34.sp, fontWeight = FontWeight.Bold)
            Text(text = "Tu peux le faire $userName !", color = TextPrimary, fontSize = 16.sp)

            Spacer(Modifier.height(14.dp))

            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
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
                            .fillMaxWidth(0.75f)
                            .clip(RoundedCornerShape(9.dp))
                            .background(brandGradient())
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text("🚀", fontSize = 26.sp)
            }

            Spacer(Modifier.height(28.dp))

            Text(text = "Objectifs clés", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))

            objectives.forEach { obj ->
                val isSelected = selectedId == obj.id
                val isMenuExpanded = expandedMenuId == obj.id

                Box(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(if (isSelected) brandGradient() else solidSurface())
                            .clickable {
                                selectedId = if (isSelected) null else obj.id
                                expandedMenuId = null
                            }
                            .padding(start = 16.dp, end = 4.dp, top = 4.dp, bottom = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .clip(CircleShape)
                                .background(if (isSelected) Color.White else OrangeStart)
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

            CalendarView(month = 4, year = 2026, trackedDays = trackedDays)

            Spacer(Modifier.height(24.dp))
        }
    }
}
