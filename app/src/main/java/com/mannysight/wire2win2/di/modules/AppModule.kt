package com.mannysight.wire2win2.di.modules

import android.app.Application
import android.arch.lifecycle.ViewModelProvider
import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Room
import android.arch.persistence.room.migration.Migration
import com.mannysight.wire2win2.data.database.AppDatabase
import com.mannysight.wire2win2.data.database.CryptocurrenciesDao
import com.mannysight.wire2win2.data.remote.WebService
import com.mannysight.wire2win2.ui.ViewModelFactory
import com.mannysight.wire2win2.utils.AppConstants
import com.mannysight.wire2win2.utils.Utils
import com.squareup.moshi.KotlinJsonAdapterFactory
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule() {

    companion object {
        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Create the new table
                database.execSQL(
                        "CREATE TABLE users_new (userid TEXT, username TEXT, last_update INTEGER, PRIMARY KEY(userid))");
                // Copy the data
                database.execSQL(
                        "INSERT INTO users_new (userid, username, last_update) SELECT userid, username, last_update FROM users");
                // Remove the old table
                database.execSQL("DROP TABLE users");
                // Change the table name to the correct one
                database.execSQL("ALTER TABLE users_new RENAME TO users");
            }

        }
    }

    @Provides
    @Singleton
    fun provideUtils(app: Application): Utils = Utils(app)

    @Provides
    @Singleton
    fun provideAppDatabase(app: Application): AppDatabase = Room.databaseBuilder(app,
            AppDatabase::class.java, "app_db")
//            .addMigrations(MIGRATION_1_2)
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideCryptocurrenciesDao(database: AppDatabase): CryptocurrenciesDao = database.cryptoCurrenciesDao()

    @Provides
    fun provideCryptocurrenciesViewModelFactory(
            factory: ViewModelFactory): ViewModelProvider.Factory = factory

    @Provides
    @Singleton
    fun providesOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun providesMoshi(): Moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

    @Provides
    @Singleton
    fun providesRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder().client(okHttpClient).baseUrl(AppConstants.BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    @Provides
    @Singleton
    fun providesApiInterface(retrofit: Retrofit): WebService = retrofit.create(
            WebService::class.java)
}
