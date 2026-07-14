package com.drinksugar.advice

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AdviceRuleTest {
    @Test fun overTarget() {
        assertTrue(AdviceRule.message(AdviceInput(60.0, 50.0, 0)).contains("超標"))
    }
    @Test fun nearTarget() {
        assertTrue(AdviceRule.message(AdviceInput(45.0, 50.0, 0)).contains("接近"))
    }
    @Test fun streakEncouragement() {
        assertTrue(AdviceRule.message(AdviceInput(10.0, 50.0, 3)).contains("連續"))
    }
    @Test fun neutralNonEmpty() {
        assertFalse(AdviceRule.message(AdviceInput(10.0, 50.0, 0)).isEmpty())
    }
}
