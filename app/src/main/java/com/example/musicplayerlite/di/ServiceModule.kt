package com.example.musicplayerlite.di

import com.example.musicplayerlite.service.MusicService
import org.koin.dsl.module

val serviceModule = module(createdAtStart=true) {
    single { MusicService() }
}