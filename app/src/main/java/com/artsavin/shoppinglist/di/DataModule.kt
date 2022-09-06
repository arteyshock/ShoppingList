package com.artsavin.shoppinglist.di

import com.artsavin.shoppinglist.data.ShopListRepositoryImpl
import com.artsavin.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module

@Module
interface DataModule {

    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository
}