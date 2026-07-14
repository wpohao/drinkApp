package com.drinksugar.advice

data class AdviceInput(
    val todaySugarG: Double,
    val targetG: Double,
    val consecutiveOnTargetDays: Int,
)

object AdviceRule {
    fun message(input: AdviceInput): String {
        if (input.todaySugarG > input.targetG) {
            return "今天糖分超標囉，明天可以試試改點半糖或無糖。"
        }
        if (input.targetG > 0 && input.todaySugarG >= input.targetG * 0.8) {
            return "已接近今日糖分上限，下一杯建議選微糖或無糖。"
        }
        if (input.consecutiveOnTargetDays >= 3) {
            return "連續 ${input.consecutiveOnTargetDays} 天達標，控糖做得很好，繼續保持！"
        }
        return "今天的糖分還在額度內，喝得開心也記得適量。"
    }
}
