package com.example.musicplayerlite.datastore

import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey

val FAVOURITE_SONGS = stringSetPreferencesKey("favourite_songs")
val CURRENT_SONG = stringPreferencesKey("current_song")
