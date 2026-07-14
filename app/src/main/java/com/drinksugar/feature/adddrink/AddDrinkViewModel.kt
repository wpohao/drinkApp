package com.drinksugar.feature.adddrink

import androidx.lifecycle.ViewModel
import com.drinksugar.core.logic.AppTime
import com.drinksugar.core.logic.SugarCalculator
import com.drinksugar.core.model.*
import com.drinksugar.core.repository.CatalogRepository
import com.drinksugar.core.repository.IntakeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class AddDrinkUiState(
    val shops: List<DrinkShop> = emptyList(),
    val items: List<DrinkItem> = emptyList(),
    val levels: List<SugarLevel> = emptyList(),
    val iceLevels: List<IceLevel> = emptyList(),
    val sizes: List<SizeOption> = emptyList(),
    val toppings: List<Topping> = emptyList(),
    val recentItemNames: List<String> = emptyList(),
    val selectedShop: DrinkShop? = null,
    val selectedItem: DrinkItem? = null,
    val selectedLevel: SugarLevel? = null,
    val selectedIce: IceLevel? = null,
    val selectedSize: SizeOption? = null,
    val toppingQty: Map<Long, Int> = emptyMap(),
    val rating: Int? = null,
    val isManual: Boolean = false,
    val customName: String = "",
    val customSugarText: String = "",
    val customKcalText: String = "",
    val previewSugar: Double = 0.0,
    val previewKcal: Double = 0.0,
)

class AddDrinkViewModel(
    private val catalog: CatalogRepository,
    private val intake: IntakeRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(AddDrinkUiState())
    val state: StateFlow<AddDrinkUiState> = _state.asStateFlow()

    suspend fun load() {
        val shops = catalog.shops()
        val levels = catalog.sugarLevels()
        val ice = catalog.iceLevels()
        val sizes = catalog.sizes()
        val toppings = catalog.toppings()
        val firstShop = shops.firstOrNull()
        val items = firstShop?.let { catalog.items(it.id) } ?: emptyList()
        _state.value = recompute(
            _state.value.copy(
                shops = shops, items = items, levels = levels, iceLevels = ice, sizes = sizes,
                toppings = toppings, recentItemNames = intake.recentItemNames(),
                selectedShop = firstShop, selectedItem = items.firstOrNull(),
                selectedLevel = levels.firstOrNull { it.code == "full" } ?: levels.lastOrNull(),
                selectedIce = ice.firstOrNull { it.code == "normal" } ?: ice.firstOrNull(),
                selectedSize = sizes.firstOrNull { it.code == "l" } ?: sizes.firstOrNull(),
            )
        )
    }

    suspend fun selectShop(shop: DrinkShop) {
        val items = catalog.items(shop.id)
        _state.value = recompute(_state.value.copy(selectedShop = shop, items = items, selectedItem = items.firstOrNull()))
    }

    fun selectItem(item: DrinkItem) { _state.value = recompute(_state.value.copy(selectedItem = item)) }
    fun selectLevel(l: SugarLevel) { _state.value = recompute(_state.value.copy(selectedLevel = l)) }
    fun selectIce(i: IceLevel) { _state.value = recompute(_state.value.copy(selectedIce = i)) }
    fun selectSize(sz: SizeOption) { _state.value = recompute(_state.value.copy(selectedSize = sz)) }
    fun setToppingQty(id: Long, qty: Int) {
        _state.value = recompute(_state.value.copy(toppingQty = _state.value.toppingQty + (id to qty)))
    }
    fun setRating(r: Int?) { _state.value = _state.value.copy(rating = r) }
    fun setManual(on: Boolean) { _state.value = recompute(_state.value.copy(isManual = on)) }
    fun setCustomName(n: String) { _state.value = _state.value.copy(customName = n) }
    fun setCustomSugar(t: String) { _state.value = recompute(_state.value.copy(customSugarText = t)) }
    fun setCustomKcal(t: String) { _state.value = recompute(_state.value.copy(customKcalText = t)) }
    fun applyRecent(name: String) { _state.value = recompute(_state.value.copy(isManual = true, customName = name)) }

    private fun recompute(s: AddDrinkUiState): AddDrinkUiState {
        if (s.isManual) {
            return s.copy(
                previewSugar = s.customSugarText.toDoubleOrNull() ?: 0.0,
                previewKcal = s.customKcalText.toDoubleOrNull() ?: 0.0,
            )
        }
        val item = s.selectedItem; val level = s.selectedLevel; val size = s.selectedSize
        if (item == null || level == null || size == null) return s.copy(previewSugar = 0.0, previewKcal = 0.0)
        val tSugar = s.toppings.mapNotNull { t -> (s.toppingQty[t.id] ?: 0).takeIf { it > 0 }?.let { t.sugarG to it } }
        val tKcal = s.toppings.mapNotNull { t -> (s.toppingQty[t.id] ?: 0).takeIf { it > 0 }?.let { (t.kcal ?: 0.0) to it } }
        return s.copy(
            previewSugar = SugarCalculator.totalSugar(item.baseSugarG, level.multiplier, size.ml, item.baseSizeMl, tSugar),
            previewKcal = SugarCalculator.totalKcal(item.baseKcal, item.baseSugarG, level.multiplier, size.ml, item.baseSizeMl, tKcal),
        )
    }

    suspend fun save(): Boolean {
        val s = recompute(_state.value)
        val log: IntakeLog = if (s.isManual) {
            if (s.customName.isBlank()) return false
            IntakeLog(
                loggedAt = AppTime.now(), itemId = null, customName = s.customName, shopName = null,
                sugarG = s.previewSugar, kcal = s.previewKcal.takeIf { it > 0 },
                sugarLevelCode = null, iceLevelCode = s.selectedIce?.code, sizeCode = null, rating = s.rating,
            )
        } else {
            val item = s.selectedItem ?: return false
            IntakeLog(
                loggedAt = AppTime.now(), itemId = item.id, customName = item.name, shopName = s.selectedShop?.name,
                sugarG = s.previewSugar, kcal = s.previewKcal,
                sugarLevelCode = s.selectedLevel?.code, iceLevelCode = s.selectedIce?.code,
                sizeCode = s.selectedSize?.code, rating = s.rating,
            )
        }
        val toppingRows = s.toppings.mapNotNull { t ->
            val q = s.toppingQty[t.id] ?: 0
            if (q > 0) IntakeLogTopping(
                intakeLogId = 0, toppingId = t.id, customName = t.name, qty = q,
                sugarG = t.sugarG * q, kcal = (t.kcal ?: 0.0) * q,
            ) else null
        }
        intake.save(log, toppingRows)
        return true
    }
}
