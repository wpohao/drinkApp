package com.drinksugar.feature.settings

import com.drinksugar.support.FakeSettingRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class SettingsViewModelTest {
    @Test fun loadAndSave() = runTest {
        val repo = FakeSettingRepository()
        val vm = SettingsViewModel(repo)
        vm.load()
        assertEquals(50.0, vm.state.value.targetG, 0.001)
        vm.setTarget(25.0)
        vm.setNotifyThreshold(false)
        vm.save()
        assertEquals(25.0, repo.current.dailySugarTargetG, 0.001)
        assertFalse(repo.current.notifyThreshold)
    }

    @Test fun dailyLogTimePersisted() = runTest {
        val repo = FakeSettingRepository()
        val vm = SettingsViewModel(repo)
        vm.load()
        vm.setNotifyDailyLog(true)
        vm.setDailyLogTime("08:30")
        vm.save()
        assertEquals("08:30", repo.current.notifyDailyLogTime)
    }
}
