package com.example.spotifyclone.data.repositories.tokenrepository

import com.example.spotifyclone.data.encoder.Base64Encoder
import com.example.spotifyclone.data.remote.token.BearerToken
import com.example.spotifyclone.data.remote.token.getSpotifyClientSecret
import com.example.spotifyclone.data.remote.token.isExpired
import com.example.spotifyclone.data.remote.token.toBearerToken
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import javax.inject.Inject

class SpotifyTokenRepository @Inject constructor(
    private val tokenManager: TokenManager,
    private val base64Encoder: Base64Encoder
) : TokenRepository {
    private var token: BearerToken? = null

    /**
     * Used to get an instance of [BearerToken].
     * If the [token] is null or expired, a new token would be automatically
     * requested and assigned. Therefore, this function guarantees that
     * a valid token would always be returned. Thus, this function can
     * be safely called multiple times.
     */
    override suspend fun getValidBearerToken(): BearerToken {
        if (token == null || token?.isExpired == true) getAndAssignToken()
        return token!!
    }

    /**
     * A helper function that gets and assigns a new [token].
     */
    private suspend fun getAndAssignToken() {
        val clientSecret = getSpotifyClientSecret(base64Encoder)
        token = tokenManager
            .getNewAccessToken(clientSecret)
            .toBearerToken()
    }
}