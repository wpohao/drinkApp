package com.drinksugar.app

import android.content.Context
import com.drinksugar.core.database.AppDatabase
import com.drinksugar.core.repository.*

class AppContainer(context: Context) {
    private val db = AppDatabase.build(context)
    val catalog: CatalogRepository = RoomCatalogRepository(db.catalogDao())
    val intake: IntakeRepository = RoomIntakeRepository(db.intakeDao())
    val setting: SettingRepository = RoomSettingRepository(db.settingDao())
}
