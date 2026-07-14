package com.drinksugar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.drinksugar.core.repository.ShopCount
import com.drinksugar.feature.history.PieMath

private val PIE_PALETTE = listOf(
    Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFFF9800), Color(0xFF9C27B0),
    Color(0xFFF44336), Color(0xFF00BCD4), Color(0xFF795548), Color(0xFF607D8B),
)

@Composable
fun PieChart(slices: List<ShopCount>) {
    if (slices.isEmpty()) {
        Text("這段期間還沒有紀錄", Modifier.padding(24.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        return
    }
    val sweeps = PieMath.sweeps(slices.map { it.count })
    Column(Modifier.fillMaxWidth().padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Canvas(modifier = Modifier.size(200.dp)) {
            var start = -90f
            sweeps.forEachIndexed { i, sweep ->
                drawArc(
                    color = PIE_PALETTE[i % PIE_PALETTE.size],
                    startAngle = start, sweepAngle = sweep, useCenter = true,
                )
                start += sweep
            }
        }
        Spacer(Modifier.height(16.dp))
        slices.forEachIndexed { i, s ->
            Row(Modifier.fillMaxWidth().padding(vertical = 2.dp), verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(14.dp).clip(CircleShape).background(PIE_PALETTE[i % PIE_PALETTE.size]))
                Spacer(Modifier.width(8.dp))
                Text(s.shop, Modifier.weight(1f))
                Text("${s.count} 杯", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}
