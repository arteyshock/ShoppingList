package com.artsavin.shoppinglist.data

import com.artsavin.shoppinglist.domain.ShopItem

class ShopListMapper {

    fun mapEntityToDbModel(shopItem: ShopItem) = ShopItemDbModel(
        shopItem.id,
        shopItem.name,
        shopItem.count,
        shopItem.enabled
    )

    fun mapDbModelToEntity(model: ShopItemDbModel) = ShopItem(
        model.name,
        model.count,
        model.enabled,
        model.id
    )

    fun mapListDbModelToListEntity(listModel: List<ShopItemDbModel>) = listModel.map {
        mapDbModelToEntity(it)
    }
}