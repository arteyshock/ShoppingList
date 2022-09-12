package com.artsavin.shoppinglist.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.artsavin.shoppinglist.R
import com.artsavin.shoppinglist.databinding.ActivityMainBinding
import javax.inject.Inject

class MainActivity : AppCompatActivity(), OnCloseFragmentListener {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    private val viewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]
    }

    private val shopListAdapter: ShopListAdapter by lazy {
        ShopListAdapter()
    }

    private val component by lazy {
        (application as ShoppingListApplication).component
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupRecyclerView()
        setupAddButton()
        viewModel.shopList.observe(this) {
            shopListAdapter.submitList(it)
        }

    }

    override fun onCloseFragment() {
        supportFragmentManager.popBackStack()
        title = getString(R.string.app_name)
    }

    private fun isOnePaneMode(): Boolean {
        // этот контейнер есть только в пейзажной ориентации
        // если он null, то мы находимся в портретной ориентации
        return binding.shopItemContainer == null
    }

    private fun launchShopItemFragment(fragment: ShopItemFragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.shop_item_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    private fun setupAddButton() {
        binding.fabAddItem.setOnClickListener {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentAddItem(this)
                startActivity(intent)
            } else {
                launchShopItemFragment(ShopItemFragment.newInstanceAddItem())
            }
        }
    }

    private fun setupRecyclerView() {

        with(binding.rvShopList) {
            adapter = shopListAdapter
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.ENABLED_ITEM_TYPE,
                ShopListAdapter.MAX_POOL_SIZE
            )
            recycledViewPool.setMaxRecycledViews(
                ShopListAdapter.DISABLED_ITEM_TYPE,
                ShopListAdapter.MAX_POOL_SIZE
            )
        }
        setupOnClickListener()
        setupOnLongClickListener()
        setupOnSwipeListener(binding.rvShopList)

    }

    private fun setupOnSwipeListener(rvShopList: RecyclerView) {
        // обработчик свайпа
        val swipeCallback = object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val shopItem = shopListAdapter.currentList[viewHolder.adapterPosition]
                viewModel.deleteShopItem(shopItem)
            }

        }
        // создаем хелпер с коллбэком
        val itemTouchHelper = ItemTouchHelper(swipeCallback)
        // прикрепляем хелпер к ресайклер вью
        itemTouchHelper.attachToRecyclerView(rvShopList)
    }

    private fun setupOnLongClickListener() {
        shopListAdapter.onShopItemLongClickListener = {
            if (isOnePaneMode()) {
                val intent = ShopItemActivity.newIntentEditItem(this, it.id)
                startActivity(intent)
            } else {
                launchShopItemFragment(ShopItemFragment.newInstanceEditItem(it.id))
            }
        }
    }

    private fun setupOnClickListener() {
        shopListAdapter.onShopItemClickListener = { viewModel.toggleShopItem(it) }
    }
}