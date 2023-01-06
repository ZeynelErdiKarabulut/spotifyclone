package com.example.spotifyclone.ui.theme.dynamictheme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import com.example.spotifyclone.ui.theme.dynamictheme.dynamicthememanager.DynamicThemeManager
import com.example.spotifyclone.ui.theme.dynamictheme.dynamicthememanager.spotifycloneDynamicThemeManager
import com.example.spotifyclone.usecases.downloadDrawableFromUrlUseCase.spotifycloneDownloadDrawableFromUrlUseCase
import kotlinx.coroutines.Dispatchers

val LocalDynamicThemeManager: ProvidableCompositionLocal<DynamicThemeManager> =
    staticCompositionLocalOf {
        spotifycloneDynamicThemeManager(
            downloadDrawableFromUrlUseCase = spotifycloneDownloadDrawableFromUrlUseCase(Dispatchers.IO),
            defaultDispatcher = Dispatchers.IO
        )
    }