package com.example.spotifyclone.data.encoder

import android.util.Base64
import javax.inject.Inject

/**
 * This is a [Base64Encoder] that can be used for converting a byte
 * array to a base64 encoded string.This class uses [android.util.Base64]
 * for encoding.
 * [java.util.Base64.getEncoder] can only be used if the minimum api is
 * at least 26 (Android Oreo). Therefore, this encoder uses
 * [android.util.Base64] for encoding.
 * Note: The value generated by [android.util.Base64.encodeToString] is
 * equivalent to [java.util.Base64] only when [android.util.Base64.NO_WRAP]
 * is used in conjunction with [android.util.Base64.encodeToString].
 */
class AndroidBase64Encoder @Inject constructor() : Base64Encoder {
    override fun encodeToString(
        input: ByteArray
    ): String = Base64.encodeToString(input, Base64.NO_WRAP)
}