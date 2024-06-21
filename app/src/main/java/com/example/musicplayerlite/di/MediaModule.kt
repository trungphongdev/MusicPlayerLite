package com.example.musicplayerlite.di

import com.example.musicplayerlite.media.IMusicPlayerController
import com.example.musicplayerlite.media.MusicPlayerController
import com.example.musicplayerlite.media.MusicPlayerManager
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val mediaModule = module {
    factory { MusicPlayerManager(androidApplication()) }
    single<IMusicPlayerController> { MusicPlayerController(androidApplication(), get())}
}