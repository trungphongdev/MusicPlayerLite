package com.example.musicplayerlite.model

import android.content.Context
import android.net.Uri
import com.example.musicplayerlite.R
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encodeToString
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json

@Serializable
data class Song(
    val id: String,
    val artistId: Long,
    val albumId: Long,
    @Serializable(with = UriAsStringSerializer::class)
    val mediaUri: Uri,
    @Serializable(with = ArtWorkUriAsStringSerializer::class)
    val artworkUri: Uri,
    val title: String,
    val artist: String,
    val album: String,
    val folder: String,
    val duration: Long,
    val date: Long,
    val isFavorite: Boolean,
) {
    fun getNameArtist(context: Context): String {
        return artist
            .takeUnless { it == context.getString(R.string.unknown) }
            ?: context.getString(R.string.unknown_artist)
    }
}


object UriAsStringSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("mediaUri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString("")
    }

    override fun deserialize(decoder: Decoder): Uri {
        return Uri.parse(decoder.decodeString())
    }
}

object ArtWorkUriAsStringSerializer : KSerializer<Uri> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("artworkUri", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: Uri) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): Uri {
        return Uri.parse(decoder.decodeString())
    }
}

fun main() {
    val b = Json.encodeToString(A())
    println(b)
    val convert = Json.decodeFromString<A>(b)
    println(convert)
}
@Serializable
data class A(val name: String = "Phong", val b: B = B())
@Serializable
data class B(val age: Int = 4)