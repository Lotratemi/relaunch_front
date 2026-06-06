package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.*
import com.codingfactory.relaunch.ui.theme.*
import com.codingfactory.relaunch.ui.congrat.CongratSuccesScreen
import com.codingfactory.relaunch.ui.congrat.CongratFailScreen

data class Objective(val id: Int, val title: String, val isCheck: Boolean = false)

@Composable
fun DashboardScreen(viewModel: DashboardViewModel) {
    val state by viewModel.uiState.collectAsState()
    val congratScreen by viewModel.congratScreen.collectAsState()
    var expandedMenuId by remember { mutableStateOf<Int?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = AppBackground,
            bottomBar = { BottomBar(selectedTab = 0, onTabSelected = {}) }
        ) { innerPadding ->
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize().padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = OrangeStart)
                }
            } else {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding)
                        .verticalScroll(rememberScrollState()).padding(horizontal = 20.dp)
                ) {
                    Spacer(Modifier.height(16.dp))

                    Spacer(Modifier.height(4.dp))
                    Text(text = "Relaunch", color = TextPrimary, fontSize = 34.sp, fontWeight = FontWeight.Bold)
                    Text(text = "Tu peux le faire ${viewModel.userName} !", color = TextPrimary, fontSize = 16.sp)

                    Spacer(Modifier.height(14.dp))

                    ProgressBar(objectives = state.objectives)

                    Spacer(Modifier.height(28.dp))

                    Text(text = "Objectifs clés", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    state.objectives.forEachIndexed { index, obj ->
                        val isChecked = obj.isCheck
                        val isMenuExpanded = expandedMenuId == obj.id

                        Box(modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(14.dp))
                                    .background(if (isChecked) brandGradient() else solidSurface())
                                    .clickable { viewModel.toggleObjective(index) }
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
                                Text(text = obj.title, color = (if (isChecked) TextWhite else TextPrimary) , fontSize = 14.sp, modifier = Modifier.weight(1f))
                                Box {
                                    IconButton(onClick = { expandedMenuId = if (isMenuExpanded) null else obj.id }) {
                                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options", tint = (if (isChecked) TextWhite else TextPrimary))                              }
                                    DropdownMenu(expanded = isMenuExpanded, onDismissRequest = { expandedMenuId = null }, containerColor = TextWhite) {
                                        DropdownMenuItem(text = { Text("Modifier") }, onClick = { expandedMenuId = null })
                                        DropdownMenuItem(
                                            text = { Text("Supprimer", color = RedEnd ) },
                                            onClick = {
                                                expandedMenuId = null
                                                viewModel.deleteObjective(index)
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                    }

                    Spacer(Modifier.height(28.dp))

                    Text(text = "Mon suivi", color = TextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.height(12.dp))

                    CalendarView(
                        month = state.displayedMonth,
                        year = state.displayedYear,
                        trackedDays = state.trackedDays,
                        wrongTrackedDays = state.wrongTrackedDays,
                        objectives = state.objectives,
                        today = state.currentDay,
                    )

                    Spacer(Modifier.height(24.dp))
                }
            }
        }

        when (congratScreen) {
            "SUCCESS" -> CongratSuccesScreen(onContinueClick = { viewModel.dismissCongratsScreen() })
            "FAIL" -> CongratFailScreen(onContinueClick = { viewModel.dismissCongratsScreen() })
        }
    }
}