package com.artsavin.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem


class ShopListAdapter: ListAdapter<ShopItem, ShopItemViewHolder>(ShopItemDiffCallback()) {

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopItemViewHolder {

        val layoutId = if (viewType == ENABLED_ITEM_TYPE) {
            R.layout.item_enabled
        } else {
            R.layout.item_disabled
        }

        val view = LayoutInflater.from(parent.context).inflate(
            layoutId,
            parent,
            false
        )
        return ShopItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ShopItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.tvName.text = item.name
        holder.tvCount.text = item.count.toString()
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        if (item.enabled) {
            return ENABLED_ITEM_TYPE
        }
        return DISABLED_ITEM_TYPE
    }

    companion object {
        const val ENABLED_ITEM_TYPE = 101
        const val DISABLED_ITEM_TYPE = 100
        const val MAX_POOL_SIZE = 15
    }
}