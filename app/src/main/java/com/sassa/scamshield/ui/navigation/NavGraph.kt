package com.sassa.scamshield.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sassa.scamshield.ScamShieldViewModelFactory
import com.sassa.scamshield.ui.screens.CallsScreen
import com.sassa.scamshield.ui.screens.EducationScreen
import com.sassa.scamshield.ui.screens.HomeScreen
import com.sassa.scamshield.ui.screens.MessageAnalysisScreen
import com.sassa.scamshield.ui.screens.MessagesScreen
import com.sassa.scamshield.ui.screens.NumberLookupScreen
import com.sassa.scamshield.ui.screens.ReportScamScreen
import com.sassa.scamshield.ui.screens.ReportsScreen
import com.sassa.scamshield.ui.screens.ScanMessageScreen
import com.sassa.scamshield.ui.screens.SettingsScreen
import com.sassa.scamshield.ui.viewmodel.CallsViewModel
import com.sassa.scamshield.ui.viewmodel.HomeViewModel
import com.sassa.scamshield.ui.viewmodel.MessageAnalysisViewModel
import com.sassa.scamshield.ui.viewmodel.MessagesViewModel
import com.sassa.scamshield.ui.viewmodel.NumberLookupViewModel
import com.sassa.scamshield.ui.viewmodel.ReportScamViewModel
import com.sassa.scamshield.ui.viewmodel.ReportsViewModel
import com.sassa.scamshield.ui.viewmodel.ScanMessageViewModel
import com.sassa.scamshield.ui.viewmodel.SettingsViewModel

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Home")
    object Messages : Screen("messages", "Messages")
    object Calls : Screen("calls", "Calls")
    object Reports : Screen("reports", "Reports")
    object Settings : Screen("settings", "Settings")
    object Education : Screen("education", "Education")
    object ScanMessage : Screen("scan_message", "Scan Message")
    object NumberLookup : Screen("number_lookup", "Check Number")
    object ReportScam : Screen("report_scam", "Report Scam")

    object MessageAnalysis : Screen("message_analysis/{id}", "Analysis") {
        fun createRoute(id: String) = "message_analysis/$id"
    }
}

@Composable
fun ScamShieldNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: String = Screen.Home.route
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Home.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: HomeViewModel = viewModel(factory = factory)
            HomeScreen(
                viewModel = viewModel,
                onNavigateToScanMessage = { navController.navigate(Screen.ScanMessage.route) },
                onNavigateToNumberLookup = { navController.navigate(Screen.NumberLookup.route) },
                onNavigateToReportScam = { navController.navigate(Screen.ReportScam.route) }
            )
        }
        composable(Screen.Messages.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: MessagesViewModel = viewModel(factory = factory)
            MessagesScreen(
                viewModel = viewModel,
                onNavigateToAnalysis = { id -> navController.navigate(Screen.MessageAnalysis.createRoute(id)) }
            )
        }
        composable(Screen.Calls.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: CallsViewModel = viewModel(factory = factory)
            CallsScreen(viewModel = viewModel)
        }
        composable(Screen.Reports.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: ReportsViewModel = viewModel(factory = factory)
            ReportsScreen(
                viewModel = viewModel,
                onNavigateToReportScam = { navController.navigate(Screen.ReportScam.route) }
            )
        }
        composable(Screen.Settings.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: SettingsViewModel = viewModel(factory = factory)
            SettingsScreen(
                viewModel = viewModel,
                onNavigateToEducation = { navController.navigate(Screen.Education.route) }
            )
        }
        composable(Screen.Education.route) {
            EducationScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ScanMessage.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: ScanMessageViewModel = viewModel(factory = factory)
            ScanMessageScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.NumberLookup.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: NumberLookupViewModel = viewModel(factory = factory)
            NumberLookupScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(Screen.ReportScam.route) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: ReportScamViewModel = viewModel(factory = factory)
            ReportScamScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() },
                onReportSubmitted = { navController.popBackStack() }
            )
        }
        composable(
            route = Screen.MessageAnalysis.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { backStackEntry ->
            val factory = remember(backStackEntry) { ScamShieldViewModelFactory(context) }
            val viewModel: MessageAnalysisViewModel = viewModel(factory = factory)
            MessageAnalysisScreen(
                onNavigateBack = { navController.popBackStack() },
                viewModel = viewModel
            )
        }
    }
}
