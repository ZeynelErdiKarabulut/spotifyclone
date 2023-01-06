package com.example.spotifyclone.di

import com.example.spotifyclone.viewmodels.homefeedviewmodel.greetingphrasegenerator.CurrentTimeBasedGreetingPhraseGenerator
import com.example.spotifyclone.viewmodels.homefeedviewmodel.greetingphrasegenerator.GreetingPhraseGenerator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class PhraseGeneratorModule {
    @Binds
    abstract fun bindCurrentTimeBasedGreetingPhraseGenerator(impl: CurrentTimeBasedGreetingPhraseGenerator): GreetingPhraseGenerator
}