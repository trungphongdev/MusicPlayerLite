package com.example.musicplayerlite.di

import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.lazyModule

@OptIn(KoinExperimentalAPI::class)
val koinAppModule = lazyModule {
    includes(serviceModule, repositoryModule, viewModelModule, dataStoreModule, mediaModule)
}
