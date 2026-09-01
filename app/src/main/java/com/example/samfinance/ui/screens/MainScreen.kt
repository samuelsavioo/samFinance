package com.example.samfinance.ui.screens

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.samfinance.ui.theme.*

sealed class BottomTab(val route: String, val title: String, val icon: ImageVector) {
    object Dashboard : BottomTab("dashboard", "Dashboard", Icons.Default.Home)
    object Transactions : BottomTab("transactions", "Transações", Icons.Default.List)
    object Chat : BottomTab("chat", "Chat Sam", Icons.Default.Send)
    object Profile : BottomTab("profile", "Perfil", Icons.Default.AccountCircle)
}

@Composable
fun MainScreen(
    onLogout: () -> Unit
) {
    var selectedTab by remember { mutableStateOf<BottomTab>(BottomTab.Dashboard) }

    Scaffold(
        containerColor = BackgroundDeepBlack,
        bottomBar = {
            NavigationBar(
                containerColor = GlassCard,
                contentColor = TextPrimaryLight
            ) {
                val tabs = listOf(
                    BottomTab.Dashboard,
                    BottomTab.Transactions,
                    BottomTab.Chat,
                    BottomTab.Profile
                )
                tabs.forEach { tab ->
                    val isSelected = selectedTab == tab
                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { selectedTab = tab },
                        icon = { Icon(tab.icon, contentDescription = tab.title) },
                        label = { Text(tab.title) },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = BackgroundDeepBlack,
                            selectedTextColor = ElectricBluePrimary,
                            indicatorColor = ElectricBluePrimary,
                            unselectedIconColor = TextSecondaryMuted,
                            unselectedTextColor = TextSecondaryMuted
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Surface(
            modifier = Modifier.padding(paddingValues),
            color = BackgroundDeepBlack
        ) {
            when (selectedTab) {
                BottomTab.Dashboard -> DashboardScreen(
                    onNavigateToChat = { selectedTab = BottomTab.Chat },
                    onNavigateToProfile = { selectedTab = BottomTab.Profile }
                )
                BottomTab.Transactions -> TransactionsScreen()
                BottomTab.Chat -> ChatScreen()
                BottomTab.Profile -> ProfileScreen(
                    onSaveSuccess = { selectedTab = BottomTab.Dashboard },
                    onLogout = onLogout
                )
            }
        }
    }
}
