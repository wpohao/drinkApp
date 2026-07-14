package com.drinksugar.feature.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var showTimePicker by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { viewModel.load() }

    fun persist() { scope.launch { viewModel.save() } }

    Scaffold(topBar = { TopAppBar(title = { Text("設定") }) }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)) {

            Text("每日糖分目標", style = MaterialTheme.typography.titleMedium)
            SingleChoiceSegmentedButtonRow(Modifier.fillMaxWidth()) {
                SegmentedButton(selected = state.targetG == 25.0,
                    onClick = { viewModel.setTarget(25.0); persist() },
                    shape = SegmentedButtonDefaults.itemShape(0, 2)) { Text("25 g（嚴格）") }
                SegmentedButton(selected = state.targetG == 50.0,
                    onClick = { viewModel.setTarget(50.0); persist() },
                    shape = SegmentedButtonDefaults.itemShape(1, 2)) { Text("50 g（一般）") }
            }
            Text("自訂：${state.targetG.roundToInt()} g")
            Slider(value = state.targetG.toFloat(), valueRange = 10f..150f, steps = 27,
                onValueChange = { viewModel.setTarget(it.toDouble()) },
                onValueChangeFinished = { persist() })

            HorizontalDivider()
            Text("通知", style = MaterialTheme.typography.titleMedium)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("接近／超標提醒", Modifier.weight(1f))
                Switch(checked = state.notifyThreshold,
                    onCheckedChange = { viewModel.setNotifyThreshold(it); persist() })
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("每日補記提醒", Modifier.weight(1f))
                Switch(checked = state.notifyDailyLog,
                    onCheckedChange = { viewModel.setNotifyDailyLog(it); persist() })
            }
            if (state.notifyDailyLog) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("提醒時間", Modifier.weight(1f))
                    TextButton(onClick = { showTimePicker = true }) { Text(state.dailyLogTime) }
                }
            }

            HorizontalDivider()
            Text("關於", style = MaterialTheme.typography.titleMedium)
            Text("版本 1.0")
            Text("本 App 的糖分／熱量為參考估算，非醫療建議。",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }

    if (showTimePicker) {
        val parts = state.dailyLogTime.split(":")
        val tpState = rememberTimePickerState(
            initialHour = parts.getOrNull(0)?.toIntOrNull() ?: 21,
            initialMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0,
            is24Hour = true,
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.setDailyLogTime("%02d:%02d".format(tpState.hour, tpState.minute))
                    persist(); showTimePicker = false
                }) { Text("確定") }
            },
            dismissButton = { TextButton(onClick = { showTimePicker = false }) { Text("取消") } },
            text = { TimePicker(state = tpState) },
        )
    }
}
