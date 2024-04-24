package com.example.musicplayerlite.extentions

import android.content.ContentUris
import android.net.Uri
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.io.File

fun String.asFolder() = File(this).parentFile?.name.orEmpty()

fun Long.asArtworkUri() = ContentUris.withAppendedId(Uri.parse(ALBUM_ART_URI), this)

private const val ALBUM_ART_URI = "content://media/external/audio/albumart"


fun Long.toLocalDateTime(): LocalDateTime {
    return Instant.fromEpochSeconds(this).toLocalDateTime(TimeZone.currentSystemDefault())
}

