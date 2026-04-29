package com.codingfactory.relaunch.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.codingfactory.relaunch.ui.theme.TextWhite
import com.codingfactory.relaunch.ui.theme.brandGradient

@Composable
internal fun BottomBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth().background(brandGradient()).navigationBarsPadding()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val tabs = listOf(
                Triple(Icons.Filled.Home, Icons.Outlined.Home, 0),
                Triple(Icons.Filled.ChatBubble, Icons.Outlined.ChatBubbleOutline, 1),
                Triple(Icons.Filled.Person, Icons.Outlined.Person, 2),
            )
            tabs.forEach { (filledIcon, outlinedIcon, index) ->

                val isSelect = selectedTab == index
                val  iconToDisplay = if (isSelect) filledIcon else outlinedIcon
                IconButton(onClick = { onTabSelected(index) }) {
                    Icon(
                        imageVector = iconToDisplay,
                        contentDescription = null,
                        tint = if (isSelect) TextWhite else TextWhite.copy(0.7f),
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }
    }
}
