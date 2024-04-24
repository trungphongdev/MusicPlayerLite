package com.example.musicplayerlite.model

data class Artist(
    val id: Long,
    val name: String,
    val songs: List<Song>
)