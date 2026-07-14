package com.drinksugar.feature.today

import androidx.lifecycle.ViewModel
import com.drinksugar.advice.AdviceInput
import com.drinksugar.advice.AdviceRule
import com.drinksugar.core.model.IntakeLog
import com.drinksugar.core.repository.IntakeRepository
import com.drinksugar.core.repository.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.time.LocalDate

data class TodayUiState(
    val todaySugar: Double = 0.0,
    val targetG: Double = 50.0,
    val remaining: Double = 50.0,
    val progress: Double = 0.0,
    val logs: List<IntakeLog> = emptyList(),
    val advice: String = "",
)

class TodayViewModel(
    private val intake: IntakeRepository,
    private val setting: SettingRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(TodayUiState())
    val state: StateFlow<TodayUiState> = _state.asStateFlow()

    suspend fun reload() {
        val today = LocalDate.now()
        val s = setting.setting()
        val total = intake.dailyTotalSugar(today)
        val target = s.dailySugarTargetG
        _state.value = TodayUiState(
            todaySugar = total,
            targetG = target,
            remaining = target - total,
            progress = if (target > 0) minOf(total / target, 1.0) else 0.0,
            logs = intake.logs(today),
            advice = AdviceRule.message(AdviceInput(total, target, 0)),
        )
    }

    suspend fun delete(log: IntakeLog) {
        intake.delete(log.id)
        reload()
    }
}
