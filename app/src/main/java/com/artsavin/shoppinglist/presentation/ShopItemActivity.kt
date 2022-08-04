package com.artsavin.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem

class ShopItemActivity : AppCompatActivity(), OnCloseFragmentListener {

    private var screenMode: String = UNKNOWN_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)
        parseIntent()
        if (savedInstanceState == null) {
            launchWrightMode()
        }
    }

    override fun onCloseFragment() {
        Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun parseIntent() {

        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("Screen mode is absent")
        }

        screenMode = intent.getStringExtra(EXTRA_SCREEN_MODE).toString()

        if (screenMode != EDIT_MODE && screenMode != ADD_MODE) {
            throw RuntimeException("Unknown screen mode")
        }

        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw RuntimeException("Shop item ID is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }
    }

    private fun launchWrightMode() {
        var fragment: ShopItemFragment
        when (screenMode) {
            EDIT_MODE -> {
                title = getString(R.string.edit_title)
                fragment = ShopItemFragment.newInstanceEditItem(shopItemId)
            }
            ADD_MODE -> {
                title = getString(R.string.add_title)
                fragment = ShopItemFragment.newInstanceAddItem()
            }
            else -> throw RuntimeException("Shop item ID is absent")
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "EXTRA_MODE"
        private const val EXTRA_ITEM_ID = "EXTRA_ITEM_ID"
        private const val EDIT_MODE = "EDIT_MODE"
        private const val ADD_MODE = "ADD_MODE"
        private const val UNKNOWN_MODE = ""

        fun newIntentAddItem(context: Context): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, ADD_MODE)
            return intent
        }

        fun newIntentEditItem(context: Context, itemId: Int): Intent {
            val intent = Intent(context, ShopItemActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, EDIT_MODE)
            intent.putExtra(EXTRA_ITEM_ID, itemId)
            return intent
        }
    }
}