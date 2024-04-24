package com.example.musicplayerlite.di

import com.example.musicplayerlite.repository.IMediaRepository
import com.example.musicplayerlite.repository.MediaRepositoryImpl
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.example.musicplayerlite.datasource.MusicMediaDataSource
import org.koin.android.ext.koin.androidContext

val repositoryModule = module {
    singleOf(::MediaRepositoryImpl){ bind<IMediaRepository>() }
    singleOf(::MusicMediaDataSource)
    single {
        androidContext().contentResolver
    }

}