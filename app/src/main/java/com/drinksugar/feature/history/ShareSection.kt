package com.drinksugar.feature.history

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun ShareSection(state: HistoryUiState) {
    val context = LocalContext.current
    val stat = remember(state.month, state.monthDays, state.distribution, state.targetG) {
        MonthlySummary.of(state.month, state.monthDays, state.distribution, state.targetG)
    }
    val bitmap = remember(stat) { ShareCardRenderer.render(stat) }
    Column(
        Modifier.fillMaxWidth().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Image(bitmap.asImageBitmap(), contentDescription = "月度戰績卡",
            modifier = Modifier.fillMaxWidth(0.8f))
        Button(onClick = { ShareUtil.shareBitmap(context, bitmap) }) { Text("分享到限動") }
    }
}
