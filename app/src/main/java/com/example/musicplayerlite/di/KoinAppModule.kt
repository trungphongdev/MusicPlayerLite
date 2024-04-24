package com.example.musicplayerlite.di

import org.koin.dsl.module

val koinAppModule = module {
    includes(serviceModule, repositoryModule, viewModelModule, dataStoreModule)
}
