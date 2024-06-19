package com.example.musicplayerlite.di

import com.example.musicplayerlite.media.MusicPlayerController
import com.example.musicplayerlite.media.MusicPlayerManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val mediaModule = module {
    factory { MusicPlayerManager(androidApplication()) }
    factory { MusicPlayerController(get(),get(), get()) }
    factory { CoroutineScope(Dispatchers.IO + SupervisorJob())}
}