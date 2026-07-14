package com.drinksugar.core.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EntitiesTest {
    @Test fun intakeLogDefaults() {
        val log = IntakeLog(loggedAt = "2026-07-14 12:00:00", sugarG = 31.0)
        assertEquals(0L, log.id)          // 未存檔
        assertNull(log.itemId)
        assertNull(log.rating)
        assertNull(log.iceLevelCode)
    }

    @Test fun iceLevelIsPlainTag() {
        val ice = IceLevel(code = "free", label = "去冰", sortOrder = 3)
        assertEquals("free", ice.code)    // 無 multiplier 欄位
    }
}
