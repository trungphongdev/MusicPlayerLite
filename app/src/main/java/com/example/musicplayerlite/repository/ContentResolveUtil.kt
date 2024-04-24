package com.example.musicplayerlite.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.ContentObserver
import android.net.Uri
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File

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
fun String.asFolder() = File(this).parentFile?.name.orEmpty()

fun Long.asArtworkUri() = ContentUris.withAppendedId(Uri.parse(ALBUM_ART_URI), this)

private const val ALBUM_ART_URI = "content://media/external/audio/albumart"


fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochSeconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

