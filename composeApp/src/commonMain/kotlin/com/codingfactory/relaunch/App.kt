package com.codingfactory.relaunch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.codingfactory.relaunch.ui.coach.CoachChatScreen
import com.codingfactory.relaunch.ui.coach.CoachViewModel
import com.codingfactory.relaunch.ui.dashboard.DashboardScreen
import com.codingfactory.relaunch.ui.dashboard.DashboardViewModel
import com.codingfactory.relaunch.ui.onboarding.OnboardingProfileScreen
import com.codingfactory.relaunch.ui.onboarding.OnboardingViewModel
import com.codingfactory.relaunch.ui.onboarding.ProfilingInviteScreen
import com.codingfactory.relaunch.ui.profiling.ProfilingResultScreen
import com.codingfactory.relaunch.ui.profiling.ProfilingScreen
import com.codingfactory.relaunch.ui.profiling.ProfilingViewModel
import com.codingfactory.relaunch.data.model.ProfilingResponseDto

private enum class AppScreen { ONBOARDING, PROFILING_INVITE, PROFILING, PROFILING_RESULT, COACH, DASHBOARD }

@Composable
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf(AppScreen.ONBOARDING) }
        var userId by remember { mutableStateOf(0L) }
        var userName by remember { mutableStateOf("") }
        var profilingResult by remember { mutableStateOf<ProfilingResponseDto?>(null) }

        when (screen) {
            AppScreen.ONBOARDING -> {
                val viewModel = viewModel { OnboardingViewModel() }
                val state by viewModel.uiState.collectAsState()

                LaunchedEffect(state.userId) {
                    if (state.userId != null) {
                        userId = state.userId!!
                        userName = state.userName
                        screen = AppScreen.PROFILING_INVITE
                    }
                }

                OnboardingProfileScreen(
                    viewModel = viewModel,
                    isLoading = state.isLoading,
                    error = state.error
                )
            }

            AppScreen.PROFILING_INVITE -> {
                ProfilingInviteScreen(
                    userName = userName,
                    onStartProfiling = { screen = AppScreen.PROFILING },
                    onSkip = { screen = AppScreen.COACH }
                )
            }

            AppScreen.PROFILING -> {
                val viewModel = viewModel { ProfilingViewModel() }
                val state by viewModel.uiState.collectAsState()

                // ── quand résultat reçu → résultat screen ──
                LaunchedEffect(state.result) {
                    if (state.result != null) screen = AppScreen.PROFILING_RESULT
                }

                ProfilingScreen(
                    viewModel = viewModel,
                    userId = userId,
                    isLoading = state.isLoading,
                    error = state.error
                )
            }


            AppScreen.PROFILING_RESULT -> {
                val viewModel = viewModel { ProfilingViewModel() }
                val state by viewModel.uiState.collectAsState()

                state.result?.let { result ->
                    ProfilingResultScreen(
                        result = result,
                        onStartCoaching = { screen = AppScreen.COACH }
                    )
                }
            }

            AppScreen.COACH -> {
                val viewModel = viewModel { CoachViewModel(userId, userName) }
                CoachChatScreen(
                    viewModel = viewModel,
                    userName = userName,
                    onComplete = { screen = AppScreen.DASHBOARD }
                )
            }

            AppScreen.DASHBOARD -> {
                val viewModel = viewModel { DashboardViewModel(userId, userName) }
                DashboardScreen(viewModel = viewModel)
            }
        }
    }
}
