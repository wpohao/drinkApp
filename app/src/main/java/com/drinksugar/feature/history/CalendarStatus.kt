package com.drinksugar.feature.history

enum class CalendarStatus { NONE, UNDER, OVER }

object CalendarStatusRule {
    fun status(sugar: Double?, target: Double): CalendarStatus = when {
        sugar == null || sugar <= 0.0 -> CalendarStatus.NONE
        sugar > target -> CalendarStatus.OVER
        else -> CalendarStatus.UNDER
    }
}
