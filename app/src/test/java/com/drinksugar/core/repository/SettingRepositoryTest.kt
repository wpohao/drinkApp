package com.drinksugar.core.repository

import androidx.test.core.app.ApplicationProvider
import com.drinksugar.core.database.AppDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class SettingRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repo: RoomSettingRepository

    @Before fun setUp() {
        db = AppDatabase.inMemory(ApplicationProvider.getApplicationContext())
        repo = RoomSettingRepository(db.settingDao())
    }
    @After fun tearDown() = db.close()

    @Test fun defaultThenUpdate() = runTest {
        assertEquals(50.0, repo.setting().dailySugarTargetG, 0.001)
        repo.update(repo.setting().copy(dailySugarTargetG = 25.0))
        assertEquals(25.0, repo.setting().dailySugarTargetG, 0.001)
    }
}
