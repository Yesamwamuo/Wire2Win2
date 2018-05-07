package com.mannysight.wire2win2.data

import android.util.Log
import com.mannysight.wire2win2.data.database.CryptocurrenciesDao
import com.mannysight.wire2win2.data.model.Cryptocurrency
import com.mannysight.wire2win2.data.remote.WebService
import com.mannysight.wire2win2.utils.Utils
import io.reactivex.Observable
import javax.inject.Inject

class CryptocurrencyRepository @Inject constructor(val apiInterface: WebService,
                                                   val cryptocurrenciesDao: CryptocurrenciesDao, val utils: Utils) {


    fun getCryptocurrencies(limit: Int, offset: Int): Observable<List<Cryptocurrency>> {
        val hasConnection = utils.isConnectedToInternet()
        var observableFromApi: Observable<List<Cryptocurrency>>? = null
        if (hasConnection){
            observableFromApi = getCryptocurrenciesFromApi()
        }
        val observableFromDb = getCryptocurrenciesFromDb(limit, offset)

        return if (hasConnection) Observable.concatArrayEager(observableFromApi, observableFromDb)
        else observableFromDb
    }


    fun getCryptocurrenciesFromApi(): Observable<List<Cryptocurrency>> {
        return apiInterface.getCryptocurrencies("0")
                .doOnNext {
                    Log.e("REPOSITORY API * ", it.size.toString())
                    for (item in it) {
                        cryptocurrenciesDao.insertCryptocurrency(item)
                    }
                }
    }

    fun getCryptocurrenciesFromDb(limit: Int, offset: Int): Observable<List<Cryptocurrency>> {
        return cryptocurrenciesDao.queryCryptocurrencies(limit, offset)
                .toObservable()
                .doOnNext {
                    //Print log it.size :)
                    Log.e("REPOSITORY DB *** ", it.size.toString())
                }
    }
}