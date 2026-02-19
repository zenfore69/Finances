package com.example.finances.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.finances.data.AppState
import com.example.finances.ui.screens.CameraScreen
import com.example.finances.ui.screens.ExpenseDetailScreen
import com.example.finances.ui.screens.ExpenseListScreen
import com.example.finances.ui.screens.LoginScreen
import com.example.finances.ui.screens.MainScreen
import com.example.finances.ui.screens.NewExpenseScreen
import com.example.finances.ui.screens.SettingsScreen
import com.example.finances.ui.screens.StatisticsScreen
import com.example.finances.ui.screens.UserManagementScreen
import com.example.finances.ui.screens.ApprovalsScreen

object Routes {
    const val LOGIN = "login"
    const val MAIN = "main"
    const val EXPENSE_LIST = "expense_list"
    const val EXPENSE_DETAIL = "expense_detail/{expenseId}"
    const val NEW_EXPENSE = "new_expense"
    const val CAMERA = "camera"
    const val SETTINGS = "settings"
    const val USERS = "users"
    const val STATISTICS = "statistics"
    const val APPROVALS = "approvals"

    fun expenseDetail(expenseId: Long) = "expense_detail/$expenseId"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onLoginSuccess = { navController.navigate(Routes.MAIN) { popUpTo(Routes.LOGIN) { inclusive = true } } }
            )
        }
        composable(Routes.MAIN) {
            MainScreen(
                onNewExpense = { navController.navigate(Routes.NEW_EXPENSE) },
                onReportHistory = { navController.navigate(Routes.EXPENSE_LIST) },
                onStatistics = { navController.navigate(Routes.STATISTICS) },
                onSettings = { navController.navigate(Routes.SETTINGS) },
                onApprovals = { navController.navigate(Routes.APPROVALS) }
            )
        }
        composable(Routes.EXPENSE_LIST) {
            ExpenseListScreen(
                onBack = { navController.popBackStack() },
                onExpenseClick = { id -> navController.navigate(Routes.expenseDetail(id)) }
            )
        }
        composable(
            route = Routes.EXPENSE_DETAIL,
            arguments = listOf(navArgument("expenseId") { type = NavType.LongType })
        ) { backStackEntry ->
            val expenseId = backStackEntry.arguments?.getLong("expenseId") ?: 0L
            val appState = AppState.getInstance()
            val expense = appState.getExpenses().find { it.getId() == expenseId }
            ExpenseDetailScreen(
                expense = expense,
                onBack = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() }
            )
        }
        composable(Routes.SETTINGS) {
            SettingsScreen(
                onBack = { navController.popBackStack() },
                onLogout = { navController.navigate(Routes.LOGIN) { popUpTo(0) { inclusive = true } } },
                onOpenUserManagement = { navController.navigate(Routes.USERS) }
            )
        }
        composable(Routes.USERS) {
            UserManagementScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.STATISTICS) {
            StatisticsScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.NEW_EXPENSE) {
            NewExpenseScreen(
                onBack = { navController.popBackStack() },
                onOpenCamera = { navController.navigate(Routes.CAMERA) },
                onSentTo1C = { navController.popBackStack() }
            )
        }
        composable(Routes.CAMERA) {
            CameraScreen(onPhotoTaken = { navController.popBackStack() })
        }
        composable(Routes.APPROVALS) {
            ApprovalsScreen(onBack = { navController.popBackStack() })
        }
    }
}
