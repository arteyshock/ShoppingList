package com.artsavin.shoppinglist.domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface ShopListRepository {

    suspend fun addShopItem(item: ShopItem)

    suspend fun deleteShopItem(item: ShopItem)

    suspend fun editShopItem(item: ShopItem)

    suspend fun getShopItem(itemId: Int): ShopItem

    fun getShopList(): LiveData<List<ShopItem>>
}