package com.artsavin.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.artsavin.shoppinglist.domain.ShopItem
import com.artsavin.shoppinglist.domain.ShopListRepository
import javax.inject.Inject

class ShopListRepositoryImpl @Inject constructor(
    private val shopListDao: ShopListDao,
    private val mapper: ShopListMapper
): ShopListRepository {

//    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
//    private val mapper = ShopListMapper()

    override suspend fun addShopItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override suspend fun deleteShopItem(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override suspend fun editShopItem(item: ShopItem) {
        // см onConflict
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override suspend fun getShopItem(itemId: Int): ShopItem {
        val item = shopListDao.getShopItem(itemId)
        return mapper.mapDbModelToEntity(item)
    }

    override fun getShopList(): LiveData<List<ShopItem>> = Transformations.map(
        shopListDao.getShopList()
    ) {
        mapper.mapListDbModelToListEntity(it)
    }
}