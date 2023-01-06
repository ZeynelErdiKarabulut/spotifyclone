package com.example.spotifyclone.data.repositories.tokenrepository

import com.example.spotifyclone.data.remote.token.BearerToken

/**
 * An interface that contains all the methods that are requisite for
 * an class that implements [TokenRepository].
 */
interface TokenRepository {
    /**
     * Used to get an instance of a valid [BearerToken].
     * This method should always return a valid token.
     */
    suspend fun getValidBearerToken(): BearerToken
}