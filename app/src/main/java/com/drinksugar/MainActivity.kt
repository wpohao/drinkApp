package com.drinksugar

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.drinksugar.app.AppRoot
import com.drinksugar.app.DrinkSugarApp
import com.drinksugar.ui.theme.DrinkSugarTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPerm = registerForActivityResult(ActivityResultContracts.RequestPermission()) {}
        if (Build.VERSION.SDK_INT >= 33 &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPerm.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        val container = (application as DrinkSugarApp).container
        setContent { DrinkSugarTheme { AppRoot(container) } }
    }
}
