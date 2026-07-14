package com.drinksugar.feature.history

import androidx.lifecycle.ViewModel
import com.drinksugar.core.repository.DailyTotal
import com.drinksugar.core.repository.IntakeRepository
import com.drinksugar.core.repository.SettingRepository
import com.drinksugar.core.repository.ShopCount
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate
import java.time.YearMonth

enum class HistoryRange { WEEK, MONTH }

data class HistoryUiState(
    val range: HistoryRange = HistoryRange.WEEK,
    val points: List<DailyTotal> = emptyList(),
    val overTargetDays: Int = 0,
    val targetG: Double = 50.0,
    val distribution: List<ShopCount> = emptyList(),
    val month: YearMonth = YearMonth.now(),
    val monthDays: Map<LocalDate, Double> = emptyMap(),
)

class HistoryViewModel(
    private val intake: IntakeRepository,
    private val setting: SettingRepository,
) : ViewModel() {
    private var range = HistoryRange.WEEK
    private var month: YearMonth = YearMonth.now()
    private val _state = MutableStateFlow(HistoryUiState())
    val state: StateFlow<HistoryUiState> = _state.asStateFlow()

    suspend fun setRange(r: HistoryRange) { range = r; reload() }
    suspend fun prevMonth() { month = month.minusMonths(1); reload() }
    suspend fun nextMonth() { month = month.plusMonths(1); reload() }

    suspend fun reload() {
        val today = LocalDate.now()
        val target = setting.setting().dailySugarTargetG
        val span = if (range == HistoryRange.WEEK) 6L else 29L
        val from = today.minusDays(span)
        val points = intake.dailyTotals(from, today)
        val distribution = intake.shopDistribution(from, today)
        val monthDays = intake.dailyTotals(month.atDay(1), month.atEndOfMonth())
            .associate { it.day to it.sugarG }
        _state.value = HistoryUiState(
            range = range, points = points, overTargetDays = points.count { it.sugarG > target },
            targetG = target, distribution = distribution, month = month, monthDays = monthDays,
        )
    }
}
