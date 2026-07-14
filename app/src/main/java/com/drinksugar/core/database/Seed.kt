package com.drinksugar.core.database

import androidx.sqlite.db.SupportSQLiteDatabase

object Seed {
    fun applyRaw(db: SupportSQLiteDatabase) {
        // 糖度（含分糖檔位）
        val levels = listOf(
            Triple("none", "無糖", 0.0), Triple("s10", "一分糖", 0.1), Triple("s30", "微糖", 0.3),
            Triple("half", "半糖", 0.5), Triple("s70", "少糖", 0.7), Triple("s80", "八分糖", 0.8),
            Triple("full", "全糖", 1.0),
        )
        levels.forEachIndexed { i, (code, label, m) ->
            db.execSQL("INSERT INTO sugar_level(code,label,multiplier,sort_order) VALUES(?,?,?,?)", arrayOf(code, label, m, i))
        }
        // 冰塊（純記錄，無 multiplier）
        listOf("normal" to "正常冰", "less" to "少冰", "light" to "微冰", "free" to "去冰", "hot" to "熱")
            .forEachIndexed { i, (code, label) ->
                db.execSQL("INSERT INTO ice_level(code,label,sort_order) VALUES(?,?,?)", arrayOf(code, label, i))
            }
        // 容量
        listOf(Triple("m", "中杯", 500), Triple("l", "大杯", 700), Triple("xl", "特大杯", 1000)).forEach { (code, label, ml) ->
            db.execSQL("INSERT INTO size_option(code,label,ml) VALUES(?,?,?)", arrayOf(code, label, ml))
        }
        // 加料（單份糖分/熱量，皆參考估算）
        val toppings = listOf(
            arrayOf<Any?>("珍珠", 15.0, 100.0, 0), arrayOf<Any?>("椰果", 8.0, 40.0, 1),
            arrayOf<Any?>("布丁", 12.0, 90.0, 2), arrayOf<Any?>("仙草", 6.0, 35.0, 3),
            arrayOf<Any?>("愛玉", 5.0, 25.0, 4),
        )
        toppings.forEach {
            db.execSQL("INSERT INTO topping(name,sugar_g,kcal,is_estimated,sort_order) VALUES(?,?,?,1,?)", it)
        }
        // 示範店家與品項（全糖、base_size_ml=700 基準；數值參考估算，待資料策展擴充）
        db.execSQL("INSERT INTO drink_shop(id,name,sort_order) VALUES(1,'示範飲料店',0)")
        val items = listOf(
            arrayOf<Any?>("珍珠奶茶", "奶茶", 62.0, 480.0), arrayOf<Any?>("紅茶", "純茶", 45.0, 190.0),
            arrayOf<Any?>("多多綠", "果茶", 55.0, 240.0),
        )
        items.forEach {
            db.execSQL("INSERT INTO drink_item(shop_id,name,category,base_sugar_g,base_kcal,base_size_ml,is_estimated) VALUES(1,?,?,?,?,700,1)", it)
        }
        // 使用者設定單列
        db.execSQL("INSERT INTO user_setting(id,daily_sugar_target_g,notify_threshold,notify_daily_log,notify_daily_log_time) VALUES(1,50,1,0,NULL)")
    }
}
