package com.drinksugar.feature.history

import com.drinksugar.core.repository.ShopCount
import java.time.LocalDate
import java.time.YearMonth

data class MonthlyStat(
    val month: YearMonth,
    val recordedDays: Int,
    val onTargetDays: Int,
    val overDays: Int,
    val favoriteShop: String?,
)

object MonthlySummary {
    fun of(
        month: YearMonth,
        monthDays: Map<LocalDate, Double>,
        distribution: List<ShopCount>,
        targetG: Double,
    ): MonthlyStat {
        val recorded = monthDays.filterValues { it > 0.0 }
        return MonthlyStat(
            month = month,
            recordedDays = recorded.size,
            onTargetDays = recorded.count { it.value <= targetG },
            overDays = recorded.count { it.value > targetG },
            favoriteShop = distribution.firstOrNull()?.shop,
        )
    }
}
