package com.example.spotifyclone.data.repositories.tokenrepository

import com.example.spotifyclone.data.remote.token.BearerToken
import java.time.LocalDateTime

class TokenRepositoryStub : TokenRepository {
    override suspend fun getValidBearerToken() = BearerToken(
        "",
        LocalDateTime.now(),
        60
    )
}