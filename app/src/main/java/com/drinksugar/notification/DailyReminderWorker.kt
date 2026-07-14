package com.drinksugar.notification

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class DailyReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {
    override suspend fun doWork(): Result {
        NotificationHelper.show(applicationContext, 1001, "別忘了記錄", "今天喝了什麼飲料？花 10 秒記一下吧。")
        return Result.success()
    }
}
