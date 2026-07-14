package com.drinksugar.core.database

import androidx.test.core.app.ApplicationProvider
import com.drinksugar.core.model.SugarLevel
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class AppDatabaseTest {
    private lateinit var db: AppDatabase

    @Before fun setUp() {
        db = AppDatabase.inMemory(ApplicationProvider.getApplicationContext())
    }
    @After fun tearDown() = db.close()

    @Test fun sugarLevelsSeeded() = runTest {
        val levels = db.catalogDao().sugarLevels()
        assertEquals(7, levels.size)
        assertEquals(1.0, levels.first { it.code == "full" }.multiplier, 0.0001)
        assertEquals(0.1, levels.first { it.code == "s10" }.multiplier, 0.0001)
        assertEquals(0.0, levels.first { it.code == "none" }.multiplier, 0.0001)
    }

    @Test fun iceLevelsSeeded() = runTest {
        val ice = db.catalogDao().iceLevels()
        assertEquals(setOf("normal", "less", "light", "free", "hot"), ice.map { it.code }.toSet())
    }

    @Test fun sizesSeeded() = runTest {
        val sizes = db.catalogDao().sizes()
        assertEquals(1000, sizes.first { it.code == "xl" }.ml)
    }

    @Test fun defaultUserSetting() = runTest {
        assertEquals(50.0, db.settingDao().find()!!.dailySugarTargetG, 0.0001)
    }
}
