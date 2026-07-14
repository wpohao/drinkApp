package com.drinksugar.feature.history

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.drinksugar.ui.TrendBarChart

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrendSection(state: HistoryUiState, onRange: (HistoryRange) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
            SegmentedButton(
                selected = state.range == HistoryRange.WEEK,
                onClick = { onRange(HistoryRange.WEEK) },
                shape = SegmentedButtonDefaults.itemShape(0, 2),
            ) { Text("近 7 天") }
            SegmentedButton(
                selected = state.range == HistoryRange.MONTH,
                onClick = { onRange(HistoryRange.MONTH) },
                shape = SegmentedButtonDefaults.itemShape(1, 2),
            ) { Text("近 30 天") }
        }
        TrendBarChart(state.points, state.targetG)
        Text("超標天數：${state.overTargetDays} 天", style = MaterialTheme.typography.titleMedium)
        Text("數值為參考估算，非醫療建議", style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
