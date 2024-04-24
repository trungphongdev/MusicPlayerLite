package com.example.musicplayerlite.di

import com.example.musicplayerlite.datastore.dataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.datastore.MusicDataStore
import org.koin.core.module.dsl.bind

val dataStoreModule = module {
    singleOf(::MusicDataStore) { bind<IMusicDataStore>()}
    single { androidApplication().dataStore }
}
