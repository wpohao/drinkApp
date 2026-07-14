package com.drinksugar.feature.history

import org.junit.Assert.assertEquals
import org.junit.Test

class CalendarStatusRuleTest {
    @Test fun noneWhenNullOrZero() {
        assertEquals(CalendarStatus.NONE, CalendarStatusRule.status(null, 50.0))
        assertEquals(CalendarStatus.NONE, CalendarStatusRule.status(0.0, 50.0))
    }
    @Test fun underWhenWithinTarget() {
        assertEquals(CalendarStatus.UNDER, CalendarStatusRule.status(30.0, 50.0))
        assertEquals(CalendarStatus.UNDER, CalendarStatusRule.status(50.0, 50.0))
    }
    @Test fun overWhenExceeds() {
        assertEquals(CalendarStatus.OVER, CalendarStatusRule.status(60.0, 50.0))
    }
}
