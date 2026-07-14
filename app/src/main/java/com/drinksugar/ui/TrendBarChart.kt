package com.drinksugar.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.unit.dp
import com.drinksugar.core.repository.DailyTotal
import kotlin.math.max

@Composable
fun TrendBarChart(points: List<DailyTotal>, targetG: Double) {
    val barColor = Color(0xFF4CAF50)
    val overColor = Color(0xFFD32F2F)
    val targetColor = Color(0xFFD32F2F)
    Canvas(modifier = Modifier.fillMaxWidth().height(220.dp).padding(8.dp)) {
        if (points.isEmpty()) return@Canvas
        val maxV = max(points.maxOf { it.sugarG }, targetG).coerceAtLeast(1.0)
        val slot = size.width / points.size
        val barW = slot * 0.6f
        points.forEachIndexed { i, p ->
            val h = (p.sugarG / maxV * size.height).toFloat()
            val x = i * slot + (slot - barW) / 2
            drawRect(
                color = if (p.sugarG > targetG) overColor else barColor,
                topLeft = androidx.compose.ui.geometry.Offset(x, size.height - h),
                size = androidx.compose.ui.geometry.Size(barW, h),
            )
        }
        // 目標虛線
        val ty = (size.height - targetG / maxV * size.height).toFloat()
        drawLine(
            color = targetColor,
            start = androidx.compose.ui.geometry.Offset(0f, ty),
            end = androidx.compose.ui.geometry.Offset(size.width, ty),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f)),
        )
    }
}
