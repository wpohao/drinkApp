package com.drinksugar.feature.adddrink

import com.drinksugar.support.FakeCatalogRepository
import com.drinksugar.support.FakeIntakeRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AddDrinkViewModelTest {
    private suspend fun loadedVm(intake: FakeIntakeRepository = FakeIntakeRepository()): AddDrinkViewModel {
        val vm = AddDrinkViewModel(FakeCatalogRepository(), intake)
        vm.load()
        return vm
    }

    @Test fun previewHalfSugarLargeNoTopping() = runTest {
        val vm = loadedVm()
        val s = vm.state.value
        vm.selectItem(s.items.first { it.name == "珍珠奶茶" })  // base 62
        vm.selectLevel(s.levels.first { it.code == "half" })    // 0.5
        vm.selectSize(s.sizes.first { it.code == "l" })         // 700
        assertEquals(31.0, vm.state.value.previewSugar, 0.001)
    }

    @Test fun previewAddsTopping() = runTest {
        val vm = loadedVm()
        val s = vm.state.value
        vm.selectItem(s.items.first { it.name == "珍珠奶茶" })
        vm.selectLevel(s.levels.first { it.code == "half" })
        vm.selectSize(s.sizes.first { it.code == "l" })
        vm.setToppingQty(s.toppings.first { it.name == "珍珠" }.id, 1)  // sugar 15
        assertEquals(31.0 + 15.0, vm.state.value.previewSugar, 0.001)
    }

    @Test fun saveWritesLogWithIceAndRating() = runTest {
        val intake = FakeIntakeRepository()
        val vm = loadedVm(intake)
        val s = vm.state.value
        vm.selectItem(s.items.first { it.name == "紅茶" })
        vm.selectLevel(s.levels.first { it.code == "full" })
        vm.selectSize(s.sizes.first { it.code == "m" })
        vm.selectIce(s.iceLevels.first { it.code == "free" })
        vm.setRating(5)
        assertTrue(vm.save())
        assertEquals(1, intake.saved.size)
        assertEquals("free", intake.saved.first().iceLevelCode)
        assertEquals(5, intake.saved.first().rating)
        assertEquals("示範飲料店", intake.saved.first().shopName)
    }

    @Test fun manualEntry() = runTest {
        val intake = FakeIntakeRepository()
        val vm = loadedVm(intake)
        vm.setManual(true)
        vm.setCustomName("自訂飲料")
        vm.setCustomSugar("18")
        assertEquals(18.0, vm.state.value.previewSugar, 0.001)
        assertTrue(vm.save())
        assertEquals("自訂飲料", intake.saved.first().customName)
    }

    @Test fun manualEmptyNameFailsSave() = runTest {
        val vm = loadedVm()
        vm.setManual(true)
        vm.setCustomName("   ")
        vm.setCustomSugar("10")
        assertTrue(!vm.save())
    }

    @Test fun loadPopulatesRecentAndApplyRecentSwitchesToManual() = runTest {
        val intake = FakeIntakeRepository().apply { recentItems = listOf("珍奶", "紅茶") }
        val vm = loadedVm(intake)
        assertEquals(listOf("珍奶", "紅茶"), vm.state.value.recentItemNames)
        vm.applyRecent("珍奶")
        assertTrue(vm.state.value.isManual)
        assertEquals("珍奶", vm.state.value.customName)
    }
}
