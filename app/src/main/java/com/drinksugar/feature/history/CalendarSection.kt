package com.drinksugar.feature.history

import androidx.compose.runtime.Composable
import com.drinksugar.ui.MonthCalendar

@Composable
fun CalendarSection(state: HistoryUiState, onPrev: () -> Unit, onNext: () -> Unit) {
    MonthCalendar(
        month = state.month,
        dayValues = state.monthDays,
        targetG = state.targetG,
        onPrev = onPrev,
        onNext = onNext,
    )
}
