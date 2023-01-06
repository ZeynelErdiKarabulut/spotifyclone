package com.example.spotifyclone.data.repositories.tokenrepository

import com.example.spotifyclone.data.remote.token.BearerToken
import com.example.spotifyclone.data.utils.FetchedResource
import com.example.spotifyclone.domain.spotifycloneErrorType
import com.example.spotifyclone.domain.getAssociatedspotifycloneErrorType
import com.fasterxml.jackson.core.JacksonException
import retrofit2.HttpException
import java.io.IOException

/**
 * A utility function used to run the [block] with a token retrieved
 * from the [TokenRepository] instance. It returns an instance of
 * [FetchedResource.Success] if the block did not throw an exception.
 * If the block throws either - a [HttpException] or an [IOException],
 * then [FetchedResource.Failure] containing the corresponding exception
 * will be returned. Any other exception thrown by the [block]
 * **will not be caught**.
 */
suspend fun <R> TokenRepository.runCatchingWithToken(block: suspend (BearerToken) -> R): FetchedResource<R, spotifycloneErrorType> =
    try {
        FetchedResource.Success(block(getValidBearerToken()))
    } catch (httpException: HttpException) {
        FetchedResource.Failure(httpException.getAssociatedspotifycloneErrorType())
    } catch (ioException: IOException) {
        FetchedResource.Failure(
            if (ioException is JacksonException) spotifycloneErrorType.DESERIALIZATION_ERROR
            else spotifycloneErrorType.NETWORK_CONNECTION_FAILURE
        )
    }