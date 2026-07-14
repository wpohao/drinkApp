package com.drinksugar.support

import com.drinksugar.core.model.*
import com.drinksugar.core.repository.*
import java.time.LocalDate

class FakeCatalogRepository(
    var shopList: List<DrinkShop> = listOf(DrinkShop(1, "示範飲料店", 0)),
    var itemsByShop: Map<Long, List<DrinkItem>> = mapOf(
        1L to listOf(
            DrinkItem(1, 1, "珍珠奶茶", "奶茶", 62.0, 480.0, 700, true),
            DrinkItem(2, 1, "紅茶", "純茶", 45.0, 190.0, 700, true),
        )
    ),
    var levelList: List<SugarLevel> = listOf(
        SugarLevel("none", "無糖", 0.0, 0), SugarLevel("half", "半糖", 0.5, 3), SugarLevel("full", "全糖", 1.0, 6),
    ),
    var iceList: List<IceLevel> = listOf(IceLevel("normal", "正常冰", 0), IceLevel("free", "去冰", 3)),
    var sizeList: List<SizeOption> = listOf(SizeOption("m", "中杯", 500), SizeOption("l", "大杯", 700)),
    var toppingList: List<Topping> = listOf(Topping(1, "珍珠", 15.0, 100.0, true, 0)),
) : CatalogRepository {
    override suspend fun shops() = shopList
    override suspend fun items(shopId: Long) = itemsByShop[shopId] ?: emptyList()
    override suspend fun sugarLevels() = levelList
    override suspend fun iceLevels() = iceList
    override suspend fun sizes() = sizeList
    override suspend fun toppings() = toppingList
}

class FakeIntakeRepository : IntakeRepository {
    val saved = mutableListOf<IntakeLog>()
    var todayLogs = mutableListOf<IntakeLog>()
    var totalSugar = 0.0
    var daily: List<DailyTotal> = emptyList()
    var distribution: List<ShopCount> = emptyList()
    var recentShopList: List<String> = emptyList()
    var recentItems: List<String> = emptyList()

    override suspend fun save(log: IntakeLog, toppings: List<IntakeLogTopping>): Long {
        val id = (saved.size + 1).toLong()
        saved.add(log.copy(id = id)); todayLogs.add(log.copy(id = id)); totalSugar += log.sugarG
        return id
    }
    override suspend fun logs(on: LocalDate) = todayLogs.toList()
    override suspend fun dailyTotalSugar(on: LocalDate) = totalSugar
    override suspend fun delete(logId: Long) { todayLogs.removeAll { it.id == logId } }
    override suspend fun dailyTotals(from: LocalDate, to: LocalDate) = daily
    override suspend fun shopDistribution(from: LocalDate, to: LocalDate) = distribution
    override suspend fun recentShops(limit: Int) = recentShopList
    override suspend fun recentItemNames(limit: Int) = recentItems
}

class FakeSettingRepository(var current: UserSetting = UserSetting()) : SettingRepository {
    override suspend fun setting() = current
    override suspend fun update(s: UserSetting) { current = s.copy(id = 1) }
}
