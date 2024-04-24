package com.example.musicplayerlite.di

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
import com.example.musicplayerlite.screen.playlist.SongViewModel

val viewModelModule =  module {
    viewModelOf(::SongViewModel)
}