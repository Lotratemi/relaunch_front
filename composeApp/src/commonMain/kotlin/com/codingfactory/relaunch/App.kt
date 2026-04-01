package com.codingfactory.relaunch

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.tooling.preview.Preview
import com.codingfactory.relaunch.ui.coach.CoachChatScreen
import com.codingfactory.relaunch.ui.dashboard.DashboardScreen
import com.codingfactory.relaunch.ui.onboarding.OnboardingProfileScreen

private enum class AppScreen { ONBOARDING, COACH, DASHBOARD }

@Composable
@Preview
fun App() {
    MaterialTheme {
        var screen by remember { mutableStateOf(AppScreen.ONBOARDING) }
        var userName by remember { mutableStateOf("") }

        when (screen) {
            AppScreen.ONBOARDING -> OnboardingProfileScreen(
                onValidate = { name, _, _ ->
                    userName = name
                    screen = AppScreen.COACH
                }
            )
            AppScreen.COACH -> CoachChatScreen(
                userName = userName,
                onComplete = { screen = AppScreen.DASHBOARD }
            )
            AppScreen.DASHBOARD -> DashboardScreen(userName = userName)
        }
    }
}
