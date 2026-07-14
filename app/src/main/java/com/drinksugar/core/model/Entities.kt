package com.drinksugar.core.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drink_shop")
data class DrinkShop(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "sort_order") val sortOrder: Int = 0,
)

@Entity(tableName = "drink_item")
data class DrinkItem(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "shop_id") val shopId: Long,
    val name: String,
    val category: String? = null,
    @ColumnInfo(name = "base_sugar_g") val baseSugarG: Double,
    @ColumnInfo(name = "base_kcal") val baseKcal: Double,
    @ColumnInfo(name = "base_size_ml") val baseSizeMl: Int = 700,
    @ColumnInfo(name = "is_estimated") val isEstimated: Boolean = true,
)

@Entity(tableName = "sugar_level")
data class SugarLevel(
    @PrimaryKey val code: String,
    val label: String,
    val multiplier: Double,
    @ColumnInfo(name = "sort_order") val sortOrder: Int = 0,
)

@Entity(tableName = "ice_level")
data class IceLevel(
    @PrimaryKey val code: String,
    val label: String,
    @ColumnInfo(name = "sort_order") val sortOrder: Int = 0,
)

@Entity(tableName = "size_option")
data class SizeOption(
    @PrimaryKey val code: String,
    val label: String,
    val ml: Int,
)

@Entity(tableName = "topping")
data class Topping(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @ColumnInfo(name = "sugar_g") val sugarG: Double,
    val kcal: Double? = null,
    @ColumnInfo(name = "is_estimated") val isEstimated: Boolean = true,
    @ColumnInfo(name = "sort_order") val sortOrder: Int = 0,
)

@Entity(tableName = "intake_log")
data class IntakeLog(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "logged_at") val loggedAt: String,
    @ColumnInfo(name = "item_id") val itemId: Long? = null,
    @ColumnInfo(name = "custom_name") val customName: String? = null,
    @ColumnInfo(name = "shop_name") val shopName: String? = null,
    @ColumnInfo(name = "sugar_g") val sugarG: Double,
    val kcal: Double? = null,
    @ColumnInfo(name = "sugar_level_code") val sugarLevelCode: String? = null,
    @ColumnInfo(name = "ice_level_code") val iceLevelCode: String? = null,
    @ColumnInfo(name = "size_code") val sizeCode: String? = null,
    val rating: Int? = null,
    val note: String? = null,
)

@Entity(tableName = "intake_log_topping")
data class IntakeLogTopping(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "intake_log_id") val intakeLogId: Long,
    @ColumnInfo(name = "topping_id") val toppingId: Long? = null,
    @ColumnInfo(name = "custom_name") val customName: String? = null,
    val qty: Int = 1,
    @ColumnInfo(name = "sugar_g") val sugarG: Double,
    val kcal: Double? = null,
)

@Entity(tableName = "user_setting")
data class UserSetting(
    @PrimaryKey val id: Long = 1,
    @ColumnInfo(name = "daily_sugar_target_g") val dailySugarTargetG: Double = 50.0,
    @ColumnInfo(name = "notify_threshold") val notifyThreshold: Boolean = true,
    @ColumnInfo(name = "notify_daily_log") val notifyDailyLog: Boolean = false,
    @ColumnInfo(name = "notify_daily_log_time") val notifyDailyLogTime: String? = null,
)
