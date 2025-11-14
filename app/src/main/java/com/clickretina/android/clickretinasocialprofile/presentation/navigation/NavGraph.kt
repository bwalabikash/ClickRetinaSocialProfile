package com.clickretina.android.clickretinasocialprofile.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.clickretina.android.clickretinasocialprofile.presentation.screen.profile.ProfileScreen

@Composable
fun TypeSafeNavigation(
    modifier: Modifier = Modifier,
    startDestination: Screen
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable<Screen.ScreenProfile> {
            ProfileScreen(navController)
        }
        composable<Screen.ScreenEditProfile> {
            // Edit Profile will open here
        }
    }
}