package com.drinksugar.core.repository

import com.drinksugar.core.database.SettingDao
import com.drinksugar.core.model.UserSetting

interface SettingRepository {
    suspend fun setting(): UserSetting
    suspend fun update(s: UserSetting)
}

class RoomSettingRepository(private val dao: SettingDao) : SettingRepository {
    override suspend fun setting(): UserSetting = dao.find() ?: UserSetting()
    override suspend fun update(s: UserSetting) = dao.upsert(s.copy(id = 1))
}
