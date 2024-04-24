package com.example.musicplayerlite.model

import com.example.musicplayerlite.common.Const
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
data class MusicState(
    val currentPos: Int = Const.NO_POSITION,
    //@Serializable(with = SongAsStringSerializer::class)
    val song: Song? = null,
    val isPlaying: Boolean = false,
    val duration: Int = 0,
)


@Serializable
object SongAsStringSerializer : KSerializer<Song> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("Song", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Song) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Song {
        val string = decoder.decodeString()
        return Json.decodeFromString<Song>(string)
    }
}