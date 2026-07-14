package com.drinksugar.core.repository

import com.drinksugar.core.database.CatalogDao
import com.drinksugar.core.model.*

interface CatalogRepository {
    suspend fun shops(): List<DrinkShop>
    suspend fun items(shopId: Long): List<DrinkItem>
    suspend fun sugarLevels(): List<SugarLevel>
    suspend fun iceLevels(): List<IceLevel>
    suspend fun sizes(): List<SizeOption>
    suspend fun toppings(): List<Topping>
}

class RoomCatalogRepository(private val dao: CatalogDao) : CatalogRepository {
    override suspend fun shops() = dao.shops()
    override suspend fun items(shopId: Long) = dao.items(shopId)
    override suspend fun sugarLevels() = dao.sugarLevels()
    override suspend fun iceLevels() = dao.iceLevels()
    override suspend fun sizes() = dao.sizes()
    override suspend fun toppings() = dao.toppings()
}
