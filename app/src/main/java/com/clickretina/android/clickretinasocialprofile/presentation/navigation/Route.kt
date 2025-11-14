package com.clickretina.android.clickretinasocialprofile.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface Screen {
    @Serializable
    data object ScreenProfile : Screen
    @Serializable
    data object ScreenEditProfile : Screen
}