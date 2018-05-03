package com.mannysight.wire2win2.di.modules

import android.app.Application
import android.arch.persistence.room.Room
import com.mannysight.wire2win2.data.database.AppDatabase
import com.mannysight.wire2win2.data.database.CryptocurrenciesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule(val app: Application) {

    @Provides
    @Singleton
    fun provideApplication(): Application = app

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase = Room.databaseBuilder(app,
            AppDatabase::class.java, "app_db").build()

    @Provides
    @Singleton
    fun provideCryptocurrenciesDao(database: AppDatabase): CryptocurrenciesDao = database.cryptoCurrenciesDao()


}