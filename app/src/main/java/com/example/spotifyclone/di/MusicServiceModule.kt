package com.example.spotifyclone.di

import com.example.spotifyclone.data.remote.musicservice.SpotifyBaseUrls
import com.example.spotifyclone.data.remote.musicservice.SpotifyService
import com.example.spotifyclone.data.remote.token.tokenmanager.TokenManager
import com.example.spotifyclone.utils.defaultspotifycloneJacksonConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MusicServiceModule {
    @Provides
    @Singleton
    fun provideSpotifyService(): SpotifyService = Retrofit.Builder()
        .baseUrl(SpotifyBaseUrls.API_URL)
        .addConverterFactory(defaultspotifycloneJacksonConverterFactory)
        .build()
        .create(SpotifyService::class.java)

    @Provides
    @Singleton
    fun provideTokenManager(): TokenManager = Retrofit.Builder()
        .baseUrl(SpotifyBaseUrls.AUTHENTICATION_URL)
        .addConverterFactory(defaultspotifycloneJacksonConverterFactory)
        .build()
        .create(TokenManager::class.java)
}