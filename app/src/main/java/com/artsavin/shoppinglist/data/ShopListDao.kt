package com.artsavin.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items ORDER BY enabled DESC, id")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addShopItem(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id = :itemId")
    fun deleteShopItem(itemId: Int)

    @Query("SELECT * FROM shop_items WHERE id = :itemId")
    fun getShopItem(itemId: Int): ShopItemDbModel
}