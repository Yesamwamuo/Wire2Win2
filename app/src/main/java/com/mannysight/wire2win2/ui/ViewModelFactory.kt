package com.mannysight.wire2win2.ui

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import javax.inject.Inject

class ViewModelFactory @Inject constructor(
        private val cryptocurrenciesViewModel: CryptocurrencyViewModel) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CryptocurrencyViewModel::class.java!!)) {
            return cryptocurrenciesViewModel as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}