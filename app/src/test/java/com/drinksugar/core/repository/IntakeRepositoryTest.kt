package com.drinksugar.core.repository

import androidx.test.core.app.ApplicationProvider
import com.drinksugar.core.database.AppDatabase
import com.drinksugar.core.model.IntakeLog
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.time.LocalDate

@RunWith(RobolectricTestRunner::class)
class IntakeRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repo: RoomIntakeRepository

    @Before fun setUp() {
        db = AppDatabase.inMemory(ApplicationProvider.getApplicationContext())
        repo = RoomIntakeRepository(db.intakeDao())
    }
    @After fun tearDown() = db.close()

    private fun at(y: Int, mo: Int, d: Int, h: Int = 12) = "%04d-%02d-%02d %02d:00:00".format(y, mo, d, h)

    @Test fun saveAndDailyTotal() = runTest {
        val day = LocalDate.of(2026, 7, 9)
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "珍奶", shopName = "50嵐", sugarG = 31.0, kcal = 240.0), emptyList())
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "紅茶", shopName = "50嵐", sugarG = 20.0, kcal = 90.0), emptyList())
        assertEquals(51.0, repo.dailyTotalSugar(day), 0.001)
        assertEquals(2, repo.logs(day).size)
    }

    @Test fun deleteRemovesLog() = runTest {
        val day = LocalDate.of(2026, 7, 9)
        val id = repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "測試", sugarG = 10.0), emptyList())
        repo.delete(id)
        assertEquals(0, repo.logs(day).size)
    }

    @Test fun dailyTotalsRange() = runTest {
        repo.save(IntakeLog(loggedAt = at(2026, 7, 8), customName = "a", sugarG = 10.0), emptyList())
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "b", sugarG = 20.0), emptyList())
        val totals = repo.dailyTotals(LocalDate.of(2026, 7, 8), LocalDate.of(2026, 7, 9))
        assertEquals(2, totals.size)
        assertEquals(30.0, totals.sumOf { it.sugarG }, 0.001)
    }

    @Test fun shopDistributionCounts() = runTest {
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "a", shopName = "50嵐", sugarG = 10.0), emptyList())
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "b", shopName = "50嵐", sugarG = 10.0), emptyList())
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9), customName = "c", shopName = "清心", sugarG = 10.0), emptyList())
        val dist = repo.shopDistribution(LocalDate.of(2026, 7, 9), LocalDate.of(2026, 7, 9))
        assertEquals("50嵐", dist.first().shop)   // 依 count DESC
        assertEquals(2, dist.first().count)
    }

    @Test fun recentShopsDistinct() = runTest {
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9, 10), customName = "a", shopName = "50嵐", sugarG = 10.0), emptyList())
        repo.save(IntakeLog(loggedAt = at(2026, 7, 9, 11), customName = "b", shopName = "清心", sugarG = 10.0), emptyList())
        assertEquals(setOf("50嵐", "清心"), repo.recentShops(8).toSet())
    }
}
