package com.drinksugar.core.repository

import androidx.test.core.app.ApplicationProvider
import com.drinksugar.core.database.AppDatabase
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class CatalogRepositoryTest {
    private lateinit var db: AppDatabase
    private lateinit var repo: RoomCatalogRepository

    @Before fun setUp() {
        db = AppDatabase.inMemory(ApplicationProvider.getApplicationContext())
        repo = RoomCatalogRepository(db.catalogDao())
    }
    @After fun tearDown() = db.close()

    @Test fun sugarLevelsOrdered() = runTest {
        assertEquals(listOf("none", "s10", "s30", "half", "s70", "s80", "full"),
            repo.sugarLevels().map { it.code })
    }
    @Test fun iceLevelsSeeded() = runTest {
        assertTrue(repo.iceLevels().any { it.code == "free" })
    }
    @Test fun itemsForSeededShop() = runTest {
        val shop = repo.shops().first()
        assertTrue(repo.items(shop.id).any { it.name == "珍珠奶茶" })
    }
    @Test fun toppingsSeeded() = runTest {
        assertTrue(repo.toppings().any { it.name == "珍珠" })
    }
}
