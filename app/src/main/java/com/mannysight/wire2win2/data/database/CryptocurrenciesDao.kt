package com.mannysight.wire2win2.data.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query
import com.mannysight.wire2win2.data.model.Cryptocurrency

@Dao
interface CryptocurrenciesDao {

    @Query("SELECT * FROM cryptocurrency")
    fun queryCryptoCurrencies(): LiveData<List<Cryptocurrency>>

    @Insert(
            onConflict = OnConflictStrategy.REPLACE
    )
    fun insertCryptoCurrency(cryptoCurrency: Cryptocurrency)
}