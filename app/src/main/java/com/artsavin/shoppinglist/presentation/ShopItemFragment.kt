package com.artsavin.shoppinglist.presentation

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.databinding.FragmentShopItemBinding
import com.artsavin.shoppinglist.domain.ShopItem
import javax.inject.Inject

class ShopItemFragment: Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel: ShopItemViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShopItemViewModel::class.java]
    }

    private lateinit var onCloseFragmentListener: OnCloseFragmentListener

    private var screenMode: String = UNKNOWN_MODE
    private var shopItemId: Int = ShopItem.UNDEFINED_ID

    private var _binding: FragmentShopItemBinding? = null
    private val binding: FragmentShopItemBinding
        get() = _binding ?: throw RuntimeException("FragmentShopItemBinding == null")

    private val component by lazy {
        (requireActivity().application as ShoppingListApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseFragmentArgs()
    }

    override fun onAttach(context: Context) {
        component.inject(this)
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
    ): View {
        _binding = FragmentShopItemBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        setListeners()
        launchRightScreen()
        observeViewModel()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun observeViewModel() {

        viewModel.activityDone.observe(viewLifecycleOwner) {
            onCloseFragmentListener.onCloseFragment()
        }
    }

    private fun launchRightScreen() {
        when (screenMode) {
            EDIT_MODE -> launchEditMode()
            ADD_MODE -> launchAddMode()
        }
    }

    private fun launchAddMode() {
        activity?.apply {
            title = getString(R.string.add_title)
        }
        binding.fabSave.setOnClickListener {
            viewModel.addShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun launchEditMode() {
        activity?.apply {
            title = getString(R.string.edit_title)
        }
        viewModel.getShopItem(shopItemId)
        binding.fabSave.setOnClickListener {
            viewModel.editShopItem(
                binding.etName.text?.toString(),
                binding.etCount.text?.toString()
            )
        }
    }

    private fun setListeners() {

        binding.etName.doOnTextChanged {
                text, start, before, count  -> viewModel.resetErrorInputName()
        }

        binding.etCount.doOnTextChanged {
                text, start, before, count -> viewModel.resetErrorInputCount()
        }

        binding.fabCancel.setOnClickListener {
            viewModel.activityDone()
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
