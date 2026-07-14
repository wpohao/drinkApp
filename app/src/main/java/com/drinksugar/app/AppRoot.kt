package com.drinksugar.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.LocalCafe
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.drinksugar.ads.AdBanner
import com.drinksugar.feature.adddrink.AddDrinkScreen
import com.drinksugar.feature.adddrink.AddDrinkViewModel
import com.drinksugar.feature.history.HistoryScreen
import com.drinksugar.feature.history.HistoryViewModel
import com.drinksugar.feature.settings.SettingsScreen
import com.drinksugar.feature.settings.SettingsViewModel
import com.drinksugar.feature.today.TodayScreen
import com.drinksugar.feature.today.TodayViewModel
import com.drinksugar.notification.NotificationHelper
import com.drinksugar.notification.ReminderScheduler
import kotlinx.coroutines.launch

@Composable
fun AppRoot(container: AppContainer) {
    val nav = rememberNavController()
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val backStack by nav.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    // 依設定排程每日補記提醒
    LaunchedEffect(Unit) {
        val s = container.setting.setting()
        val parts = (s.notifyDailyLogTime ?: "21:00").split(":")
        ReminderScheduler.schedule(
            context, s.notifyDailyLog,
            parts.getOrNull(0)?.toIntOrNull() ?: 21,
            parts.getOrNull(1)?.toIntOrNull() ?: 0,
        )
    }

    // 今日頁 VM 提升到 AppRoot，方便記一杯回來後 reload ＋ 觸發門檻通知
    val todayVm: TodayViewModel = viewModel(
        factory = viewModelFactory { initializer { TodayViewModel(container.intake, container.setting) } })

    Column(Modifier.fillMaxSize()) {
        NavHost(nav, startDestination = "today", modifier = Modifier.weight(1f)) {
            composable("today") { TodayScreen(todayVm) { nav.navigate("add") } }
            composable("history") {
                val vm: HistoryViewModel = viewModel(
                    factory = viewModelFactory { initializer { HistoryViewModel(container.intake, container.setting) } })
                HistoryScreen(vm)
            }
            composable("settings") {
                val vm: SettingsViewModel = viewModel(
                    factory = viewModelFactory { initializer { SettingsViewModel(container.setting) } })
                SettingsScreen(vm)
            }
            composable("add") {
                val vm: AddDrinkViewModel = viewModel(
                    factory = viewModelFactory { initializer { AddDrinkViewModel(container.catalog, container.intake) } })
                AddDrinkScreen(vm) {
                    nav.popBackStack()
                    scope.launch {
                        todayVm.reload()
                        val s = container.setting.setting()
                        NotificationHelper.maybeWarnThreshold(
                            context, todayVm.state.value.todaySugar, s.dailySugarTargetG, s.notifyThreshold)
                    }
                }
            }
        }
        if (route != "add") {
            AdBanner()
            NavigationBar {
                NavItem(nav, route, "today", "今日", Icons.Filled.LocalCafe)
                NavItem(nav, route, "history", "歷史", Icons.Filled.BarChart)
                NavItem(nav, route, "settings", "設定", Icons.Filled.Settings)
            }
        }
    }
}

@Composable
private fun RowScope.NavItem(
    nav: NavHostController, current: String?, route: String, label: String, icon: ImageVector,
) {
    NavigationBarItem(
        selected = current == route,
        onClick = {
            if (current != route) nav.navigate(route) {
                popUpTo("today"); launchSingleTop = true
            }
        },
        icon = { Icon(icon, contentDescription = label) },
        label = { Text(label) },
    )
}
