package com.drinksugar.feature.history

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream

object ShareUtil {
    fun shareBitmap(context: Context, bitmap: Bitmap) {
        val dir = File(context.cacheDir, "shares").apply { mkdirs() }
        val file = File(dir, "monthly_card.png")
        FileOutputStream(file).use { bitmap.compress(Bitmap.CompressFormat.PNG, 100, it) }
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val send = Intent(Intent.ACTION_SEND).apply {
            type = "image/png"
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(
            Intent.createChooser(send, "分享月報").addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        )
    }
}
