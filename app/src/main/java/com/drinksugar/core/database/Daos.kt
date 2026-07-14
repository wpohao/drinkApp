package com.drinksugar.core.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.drinksugar.core.model.*

@Dao
interface CatalogDao {
    @Query("SELECT * FROM drink_shop ORDER BY sort_order") suspend fun shops(): List<DrinkShop>
    @Query("SELECT * FROM drink_item WHERE shop_id = :shopId ORDER BY name") suspend fun items(shopId: Long): List<DrinkItem>
    @Query("SELECT * FROM sugar_level ORDER BY sort_order") suspend fun sugarLevels(): List<SugarLevel>
    @Query("SELECT * FROM ice_level ORDER BY sort_order") suspend fun iceLevels(): List<IceLevel>
    @Query("SELECT * FROM size_option ORDER BY ml") suspend fun sizes(): List<SizeOption>
    @Query("SELECT * FROM topping ORDER BY sort_order") suspend fun toppings(): List<Topping>
}

@Dao
interface IntakeDao {
    @Insert suspend fun insertLog(log: IntakeLog): Long
    @Insert suspend fun insertToppings(rows: List<IntakeLogTopping>)
    @Query("SELECT * FROM intake_log WHERE logged_at >= :start AND logged_at < :end ORDER BY logged_at DESC")
    suspend fun logsBetween(start: String, end: String): List<IntakeLog>
    @Query("SELECT COALESCE(SUM(sugar_g),0) FROM intake_log WHERE logged_at >= :start AND logged_at < :end")
    suspend fun totalSugarBetween(start: String, end: String): Double
    @Query("DELETE FROM intake_log WHERE id = :id") suspend fun deleteLog(id: Long)
    @Query("""SELECT substr(logged_at,1,10) AS day, COALESCE(SUM(sugar_g),0) AS sugar, COALESCE(SUM(kcal),0) AS kcal
              FROM intake_log WHERE logged_at >= :start AND logged_at < :end
              GROUP BY day ORDER BY day""")
    suspend fun dailyTotalsBetween(start: String, end: String): List<DailyTotalRow>
    @Query("""SELECT COALESCE(shop_name,'') AS label, COUNT(*) AS count FROM intake_log
              WHERE logged_at >= :start AND logged_at < :end AND shop_name IS NOT NULL AND shop_name <> ''
              GROUP BY shop_name ORDER BY count DESC""")
    suspend fun shopCountsBetween(start: String, end: String): List<LabelCountRow>
    @Query("SELECT DISTINCT shop_name FROM intake_log WHERE shop_name IS NOT NULL AND shop_name <> '' ORDER BY logged_at DESC LIMIT :limit")
    suspend fun recentShops(limit: Int): List<String>
    @Query("SELECT DISTINCT custom_name FROM intake_log WHERE custom_name IS NOT NULL AND custom_name <> '' ORDER BY logged_at DESC LIMIT :limit")
    suspend fun recentItemNames(limit: Int): List<String>
}

@Dao
interface SettingDao {
    @Query("SELECT * FROM user_setting WHERE id = 1") suspend fun find(): UserSetting?
    @Upsert suspend fun upsert(setting: UserSetting)
}
