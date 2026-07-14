package com.drinksugar.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(viewModel: HistoryViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var tab by remember { mutableIntStateOf(0) }
    LaunchedEffect(Unit) { viewModel.reload() }

    Scaffold(topBar = { TopAppBar(title = { Text("歷史") }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState())) {
            TabRow(selectedTabIndex = tab) {
                listOf("趨勢", "日曆", "圓餅", "戰績").forEachIndexed { i, t ->
                    Tab(selected = tab == i, onClick = { tab = i }, text = { Text(t) })
                }
            }
            when (tab) {
                0 -> TrendSection(state) { r -> scope.launch { viewModel.setRange(r) } }
                1 -> CalendarSection(state,
                    onPrev = { scope.launch { viewModel.prevMonth() } },
                    onNext = { scope.launch { viewModel.nextMonth() } })
                2 -> PieSection(state)
                else -> ShareSection(state)
            }
        }
    }
}
