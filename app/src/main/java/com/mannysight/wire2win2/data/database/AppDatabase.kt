package com.mannysight.wire2win2.data.database

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.mannysight.wire2win2.data.model.Cryptocurrency

@Database(entities = arrayOf(Cryptocurrency::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun cryptoCurrenciesDao(): CryptocurrenciesDao
}