package com.mannysight.wire2win2.ui

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.mannysight.wire2win2.R
import dagger.android.AndroidInjection

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidInjection.inject(this)
    }
}
