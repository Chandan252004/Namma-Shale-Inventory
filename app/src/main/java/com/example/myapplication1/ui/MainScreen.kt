package com.example.myapplication1.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.myapplication1.ui.theme.HeaderBlue

sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Login : Screen("login", "Login", Icons.Default.Lock)
    object Home : Screen("home", "Home", Icons.Default.Home)
    object Assets : Screen("assets", "Assets", Icons.Default.Inventory)
    object Health : Screen("health", "Health", Icons.Default.HealthAndSafety)
    object Issues : Screen("issues", "Issues", Icons.Default.ReportProblem)
    object Repair : Screen("repair", "Repair", Icons.Default.Build)
}

@Composable
fun MainScreen(viewModel: InventoryViewModel) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    
    val showBottomBar = currentRoute != Screen.Login.route

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                BottomNavigation(navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Login.route,
            modifier = Modifier.padding(if (showBottomBar) innerPadding else PaddingValues(0.dp))
        ) {
            composable(Screen.Login.route) { 
                LoginScreen(viewModel = viewModel, onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                })
            }
            composable(Screen.Home.route) { DashboardScreen(viewModel) }
            composable(Screen.Assets.route) { AssetRegisterScreen(viewModel) }
            composable(Screen.Health.route) { HealthCheckScreen(viewModel) }
            composable(Screen.Issues.route) { IssueLogScreen(viewModel) }
            composable(Screen.Repair.route) { RepairRequestsScreen(viewModel) }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavHostController) {
    val items = listOf(
        Screen.Home,
        Screen.Assets,
        Screen.Health,
        Screen.Issues,
        Screen.Repair
    )
    
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        items.forEach { screen ->
            val isSelected = currentRoute == screen.route
            NavigationBarItem(
                icon = { 
                    Icon(
                        screen.icon, 
                        contentDescription = screen.title,
                        tint = if (isSelected) HeaderBlue else Color.Gray
                    ) 
                },
                label = { 
                    Text(
                        screen.title,
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        color = if (isSelected) HeaderBlue else Color.Gray
                    ) 
                },
                selected = isSelected,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = HeaderBlue.copy(alpha = 0.1f)
                )
            )
        }
    }
}
