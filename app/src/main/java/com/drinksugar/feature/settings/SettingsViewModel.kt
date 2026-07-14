package com.drinksugar.feature.settings

import androidx.lifecycle.ViewModel
import com.drinksugar.core.model.UserSetting
import com.drinksugar.core.repository.SettingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class SettingsUiState(
    val targetG: Double = 50.0,
    val notifyThreshold: Boolean = true,
    val notifyDailyLog: Boolean = false,
    val dailyLogTime: String = "21:00",
)

class SettingsViewModel(private val setting: SettingRepository) : ViewModel() {
    private val _state = MutableStateFlow(SettingsUiState())
    val state: StateFlow<SettingsUiState> = _state.asStateFlow()

    suspend fun load() {
        val s = setting.setting()
        _state.value = SettingsUiState(
            targetG = s.dailySugarTargetG,
            notifyThreshold = s.notifyThreshold,
            notifyDailyLog = s.notifyDailyLog,
            dailyLogTime = s.notifyDailyLogTime ?: "21:00",
        )
    }

    fun setTarget(v: Double) { _state.value = _state.value.copy(targetG = v) }
    fun setNotifyThreshold(v: Boolean) { _state.value = _state.value.copy(notifyThreshold = v) }
    fun setNotifyDailyLog(v: Boolean) { _state.value = _state.value.copy(notifyDailyLog = v) }
    fun setDailyLogTime(hhmm: String) { _state.value = _state.value.copy(dailyLogTime = hhmm) }

    suspend fun save() {
        val s = _state.value
        setting.update(
            UserSetting(
                id = 1, dailySugarTargetG = s.targetG, notifyThreshold = s.notifyThreshold,
                notifyDailyLog = s.notifyDailyLog, notifyDailyLogTime = s.dailyLogTime,
            )
        )
    }
}
