package com.drinksugar.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.drinksugar.R

object NotificationHelper {
    const val CHANNEL_ID = "drinksugar_default"
    private const val THRESHOLD_ID = 2001

    fun ensureChannel(context: Context) {
        val channel = NotificationChannel(
            CHANNEL_ID, "糖分提醒", NotificationManager.IMPORTANCE_DEFAULT
        )
        context.getSystemService(NotificationManager::class.java).createNotificationChannel(channel)
    }

    private fun hasPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < 33 ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED

    fun show(context: Context, id: Int, title: String, body: String) {
        ensureChannel(context)
        if (!hasPermission(context)) return
        val n = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)
            .build()
        NotificationManagerCompat.from(context).notify(id, n)
    }

    fun maybeWarnThreshold(context: Context, total: Double, target: Double, enabled: Boolean) {
        if (!enabled || !NotificationDecision.shouldWarn(total, target)) return
        val body = if (total > target) "今天糖分已超標，下一杯建議選無糖。"
                   else "今天糖分已接近上限，注意一下囉。"
        show(context, THRESHOLD_ID, "糖分提醒", body)
    }
}
