package com.drinksugar.core.logic

object SugarCalculator {
    const val KCAL_PER_SUGAR_GRAM = 4.0

    fun drinkSugar(baseSugarG: Double, multiplier: Double, sizeMl: Int, baseSizeMl: Int): Double {
        val ratio = sizeMl.toDouble() / baseSizeMl.toDouble()
        return baseSugarG * multiplier * ratio
    }

    fun drinkKcal(baseKcal: Double, baseSugarG: Double, multiplier: Double, sizeMl: Int, baseSizeMl: Int): Double {
        val ratio = sizeMl.toDouble() / baseSizeMl.toDouble()
        return (baseKcal - baseSugarG * (1 - multiplier) * KCAL_PER_SUGAR_GRAM) * ratio
    }

    fun toppingsSugar(items: List<Pair<Double, Int>>): Double =
        items.sumOf { it.first * it.second }

    fun toppingsKcal(items: List<Pair<Double, Int>>): Double =
        items.sumOf { it.first * it.second }

    fun totalSugar(baseSugarG: Double, multiplier: Double, sizeMl: Int, baseSizeMl: Int,
                   toppings: List<Pair<Double, Int>>): Double =
        drinkSugar(baseSugarG, multiplier, sizeMl, baseSizeMl) + toppingsSugar(toppings)

    fun totalKcal(baseKcal: Double, baseSugarG: Double, multiplier: Double, sizeMl: Int, baseSizeMl: Int,
                  toppings: List<Pair<Double, Int>>): Double =
        drinkKcal(baseKcal, baseSugarG, multiplier, sizeMl, baseSizeMl) + toppingsKcal(toppings)
}
