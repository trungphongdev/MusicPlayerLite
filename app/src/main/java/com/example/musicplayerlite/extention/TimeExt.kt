package com.example.musicplayerlite.extention

import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun Long?.toWholeSeconds(): Int {
    return (this ?: 0L).toDuration(DurationUnit.MILLISECONDS).inWholeSeconds.toInt()
}