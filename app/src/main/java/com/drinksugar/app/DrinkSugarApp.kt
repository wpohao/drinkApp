package com.drinksugar.app

import android.app.Application
import com.google.android.gms.ads.MobileAds

class DrinkSugarApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        MobileAds.initialize(this) {}
    }
}
