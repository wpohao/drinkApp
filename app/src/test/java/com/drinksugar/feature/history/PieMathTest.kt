package com.drinksugar.feature.history

import org.junit.Assert.assertEquals
import org.junit.Test

class PieMathTest {
    @Test fun sweepsProportional() {
        val s = PieMath.sweeps(listOf(3, 1))
        assertEquals(270f, s[0], 0.01f)
        assertEquals(90f, s[1], 0.01f)
    }
    @Test fun sweepsSumTo360() {
        val s = PieMath.sweeps(listOf(1, 1, 2))
        assertEquals(360f, s.sum(), 0.01f)
    }
    @Test fun sweepsEmptyTotalAllZero() {
        assertEquals(listOf(0f, 0f), PieMath.sweeps(listOf(0, 0)))
    }
}
