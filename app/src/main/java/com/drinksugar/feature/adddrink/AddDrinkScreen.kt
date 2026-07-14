package com.drinksugar.feature.adddrink

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.drinksugar.core.model.*
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddDrinkScreen(viewModel: AddDrinkViewModel, onDone: () -> Unit) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { viewModel.load() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("記一杯") },
                navigationIcon = { TextButton(onClick = onDone) { Text("取消") } },
                actions = {
                    TextButton(onClick = { scope.launch { if (viewModel.save()) onDone() } }) { Text("儲存") }
                },
            )
        },
    ) { padding ->
        Column(
            Modifier.padding(padding).fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            // 歷史快選
            if (state.recentItemNames.isNotEmpty()) {
                Text("最近常喝", style = MaterialTheme.typography.labelLarge)
                Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    state.recentItemNames.forEach { name ->
                        AssistChip(onClick = { viewModel.applyRecent(name) }, label = { Text(name) })
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("手動輸入", Modifier.weight(1f))
                Switch(checked = state.isManual, onCheckedChange = { viewModel.setManual(it) })
            }

            if (state.isManual) {
                OutlinedTextField(value = state.customName, onValueChange = viewModel::setCustomName,
                    label = { Text("飲料名稱") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.customSugarText, onValueChange = viewModel::setCustomSugar,
                    label = { Text("糖分 (g)") }, keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = state.customKcalText, onValueChange = viewModel::setCustomKcal,
                    label = { Text("熱量 (kcal，可空)") }, keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    modifier = Modifier.fillMaxWidth())
            } else {
                LabeledChips("店家", state.shops.map { it.id.toString() to it.name }, state.selectedShop?.id?.toString()) { id ->
                    state.shops.firstOrNull { it.id.toString() == id }?.let { scope.launch { viewModel.selectShop(it) } }
                }
                LabeledChips("品項", state.items.map { it.id.toString() to it.name }, state.selectedItem?.id?.toString()) { id ->
                    state.items.firstOrNull { it.id.toString() == id }?.let { viewModel.selectItem(it) }
                }
                LabeledChips("糖度", state.levels.map { it.code to it.label }, state.selectedLevel?.code) { code ->
                    state.levels.firstOrNull { it.code == code }?.let { viewModel.selectLevel(it) }
                }
                LabeledChips("容量", state.sizes.map { it.code to it.label }, state.selectedSize?.code) { code ->
                    state.sizes.firstOrNull { it.code == code }?.let { viewModel.selectSize(it) }
                }
                LabeledChips("冰塊", state.iceLevels.map { it.code to it.label }, state.selectedIce?.code) { code ->
                    state.iceLevels.firstOrNull { it.code == code }?.let { viewModel.selectIce(it) }
                }
                Text("加料", style = MaterialTheme.typography.labelLarge)
                state.toppings.forEach { t ->
                    val qty = state.toppingQty[t.id] ?: 0
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("${t.name}（$qty）", Modifier.weight(1f))
                        IconButton(onClick = { if (qty > 0) viewModel.setToppingQty(t.id, qty - 1) }) { Text("－") }
                        IconButton(onClick = { if (qty < 5) viewModel.setToppingQty(t.id, qty + 1) }) { Text("＋") }
                    }
                }
            }

            // 星級評分
            Text("評分", style = MaterialTheme.typography.labelLarge)
            Row {
                (1..5).forEach { i ->
                    IconButton(onClick = { viewModel.setRating(if (state.rating == i) null else i) }) {
                        if ((state.rating ?: 0) >= i) Icon(Icons.Filled.Star, contentDescription = "$i 星")
                        else Icon(Icons.Outlined.StarBorder, contentDescription = "$i 星")
                    }
                }
            }

            HorizontalDivider()
            Text("預估糖分：${state.previewSugar.roundToInt()} g", style = MaterialTheme.typography.titleMedium)
            Text("預估熱量：${state.previewKcal.roundToInt()} kcal")
            Text("數值為參考估算", style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun LabeledChips(label: String, options: List<Pair<String, String>>, selected: String?, onSelect: (String) -> Unit) {
    Text(label, style = MaterialTheme.typography.labelLarge)
    Row(Modifier.horizontalScroll(rememberScrollState()), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        options.forEach { (value, text) ->
            FilterChip(selected = selected == value, onClick = { onSelect(value) }, label = { Text(text) })
        }
    }
}
