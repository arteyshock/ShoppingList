package com.artsavin.shoppinglist.data

import com.artsavin.shoppinglist.domain.ShopItem
import com.artsavin.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException

class ShopListRepositoryImpl: ShopListRepository {

    private val itemList = mutableListOf<ShopItem>()

    private var autoIncrementId = 0

    override fun addShopItem(item: ShopItem) {
        if (item.id <= ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        itemList.add(item)
    }

    override fun deleteShopItem(item: ShopItem) {
        itemList.remove(item)
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItem(item.id)
        deleteShopItem(oldItem)
        addShopItem(item)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return itemList.find { it.id == itemId } ?: throw RuntimeException(
            "Item with id $itemId not found"
        )
    }

    override fun getShopList(): List<ShopItem> {
        return itemList.toList()
    }
}