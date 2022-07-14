package com.artsavin.shoppinglist.domain

class EditShopItemUseCase(private val shopListRepository: ShopListRepository) {

    fun editShopItem(item: ShopItem) {
        val oldItem = shopListRepository.getShopItem(item.id)
        shopListRepository.deleteShopItem(oldItem)
        shopListRepository.addShopItem(item)
    }
}