package com.drinksugar.feature.history

import androidx.compose.runtime.Composable
import com.drinksugar.ui.PieChart

@Composable
fun PieSection(state: HistoryUiState) {
    PieChart(state.distribution)
}
