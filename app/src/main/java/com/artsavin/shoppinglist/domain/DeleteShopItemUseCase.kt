package com.artsavin.shoppinglist.domain

class DeleteShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun deleteShopItem(item: ShopItem) {
        shopListRepository.deleteShopItem(item)
    }
}