package com.artsavin.shoppinglist.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem


class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopItemViewHolder>() {

    var itemList = listOf<ShopItem>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    var onShopItemClickListener: ((ShopItem) -> Unit)? = null
    var onShopItemLongClickListener: ((ShopItem) -> Unit)? = null

    class ShopItemViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        val tvName = view.findViewById<TextView>(R.id.tv_name)
        val tvCount = view.findViewById<TextView>(R.id.tv_count)
    }

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
        val item = itemList[position]
        holder.tvName.text = item.name
        holder.tvCount.text = item.quantity.toString()
        holder.view.setOnClickListener {
            onShopItemClickListener?.invoke(item)
        }
        holder.view.setOnLongClickListener {
            onShopItemLongClickListener?.invoke(item)
            true
        }
    }

    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        val item = itemList[position]
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