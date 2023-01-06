package com.example.spotifyclone.data.encoder

fun interface Base64Encoder {
    fun encodeToString(input: ByteArray): String
}