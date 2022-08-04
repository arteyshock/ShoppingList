package com.artsavin.shoppinglist.presentation

import android.text.Editable
import androidx.databinding.BindingAdapter
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("nameError")
fun bindNameError(til: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        til.context.getString(R.string.error_name)
    } else {
        null
    }
    til.error = message
}

@BindingAdapter("countError")
fun bindCountError(til: TextInputLayout, isError: Boolean) {
    val message = if (isError) {
        til.context.getString(R.string.error_count)
    } else {
        null
    }
    til.error = message
}

@BindingAdapter("shopItemName")
fun bindShopItemName(textEdit: TextInputEditText, name: String?) {
    name?.let {
        textEdit.setText(name)
    }
}

@BindingAdapter("shopItemCount")
fun bindShopItemCount(textEdit: TextInputEditText, count: Int?) {
    count?.let {
        textEdit.setText(count.toString())
    }
}