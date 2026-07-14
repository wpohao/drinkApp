package com.drinksugar.core.database

data class DailyTotalRow(val day: String, val sugar: Double, val kcal: Double)
data class LabelCountRow(val label: String, val count: Int)
