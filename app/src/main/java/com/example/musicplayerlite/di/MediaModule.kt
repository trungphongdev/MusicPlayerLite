package com.example.musicplayerlite.di

import com.example.musicplayerlite.media.IMusicPlayerController
import com.example.musicplayerlite.media.MusicManager
import com.example.musicplayerlite.media.MusicPlayerController
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.core.qualifier.named
import org.koin.dsl.module

val mediaModule = module {
    factory { MusicManager(androidApplication()) }
    single<IMusicPlayerController> {
        MusicPlayerController(
            get(),
            get<CoroutineScope>(named("coroutine_media"))
        )
    }
    single<CoroutineScope> { CoroutineScope(Dispatchers.IO + SupervisorJob() + CoroutineExceptionHandler{ _, _ -> }) } withOptions {
        named("coroutine_media")
    }
}