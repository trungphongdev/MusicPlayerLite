package com.example.musicplayerlite.di

import com.example.musicplayerlite.service.MusicService
import org.koin.core.module.dsl.createdAtStart
import org.koin.core.module.dsl.named
import org.koin.core.module.dsl.withOptions
import org.koin.dsl.module

val serviceModule = module(createdAtStart = true) {
    single { MusicService() } withOptions {
        named("service music")
        createdAtStart()
    }
}