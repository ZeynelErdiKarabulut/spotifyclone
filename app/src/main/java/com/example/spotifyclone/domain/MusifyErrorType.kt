package com.example.spotifyclone.domain

import retrofit2.HttpException

/**
 * An enum that contains different error types associated with [HttpException.code].
 */
enum class spotifycloneErrorType {
    BAD_OR_EXPIRED_TOKEN,
    BAD_OAUTH_REQUEST,
    INVALID_REQUEST,
    RATE_LIMIT_EXCEEDED,
    UNKNOWN_ERROR,
    NETWORK_CONNECTION_FAILURE,
    RESOURCE_NOT_FOUND,
    DESERIALIZATION_ERROR
}

/**
 * An extension property on [retrofit2.HttpException] that indicates
 * the [spotifycloneErrorType] associated with the [retrofit2.HttpException.code]
 */
fun HttpException.getAssociatedspotifycloneErrorType(): spotifycloneErrorType =
    when (this.code()) {
        400 -> spotifycloneErrorType.INVALID_REQUEST
        401 -> spotifycloneErrorType.BAD_OR_EXPIRED_TOKEN
        403 -> spotifycloneErrorType.BAD_OAUTH_REQUEST
        429 -> spotifycloneErrorType.RATE_LIMIT_EXCEEDED
        404 -> spotifycloneErrorType.RESOURCE_NOT_FOUND
        else -> spotifycloneErrorType.UNKNOWN_ERROR
    }