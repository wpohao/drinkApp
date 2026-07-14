package com.drinksugar.notification

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDateTime

class NotificationLogicTest {
    @Test fun warnWhenReaching80Percent() {
        assertTrue(NotificationDecision.shouldWarn(40.0, 50.0))   // 0.8
        assertTrue(NotificationDecision.shouldWarn(55.0, 50.0))   // 超標
    }
    @Test fun noWarnBelowThreshold() {
        assertFalse(NotificationDecision.shouldWarn(30.0, 50.0))  // 0.6
    }
    @Test fun zeroTargetNeverWarns() {
        assertFalse(NotificationDecision.shouldWarn(10.0, 0.0))
    }
    @Test fun delayLaterToday() {
        val now = LocalDateTime.of(2026, 7, 14, 9, 0)
        assertEquals(12 * 60L, ReminderScheduler.initialDelayMinutes(21, 0, now))
    }
    @Test fun delayRollsToTomorrow() {
        val now = LocalDateTime.of(2026, 7, 14, 22, 0)
        assertEquals(23 * 60L, ReminderScheduler.initialDelayMinutes(21, 0, now))
    }
}
