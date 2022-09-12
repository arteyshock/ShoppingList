package com.artsavin.shoppinglist.presentation

import android.app.Application
import com.artsavin.shoppinglist.di.DaggerAppComponent

class ShoppingListApplication: Application() {

    val component by lazy {

        DaggerAppComponent.factory().create(this)
    }

    override fun onCreate() {
        super.onCreate()
    }
}