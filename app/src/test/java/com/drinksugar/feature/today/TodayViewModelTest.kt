package com.drinksugar.feature.today

import com.drinksugar.core.model.IntakeLog
import com.drinksugar.support.FakeIntakeRepository
import com.drinksugar.support.FakeSettingRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TodayViewModelTest {
    @Test fun reloadComputesRemainingAndProgress() = runTest {
        val intake = FakeIntakeRepository().apply {
            totalSugar = 40.0
            todayLogs = mutableListOf(IntakeLog(id = 1, loggedAt = "2026-07-14 12:00:00", customName = "珍奶", sugarG = 40.0))
        }
        val vm = TodayViewModel(intake, FakeSettingRepository())
        vm.reload()
        val st = vm.state.value
        assertEquals(40.0, st.todaySugar, 0.001)
        assertEquals(50.0, st.targetG, 0.001)
        assertEquals(10.0, st.remaining, 0.001)
        assertEquals(0.8, st.progress, 0.001)
        assertEquals(1, st.logs.size)
        assertTrue(st.advice.isNotEmpty())
    }

    @Test fun progressClampedAtOne() = runTest {
        val intake = FakeIntakeRepository().apply { totalSugar = 80.0 }
        val vm = TodayViewModel(intake, FakeSettingRepository())
        vm.reload()
        assertEquals(1.0, vm.state.value.progress, 0.001)
        assertTrue(vm.state.value.remaining < 0)
    }

    @Test fun deleteReloads() = runTest {
        val intake = FakeIntakeRepository().apply {
            todayLogs = mutableListOf(IntakeLog(id = 7, loggedAt = "2026-07-14 12:00:00", customName = "x", sugarG = 10.0))
        }
        val vm = TodayViewModel(intake, FakeSettingRepository())
        vm.reload()
        vm.delete(vm.state.value.logs.first())
        assertEquals(0, vm.state.value.logs.size)
    }
}
