package com.artsavin.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ShopListDao {

    @Query("SELECT * FROM shop_items ORDER BY enabled DESC")
    fun getShopList(): LiveData<List<ShopItemDbModel>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addShopItem(shopItemDbModel: ShopItemDbModel)

    @Query("DELETE FROM shop_items WHERE id = :itemId")
    suspend fun deleteShopItem(itemId: Int)

    @Query("SELECT * FROM shop_items WHERE id = :itemId")
    suspend fun getShopItem(itemId: Int): ShopItemDbModel
}