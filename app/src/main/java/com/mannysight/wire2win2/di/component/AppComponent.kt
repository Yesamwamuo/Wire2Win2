package com.mannysight.wire2win2.di.component

import android.app.Application
import com.mannysight.wire2win2.Wired2Win
import com.mannysight.wire2win2.di.modules.AppModule
import com.mannysight.wire2win2.di.modules.BuildersModule
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = arrayOf(AndroidInjectionModule::class, BuildersModule::class, AppModule::class)
)
interface AppComponent {

    fun inject(app: Application)
}