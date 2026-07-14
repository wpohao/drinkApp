package com.drinksugar.notification

object NotificationDecision {
    fun shouldWarn(total: Double, target: Double, thresholdRatio: Double = 0.8): Boolean {
        if (target <= 0) return false
        return total >= target * thresholdRatio
    }
}
