package com.drinksugar.feature.today

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drinksugar.core.model.IntakeLog
import com.drinksugar.ui.RingProgress
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayScreen(viewModel: TodayViewModel, onAddTapped: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    var pendingDelete by remember { mutableStateOf<IntakeLog?>(null) }

    LaunchedEffect(Unit) { viewModel.reload() }

    Scaffold(
        topBar = { TopAppBar(title = { Text("今日") }) },
        bottomBar = {
            Button(onClick = onAddTapped, modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Text("＋ 記一杯", style = MaterialTheme.typography.titleMedium)
            }
        },
    ) { padding ->
        Column(
            modifier = Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            RingProgress(
                progress = state.progress,
                centerText = "${state.todaySugar.roundToInt()} g",
                subText = "／ 目標 ${state.targetG.roundToInt()} g",
            )
            Spacer(Modifier.height(16.dp))
            Text(
                if (state.remaining >= 0) "今天還可以攝取 ${state.remaining.roundToInt()} g 糖"
                else "今天已超標 ${(-state.remaining).roundToInt()} g 糖",
                style = MaterialTheme.typography.titleMedium,
                color = if (state.remaining >= 0) MaterialTheme.colorScheme.onSurface else Color(0xFFD32F2F),
            )
            Spacer(Modifier.height(8.dp))
            Text(state.advice, style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant, textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp))
            Spacer(Modifier.height(16.dp))

            if (state.logs.isEmpty()) {
                Text("今天還沒有記錄，點下方按鈕記一杯吧！",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 32.dp))
            } else {
                state.logs.forEach { log ->
                    Card(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp)) {
                        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Column(Modifier.weight(1f)) {
                                Text(log.customName ?: "飲料", style = MaterialTheme.typography.bodyLarge)
                                Text("${log.sugarG.roundToInt()} g 糖", style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                            IconButton(onClick = { pendingDelete = log }) {
                                Icon(Icons.Filled.Delete, contentDescription = "刪除")
                            }
                        }
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Text("數值為參考估算，非醫療建議",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(24.dp))
        }
    }

    // 刪除二次確認
    pendingDelete?.let { log ->
        AlertDialog(
            onDismissRequest = { pendingDelete = null },
            title = { Text("刪除這筆記錄？") },
            text = { Text("「${log.customName ?: "飲料"}」將被刪除，此動作無法復原。") },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { viewModel.delete(log) }
                    pendingDelete = null
                }) { Text("刪除") }
            },
            dismissButton = { TextButton(onClick = { pendingDelete = null }) { Text("取消") } },
        )
    }
}
