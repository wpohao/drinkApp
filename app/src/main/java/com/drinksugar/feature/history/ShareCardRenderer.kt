package com.drinksugar.feature.history

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object ShareCardRenderer {
    fun render(stat: MonthlyStat): Bitmap {
        val size = 1080
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.parseColor("#FFF8E1"))
        val cx = size / 2f
        val title = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#5D4037"); textSize = 84f; textAlign = Paint.Align.CENTER; isFakeBoldText = true
        }
        val body = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#5D4037"); textSize = 56f; textAlign = Paint.Align.CENTER
        }
        val tag = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#4CAF50"); textSize = 48f; textAlign = Paint.Align.CENTER
        }
        val small = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#9E9E9E"); textSize = 34f; textAlign = Paint.Align.CENTER
        }
        canvas.drawText("控糖月報", cx, 200f, title)
        canvas.drawText("${stat.month.year} 年 ${stat.month.monthValue} 月", cx, 300f, body)
        canvas.drawText("有紀錄 ${stat.recordedDays} 天", cx, 470f, body)
        canvas.drawText("達標 ${stat.onTargetDays} 天 · 超標 ${stat.overDays} 天", cx, 570f, body)
        canvas.drawText("最愛店家：${stat.favoriteShop ?: "—"}", cx, 670f, body)
        canvas.drawText("#控糖 #手搖日記", cx, 880f, tag)
        canvas.drawText("數值為參考估算，非醫療建議", cx, 990f, small)
        return bmp
    }
}
