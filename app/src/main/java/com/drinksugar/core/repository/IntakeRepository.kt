package com.drinksugar.core.repository

import com.drinksugar.core.database.IntakeDao
import com.drinksugar.core.logic.AppTime
import com.drinksugar.core.model.IntakeLog
import com.drinksugar.core.model.IntakeLogTopping
import java.time.LocalDate

data class DailyTotal(val day: LocalDate, val sugarG: Double, val kcal: Double)
data class ShopCount(val shop: String, val count: Int)

interface IntakeRepository {
    suspend fun save(log: IntakeLog, toppings: List<IntakeLogTopping>): Long
    suspend fun logs(on: LocalDate): List<IntakeLog>
    suspend fun dailyTotalSugar(on: LocalDate): Double
    suspend fun delete(logId: Long)
    suspend fun dailyTotals(from: LocalDate, to: LocalDate): List<DailyTotal>
    suspend fun shopDistribution(from: LocalDate, to: LocalDate): List<ShopCount>
    suspend fun recentShops(limit: Int = 8): List<String>
    suspend fun recentItemNames(limit: Int = 8): List<String>
}

class RoomIntakeRepository(private val dao: IntakeDao) : IntakeRepository {
    override suspend fun save(log: IntakeLog, toppings: List<IntakeLogTopping>): Long {
        val id = dao.insertLog(log)
        if (toppings.isNotEmpty()) dao.insertToppings(toppings.map { it.copy(intakeLogId = id) })
        return id
    }

    override suspend fun logs(on: LocalDate) =
        dao.logsBetween(AppTime.dayStart(on), AppTime.dayEnd(on))

    override suspend fun dailyTotalSugar(on: LocalDate) =
        dao.totalSugarBetween(AppTime.dayStart(on), AppTime.dayEnd(on))

    override suspend fun delete(logId: Long) = dao.deleteLog(logId)

    override suspend fun dailyTotals(from: LocalDate, to: LocalDate): List<DailyTotal> =
        dao.dailyTotalsBetween(AppTime.dayStart(from), AppTime.dayEnd(to))
            .map { DailyTotal(LocalDate.parse(it.day), it.sugar, it.kcal) }

    override suspend fun shopDistribution(from: LocalDate, to: LocalDate): List<ShopCount> =
        dao.shopCountsBetween(AppTime.dayStart(from), AppTime.dayEnd(to))
            .map { ShopCount(it.label, it.count) }

    override suspend fun recentShops(limit: Int) = dao.recentShops(limit)
    override suspend fun recentItemNames(limit: Int) = dao.recentItemNames(limit)
}
