package com.artsavin.shoppinglist.presentation

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.ViewModelProvider
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemActivity : AppCompatActivity() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var buttonCancel: FloatingActionButton
    private lateinit var buttonSave: FloatingActionButton

    private var screenMode = UNKNOWN_MODE
    private var shopItemId = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shop_item)

        parseIntent()
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews()
        launchWrightScreen()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(this) {
            val message = if (it) {
                getString(R.string.error_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.errorInputCount.observe(this) {
            val message = if (it) {
                getString(R.string.error_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.activityDone.observe(this) {
            finish()
        }
    }

    private fun launchWrightScreen() {
        when (screenMode) {
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        title = getString(R.string.add_title)
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        title = getString(R.string.edit_title)
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(this) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun initViews() {
        tilName = findViewById(R.id.til_name)
        tilCount = findViewById(R.id.til_count)
        etName = findViewById(R.id.et_name)
        etCount = findViewById(R.id.et_count)
        buttonCancel = findViewById(R.id.fab_cancel)
        buttonSave = findViewById(R.id.fab_save)

        etName.doOnTextChanged {
                text, start, count, after -> viewModel.resetErrorInputName()
        }

        etCount.doOnTextChanged {
                text, start, count, after -> viewModel.resetErrorInputCount()
        }

        buttonCancel.setOnClickListener {
            onBackPressed()
        }
    }

    private fun parseIntent() {

        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw RuntimeException("ShopItem Activity mode is undefined")
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)

        if (mode != ADD_MODE && mode != EDIT_MODE) {
            throw RuntimeException("ShopItem Activity mode is unknown")
        }

        screenMode = mode

        if (screenMode == EDIT_MODE) {
            if (!intent.hasExtra(EXTRA_ITEM_ID)) {
                throw RuntimeException("ShopItem id mode is absent")
            }
            shopItemId = intent.getIntExtra(EXTRA_ITEM_ID, ShopItem.UNDEFINED_ID)
        }

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