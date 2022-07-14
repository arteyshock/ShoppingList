package com.artsavin.shoppinglist.domain

import androidx.lifecycle.MutableLiveData

interface ShopListRepository {

    fun addShopItem(item: ShopItem)

    fun deleteShopItem(item: ShopItem)

    fun editShopItem(item: ShopItem)

    fun getShopItem(itemId: Int): ShopItem

    fun getShopList(): MutableLiveData<List<ShopItem>>
}