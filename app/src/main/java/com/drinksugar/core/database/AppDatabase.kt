package com.drinksugar.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.drinksugar.core.model.*

@Database(
    entities = [
        DrinkShop::class, DrinkItem::class, SugarLevel::class, IceLevel::class,
        SizeOption::class, Topping::class, IntakeLog::class, IntakeLogTopping::class, UserSetting::class,
    ],
    version = 1, exportSchema = false,
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun catalogDao(): CatalogDao
    abstract fun intakeDao(): IntakeDao
    abstract fun settingDao(): SettingDao

    companion object {
        fun build(context: Context): AppDatabase =
            Room.databaseBuilder(context, AppDatabase::class.java, "drinksugar.db")
                .addCallback(seedCallback(context))
                .build()

        fun inMemory(context: Context): AppDatabase =
            Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
                .allowMainThreadQueries()
                .addCallback(seedCallback(context))
                .build()

        private fun seedCallback(context: Context) = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                // onCreate 內不能用 Room DAO（DB 尚未就緒），改用 SQL 直接灌種子。
                Seed.applyRaw(db)
            }
        }
    }
}
