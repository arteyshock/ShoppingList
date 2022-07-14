package com.artsavin.shoppinglist.domain

data class ShopItem(
    private val name: String,
    private val quantity: Int,
    private val enabled: Boolean,
    var id: Int = UNDEFINED_ID
) {
    companion object {
        const val UNDEFINED_ID = -1
    }
}