package com.drinksugar.feature.history

import com.drinksugar.core.repository.DailyTotal
import com.drinksugar.core.repository.ShopCount
import com.drinksugar.support.FakeIntakeRepository
import com.drinksugar.support.FakeSettingRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class HistoryViewModelTest {
    @Test fun overTargetDaysAndPoints() = runTest {
        val intake = FakeIntakeRepository().apply {
            daily = listOf(
                DailyTotal(LocalDate.of(2026, 7, 8), 20.0, 0.0),
                DailyTotal(LocalDate.of(2026, 7, 9), 60.0, 0.0),  // > 50
            )
        }
        val vm = HistoryViewModel(intake, FakeSettingRepository())
        vm.setRange(HistoryRange.WEEK)
        assertEquals(1, vm.state.value.overTargetDays)
        assertEquals(2, vm.state.value.points.size)
    }

    @Test fun distributionLoaded() = runTest {
        val intake = FakeIntakeRepository().apply {
            distribution = listOf(ShopCount("50嵐", 3), ShopCount("清心", 1))
        }
        val vm = HistoryViewModel(intake, FakeSettingRepository())
        vm.reload()
        assertEquals("50嵐", vm.state.value.distribution.first().shop)
    }
}
