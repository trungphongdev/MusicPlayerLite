package com.example.musicplayerlite.di

import com.example.musicplayerlite.datasource.MusicMediaDataSource
import com.example.musicplayerlite.repository.IMediaRepository
import com.example.musicplayerlite.repository.MediaRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.bind
import org.koin.dsl.module

val repositoryModule = module {
    single { MediaRepositoryImpl(get()) } bind IMediaRepository::class
    single { MusicMediaDataSource(get()) }
    single { androidApplication().contentResolver }
}