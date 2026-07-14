package com.drinksugar.core.logic

import org.junit.Assert.assertEquals
import org.junit.Test

class SugarCalculatorTest {
    @Test fun fullSugarAtBaseSize() {
        assertEquals(62.0, SugarCalculator.drinkSugar(62.0, 1.0, 700, 700), 0.001)
    }
    @Test fun halfSugarMediumSize() {
        assertEquals(62.0 * 0.5 * (500.0 / 700.0),
            SugarCalculator.drinkSugar(62.0, 0.5, 500, 700), 0.001)
    }
    @Test fun noSugarIsZero() {
        assertEquals(0.0, SugarCalculator.drinkSugar(62.0, 0.0, 1000, 700), 0.001)
    }
    @Test fun kcalReducesWhenLessSugar() {
        assertEquals(480.0 - 62.0 * 4, SugarCalculator.drinkKcal(480.0, 62.0, 0.0, 700, 700), 0.001)
    }
    @Test fun kcalFullSugarEqualsBase() {
        assertEquals(480.0, SugarCalculator.drinkKcal(480.0, 62.0, 1.0, 700, 700), 0.001)
    }
    @Test fun toppingsSugarSums() {
        assertEquals(15.0 + 16.0, SugarCalculator.toppingsSugar(listOf(15.0 to 1, 8.0 to 2)), 0.001)
    }
    @Test fun totalSugarWithToppings() {
        assertEquals(31.0 + 15.0,
            SugarCalculator.totalSugar(62.0, 0.5, 700, 700, listOf(15.0 to 1)), 0.001)
    }
}
