package com.example.musicplayerlite.di

import com.example.musicplayerlite.screen.playlist.SongViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule =  module {
    viewModelOf(::SongViewModel)
}