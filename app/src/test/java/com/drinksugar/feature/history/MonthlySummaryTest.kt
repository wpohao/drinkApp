package com.drinksugar.feature.history

import com.drinksugar.core.repository.ShopCount
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate
import java.time.YearMonth

class MonthlySummaryTest {
    private fun d(day: Int) = LocalDate.of(2026, 7, day)

    @Test fun summarize() {
        val stat = MonthlySummary.of(
            month = YearMonth.of(2026, 7),
            monthDays = mapOf(d(1) to 30.0, d(2) to 60.0, d(3) to 0.0),
            distribution = listOf(ShopCount("50嵐", 3), ShopCount("清心", 1)),
            targetG = 50.0,
        )
        assertEquals(2, stat.recordedDays)   // 30、60（0 不算）
        assertEquals(1, stat.onTargetDays)   // 30 ≤ 50
        assertEquals(1, stat.overDays)       // 60 > 50
        assertEquals("50嵐", stat.favoriteShop)
    }

    @Test fun emptyMonth() {
        val stat = MonthlySummary.of(YearMonth.of(2026, 7), emptyMap(), emptyList(), 50.0)
        assertEquals(0, stat.recordedDays)
        assertEquals(null, stat.favoriteShop)
    }
}
