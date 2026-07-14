package com.drinksugar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.drinksugar.feature.history.CalendarStatus
import com.drinksugar.feature.history.CalendarStatusRule
import java.time.LocalDate
import java.time.YearMonth

@Composable
fun MonthCalendar(
    month: YearMonth,
    dayValues: Map<LocalDate, Double>,
    targetG: Double,
    onPrev: () -> Unit,
    onNext: () -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onPrev) { Icon(Icons.Filled.ChevronLeft, contentDescription = "上個月") }
            Text("${month.year} 年 ${month.monthValue} 月", Modifier.weight(1f),
                textAlign = TextAlign.Center, style = MaterialTheme.typography.titleMedium)
            IconButton(onClick = onNext) { Icon(Icons.Filled.ChevronRight, contentDescription = "下個月") }
        }
        Row {
            listOf("日", "一", "二", "三", "四", "五", "六").forEach {
                Text(it, Modifier.weight(1f), textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall)
            }
        }
        val firstDow = month.atDay(1).dayOfWeek.value % 7  // 週日=0
        val cells: List<LocalDate?> = List(firstDow) { null } + (1..month.lengthOfMonth()).map { month.atDay(it) }
        cells.chunked(7).forEach { week ->
            Row {
                for (i in 0 until 7) {
                    val day = week.getOrNull(i)
                    Box(Modifier.weight(1f).aspectRatio(1f).padding(2.dp), contentAlignment = Alignment.Center) {
                        if (day != null) {
                            val status = CalendarStatusRule.status(dayValues[day], targetG)
                            val bg = when (status) {
                                CalendarStatus.OVER -> Color(0xFFD32F2F)
                                CalendarStatus.UNDER -> Color(0xFF4CAF50)
                                CalendarStatus.NONE -> Color(0xFFE0E0E0)
                            }
                            val fg = if (status == CalendarStatus.NONE) MaterialTheme.colorScheme.onSurface else Color.White
                            Box(Modifier.fillMaxSize().clip(CircleShape).background(bg),
                                contentAlignment = Alignment.Center) {
                                Text("${day.dayOfMonth}", color = fg, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.padding(top = 8.dp)) {
            LegendDot(Color(0xFF4CAF50), "達標")
            LegendDot(Color(0xFFD32F2F), "超標")
            LegendDot(Color(0xFFE0E0E0), "無紀錄")
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(Modifier.size(12.dp).clip(CircleShape).background(color))
        Text(label, style = MaterialTheme.typography.labelSmall)
    }
}
