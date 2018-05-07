package com.mannysight.wire2win2.ui

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.mannysight.wire2win2.data.CryptocurrencyRepository
import com.mannysight.wire2win2.data.model.Cryptocurrency
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit.MILLISECONDS
import javax.inject.Inject

class CryptocurrencyViewModel @Inject constructor(
        private val cryptocurrencyRepository: CryptocurrencyRepository) : ViewModel() {

    var cryptocurrenciesResult: MutableLiveData<List<Cryptocurrency>> = MutableLiveData()
    var cryptocurrenciesError: MutableLiveData<String> = MutableLiveData()
    lateinit var disposableObserver: DisposableObserver<List<Cryptocurrency>>
    var cryptocurrenciesLoader: MutableLiveData<Boolean> = MutableLiveData()

    fun cryptocurrenciesResult(): LiveData<List<Cryptocurrency>> {
        return cryptocurrenciesResult
    }
    fun cryptocurrenciesLoader(): LiveData<Boolean> {
        return cryptocurrenciesLoader
    }
    fun cryptocurrenciesError(): LiveData<String> {
        return cryptocurrenciesError
    }


    fun loadCryptocurrencies(limit: Int, offset: Int ) {

        disposableObserver = object : DisposableObserver<List<Cryptocurrency>>() {
            override fun onComplete() {

            }

            override fun onNext(cryptocurrencies: List<Cryptocurrency>) {
                cryptocurrenciesResult.postValue(cryptocurrencies)
                cryptocurrenciesLoader.postValue(false)
            }

            override fun onError(e: Throwable) {
                cryptocurrenciesError.postValue(e.message)
                cryptocurrenciesLoader.postValue(false)
            }
        }

        cryptocurrencyRepository.getCryptocurrencies(limit, offset)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .debounce(400, MILLISECONDS)
                .subscribe(disposableObserver)
    }

    fun disposeElements() {
        if (null != disposableObserver && !disposableObserver.isDisposed) disposableObserver.dispose()
    }

}