package com.example.musicplayerlite

import android.app.Application
import com.example.musicplayerlite.di.koinAppModule
import kotlinx.coroutines.MainScope
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {
    val applicationScope = MainScope()
    companion object {
        lateinit var instance: App
    }

    override fun onCreate() {
        super.onCreate()
        instance = this@App
        startKoin {
            androidContext(this@App)
            androidLogger()
            modules(koinAppModule)
        }
    }
}