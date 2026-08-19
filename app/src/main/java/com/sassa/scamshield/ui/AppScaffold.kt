package com.sassa.scamshield.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sassa.scamshield.ui.navigation.ScamShieldNavHost
import com.sassa.scamshield.ui.navigation.Screen
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import com.sassa.scamshield.ui.theme.Purple500
import com.sassa.scamshield.ui.theme.CardBackground
import com.sassa.scamshield.ui.theme.TextSecondary
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class BottomNavItem(
    val screen: Screen,
    val icon: ImageVector,
    val label: String
)

val bottomNavItems = listOf(
    BottomNavItem(Screen.Home, Icons.Outlined.Home, "Home"),
    BottomNavItem(Screen.Messages, Icons.Outlined.Email, "Messages"),
    BottomNavItem(Screen.Calls, Icons.Outlined.Call, "Calls"),
    BottomNavItem(Screen.Reports, Icons.Outlined.Description, "Reports"),
    BottomNavItem(Screen.Settings, Icons.Outlined.Settings, "Settings"),
)

val topLevelRoutes = setOf(
    Screen.Home.route,
    Screen.Messages.route,
    Screen.Calls.route,
    Screen.Reports.route,
    Screen.Settings.route
)

@Composable
fun AppScaffold(onNavigateBack: (() -> Unit)? = null) {
    val navController = rememberNavController()
    val currentBackStack = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStack.value?.destination?.route
    val isTopLevel = currentRoute in topLevelRoutes

    Scaffold(
        bottomBar = {
            if (isTopLevel) {
                ScamShieldBottomBar(
                    navController = navController,
                    items = bottomNavItems,
                    currentRoute = currentRoute
                )
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            ScamShieldNavHost(navController = navController)
        }
    }
}

@Composable
fun ScamShieldBottomBar(
    navController: NavHostController,
    items: List<BottomNavItem>,
    currentRoute: String?
) {
    val visibleRoutes = setOf(
        Screen.Home.route,
        Screen.Messages.route,
        Screen.Calls.route,
        Screen.Reports.route,
        Screen.Settings.route
    )

    if (currentRoute !in visibleRoutes) return

    NavigationBar(
        containerColor = CardBackground,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val isSelected = currentRoute?.startsWith(item.screen.route) == true

            NavigationBarItem(
                selected = isSelected,
                onClick = {
                    if (currentRoute != item.screen.route) {
                        navController.navigate(item.screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = item.icon,
                        contentDescription = item.label,
                        tint = if (isSelected) Purple500 else TextSecondary,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (isSelected) Color(0xFF8B5CF6) else TextSecondary,
                        style = androidx.compose.material3.MaterialTheme.typography.labelSmall
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color(0x338B5CF6)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}
