package com.example.musicplayerlite

import android.app.Application
import com.example.musicplayerlite.di.koinAppModule
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.core.context.startKoin
import org.koin.core.lazyModules

class App : Application() {
    val applicationScope = MainScope()
    companion object {
        lateinit var instance: App
    }

    @OptIn(KoinExperimentalAPI::class)
    override fun onCreate() {
        super.onCreate()
        instance = this@App
        startKoin {
            androidLogger()
            androidContext(this@App)
            lazyModules(koinAppModule)
        }
    }
}