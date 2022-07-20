package com.artsavin.shoppinglist.data

import androidx.lifecycle.MutableLiveData
import com.artsavin.shoppinglist.domain.ShopItem
import com.artsavin.shoppinglist.domain.ShopListRepository
import java.lang.RuntimeException
import kotlin.random.Random

class ShopListRepositoryImpl: ShopListRepository {

    private val itemListLD = MutableLiveData<List<ShopItem>>()
    //TODO + сортировка по статусу ВКЛ - ВЫКЛ
    private val itemList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id)})

    private var autoIncrementId = 0

    init {
        for (i in 0 until 10) {
            val name = "Item $i"
            addShopItem(ShopItem(name, i, Random.nextBoolean()))
        }
    }

    override fun addShopItem(item: ShopItem) {
        if (item.id <= ShopItem.UNDEFINED_ID) {
            item.id = autoIncrementId++
        }
        itemList.add(item)
        updateList()
    }

    override fun deleteShopItem(item: ShopItem) {
        itemList.remove(item)
        updateList()
    }

    override fun editShopItem(item: ShopItem) {
        val oldItem = getShopItem(item.id)
        itemList.remove(oldItem)
        addShopItem(item)
    }

    override fun getShopItem(itemId: Int): ShopItem {
        return itemList.find { it.id == itemId } ?: throw RuntimeException(
            "Item with id $itemId not found"
        )
    }

    override fun getShopList(): MutableLiveData<List<ShopItem>> {
        return itemListLD
    }

    private fun updateList() {
        itemListLD.value = itemList.toList()
    }
}