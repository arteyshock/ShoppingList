package com.artsavin.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.artsavin.shoppinglist.data.ShopListRepositoryImpl
import com.artsavin.shoppinglist.domain.DeleteShopItemUseCase
import com.artsavin.shoppinglist.domain.EditShopItemUseCase
import com.artsavin.shoppinglist.domain.GetShopListUseCase
import com.artsavin.shoppinglist.domain.ShopItem

class MainViewModel: ViewModel() {

    //TODO перейти на DI
    private val shopListRepository = ShopListRepositoryImpl()

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun toggleShopItem(shopItem: ShopItem) {
        val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
        editShopItemUseCase.editShopItem(newShopItem)
    }
}