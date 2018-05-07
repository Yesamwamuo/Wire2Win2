package com.mannysight.wire2win2.ui

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.SyncStateContract
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.mannysight.wire2win2.R
import com.mannysight.wire2win2.data.model.Cryptocurrency
import com.mannysight.wire2win2.data.remote.ApiClient
import com.mannysight.wire2win2.data.remote.WebService
import com.mannysight.wire2win2.utils.AppConstants
import dagger.android.AndroidInjection
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cryptocurrenciesViewModelFactory: ViewModelFactory
    var cryptocurrenciesAdapter = CrytoCurrencyAdapter(ArrayList())
    lateinit var cryptocurrenciesViewModel: CryptocurrencyViewModel
    var currentPage = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AndroidInjection.inject(this)

        initializeRecycler()

        cryptocurrenciesViewModel = ViewModelProviders.of(this, cryptocurrenciesViewModelFactory).get(
                CryptocurrencyViewModel::class.java)


        progressBar.visibility = View.VISIBLE
        loadData()

        cryptocurrenciesViewModel.cryptocurrenciesResult().observe(this,
                Observer<List<Cryptocurrency>> {
                    if (it != null) {
                        val position = cryptocurrenciesAdapter.itemCount
                        cryptocurrenciesAdapter.addCryptocurrencies(it)
                        recycler.adapter = cryptocurrenciesAdapter
                        recycler.scrollToPosition(position - AppConstants.LIST_SCROLLING)
                    }
                })

        cryptocurrenciesViewModel.cryptocurrenciesError().observe(this, Observer<String> {
            if (it != null) {
                Toast.makeText(this, resources.getString(R.string.cryptocurrency_error_message) + it,
                        Toast.LENGTH_SHORT).show()
            }
        })

        cryptocurrenciesViewModel.cryptocurrenciesLoader().observe(this, Observer<Boolean> {
            if (it == false) progressBar.visibility = View.GONE
        })
//        showCryptocurrencies()
    }


    private fun initializeRecycler() {
        val gridLayoutManager = GridLayoutManager(this, 1)
        gridLayoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler.apply {
            setHasFixedSize(true)
            layoutManager = gridLayoutManager
            addOnScrollListener(InfiniteScrollListener({ loadData() }, gridLayoutManager))
        }
    }

    fun loadData() {
        cryptocurrenciesViewModel.loadCryptocurrencies(AppConstants.LIMIT, currentPage * AppConstants.OFFSET)
        currentPage++
    }


    override fun onDestroy() {
        cryptocurrenciesViewModel.disposeElements()
        super.onDestroy()
    }


//    val compositeDisposable = CompositeDisposable()
//
//    private fun showCryptocurrencies() {
//        val cryptocurrenciesResponse = getCryptocurrencies()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//
//        val disposableObserver =
//                cryptocurrenciesResponse.subscribeWith(object : DisposableObserver<List<Cryptocurrency>>() {
//                    override fun onComplete() {
//                    }
//
//                    override fun onNext(cryptocurrencies: List<Cryptocurrency>) {
//                        val listSize = cryptocurrencies.size
//                        Log.e("ITEMS **** ", listSize.toString())
//                    }
//
//                    override fun onError(e: Throwable) {
//                        Log.e("ERROR *** ", e.message)
//                    }
//
//                })
//
//        compositeDisposable.addAll(disposableObserver)
//
//    }
//
//    private fun getCryptocurrencies(): Observable<List<Cryptocurrency>> {
//        val retrofit = ApiClient.getClient()
//        val apiInterface = retrofit.create(WebService::class.java)
//        return apiInterface.getCryptocurrencies("0")
//    }
//
//    override fun onDestroy() {
//        compositeDisposable.dispose()
//        super.onDestroy()
//    }
}
