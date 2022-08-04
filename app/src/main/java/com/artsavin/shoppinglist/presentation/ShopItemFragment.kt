package com.artsavin.shoppinglist.presentation

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.domain.ShopItem
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class ShopItemFragment: Fragment() {

    private lateinit var viewModel: ShopItemViewModel

    private lateinit var onCloseFragmentListener: OnCloseFragmentListener

    private lateinit var tilName: TextInputLayout
    private lateinit var tilCount: TextInputLayout
    private lateinit var etName: TextInputEditText
    private lateinit var etCount: TextInputEditText
    private lateinit var buttonCancel: FloatingActionButton
    private lateinit var buttonSave: FloatingActionButton

    private var screenMode: String = UNKNOWN_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseFragmentArgs()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnCloseFragmentListener) {
            onCloseFragmentListener = context
        } else {
            throw RuntimeException("Activity must implement OnCloseFragmentListener")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_shop_item, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(ShopItemViewModel::class.java)
        initViews(view)
        launchWrightScreen()
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.errorInputName.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_name)
            } else {
                null
            }
            tilName.error = message
        }

        viewModel.errorInputCount.observe(viewLifecycleOwner) {
            val message = if (it) {
                getString(R.string.error_count)
            } else {
                null
            }
            tilCount.error = message
        }

        viewModel.activityDone.observe(viewLifecycleOwner) {
            onCloseFragmentListener.onCloseFragment()
        }
    }

    private fun launchWrightScreen() {
        when (screenMode) {
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        activity?.title = getString(R.string.add_title)
        buttonSave.setOnClickListener {
            viewModel.addShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun launchEditMode() {
        activity?.title = getString(R.string.edit_title)
        viewModel.getShopItem(shopItemId)
        viewModel.shopItem.observe(viewLifecycleOwner) {
            etName.setText(it.name)
            etCount.setText(it.count.toString())
        }
        buttonSave.setOnClickListener {
            viewModel.editShopItem(etName.text?.toString(), etCount.text?.toString())
        }
    }

    private fun initViews(view: View) {
        tilName = view.findViewById(R.id.til_name)
        tilCount = view.findViewById(R.id.til_count)
        etName = view.findViewById(R.id.et_name)
        etCount = view.findViewById(R.id.et_count)
        buttonCancel = view.findViewById(R.id.fab_cancel)
        buttonSave = view.findViewById(R.id.fab_save)

        etName.doOnTextChanged {
                text, start, before, count  -> viewModel.resetErrorInputName()
        }

        etCount.doOnTextChanged {
                text, start, before, count -> viewModel.resetErrorInputCount()
        }

        buttonCancel.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun parseFragmentArgs() {
        val args = requireArguments()
        if (!args.containsKey(SCREEN_MODE)) {
            throw RuntimeException("Screen mode is absent")
        }
        val mode = args.getString(SCREEN_MODE)
        if (mode != EDIT_MODE && mode != ADD_MODE) {
            throw RuntimeException("Unknown screen mode $mode")
        }

        screenMode = mode

        if (screenMode == EDIT_MODE) {
            if (!args.containsKey(ITEM_ID)) {
                throw RuntimeException("Shop item ID is absent")
            }
            shopItemId = args.getInt(ITEM_ID)
        }

    }

    companion object {
        private const val SCREEN_MODE = "EXTRA_MODE"
        private const val ITEM_ID = "EXTRA_ITEM_ID"
        private const val EDIT_MODE = "EDIT_MODE"
        private const val ADD_MODE = "ADD_MODE"
        private const val UNKNOWN_MODE = ""

        fun newInstanceAddItem(): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, ADD_MODE)
                }
            }
        }

        fun newInstanceEditItem(itemId: Int): ShopItemFragment {
            return ShopItemFragment().apply {
                arguments = Bundle().apply {
                    putString(SCREEN_MODE, EDIT_MODE)
                    putInt(ITEM_ID, itemId)
                }
            }
        }
    }
}

interface OnCloseFragmentListener {

    fun onCloseFragment()
}
