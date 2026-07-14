package com.drinksugar.feature.history

object PieMath {
    fun sweeps(counts: List<Int>): List<Float> {
        val total = counts.sum()
        if (total == 0) return counts.map { 0f }
        return counts.map { it.toFloat() / total * 360f }
    }
}
