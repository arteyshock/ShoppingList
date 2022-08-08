package com.artsavin.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.artsavin.shoppinglist.data.ShopListRepositoryImpl
import com.artsavin.shoppinglist.domain.DeleteShopItemUseCase
import com.artsavin.shoppinglist.domain.EditShopItemUseCase
import com.artsavin.shoppinglist.domain.GetShopListUseCase
import com.artsavin.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainViewModel(application: Application): AndroidViewModel(application) {

    //TODO перейти на DI
    private val shopListRepository = ShopListRepositoryImpl(application)

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(shopListRepository)
    private val editShopItemUseCase = EditShopItemUseCase(shopListRepository)

    private val scope = CoroutineScope(Dispatchers.IO)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        scope.launch {
            deleteShopItemUseCase.deleteShopItem(shopItem)
        }
    }

    fun toggleShopItem(shopItem: ShopItem) {
        scope.launch {
            val newShopItem = shopItem.copy(enabled = !shopItem.enabled)
            editShopItemUseCase.editShopItem(newShopItem)
        }
    }

    override fun onCleared() {
        super.onCleared()
        scope.cancel()
    }
}