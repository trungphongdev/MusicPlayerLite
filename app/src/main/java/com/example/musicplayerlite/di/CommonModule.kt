package com.example.musicplayerlite.di

import com.example.musicplayerlite.permission.PermissionHelper
import org.koin.dsl.module

val commonModule = module {
    factory { PermissionHelper() }
}