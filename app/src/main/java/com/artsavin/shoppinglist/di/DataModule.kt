package com.artsavin.shoppinglist.di

import android.app.Application
import com.artsavin.shoppinglist.data.AppDataBase
import com.artsavin.shoppinglist.data.ShopListDao
import com.artsavin.shoppinglist.data.ShopListRepositoryImpl
import com.artsavin.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @ApplicationScope
    @Binds
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @ApplicationScope
        @Provides
        fun provideShopListDao(application: Application): ShopListDao {
            return AppDataBase.getInstance(application).shopListDao()
        }
    }
}