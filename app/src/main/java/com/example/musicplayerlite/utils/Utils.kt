package com.example.musicplayerlite.utils

import android.os.Build

object Utils {
    fun isAndroidS() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
}