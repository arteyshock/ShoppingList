package com.artsavin.shoppinglist.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.artsavin.shoppinglist.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ShopItemActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        val buttonCancel = findViewById<FloatingActionButton>(R.id.fab_cancel)
        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }
}