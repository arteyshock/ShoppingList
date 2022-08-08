package com.artsavin.shoppinglist.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.artsavin.shoppinglist.domain.ShopItem
import com.artsavin.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

class ShopListRepositoryImpl(
    application: Application
): ShopListRepository {

    private val shopListDao = AppDataBase.getInstance(application).shopListDao()
    private val mapper = ShopListMapper()

    override fun addShopItem(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override fun deleteShopItem(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override fun editShopItem(item: ShopItem) {
        // см onConflict
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }

    override fun getShopItem(itemId: Int): ShopItem {
        val item = shopListDao.getShopItem(itemId)
        return mapper.mapDbModelToEntity(item)
    }

    override fun getShopList(): MutableLiveData<List<ShopItem>> = shopListDao.getShopList()
}