package com.example.musicplayerlite.di

import com.example.musicplayerlite.datastore.IMusicDataStore
import com.example.musicplayerlite.datastore.MusicDataStore
import com.example.musicplayerlite.datastore.dataStore
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

val dataStoreModule = module {
    single { MusicDataStore(get())} bind IMusicDataStore::class
    single { androidApplication().dataStore }
}
