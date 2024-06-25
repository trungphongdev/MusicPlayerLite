package com.example.musicplayerlite.utils

import android.content.ContentResolver
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

fun ContentResolver.observer(uri: Uri) = callbackFlow<Boolean> {
    val observer = object : ContentObserver(null) {
        override fun onChange(selfChange: Boolean) {
            super.onChange(selfChange)
            trySend(selfChange)
        }
    }
    registerContentObserver(uri, true, observer)
    send(true)
    awaitClose {
        unregisterContentObserver(observer)
    }
}