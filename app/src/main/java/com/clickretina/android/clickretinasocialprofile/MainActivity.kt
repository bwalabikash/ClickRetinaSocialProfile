package com.clickretina.android.clickretinasocialprofile

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.clickretina.android.clickretinasocialprofile.presentation.navigation.Screen
import com.clickretina.android.clickretinasocialprofile.presentation.navigation.TypeSafeNavigation
import com.clickretina.android.clickretinasocialprofile.ui.theme.ClickRetinaSocialProfileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ClickRetinaSocialProfileTheme {
                TypeSafeNavigation(startDestination = Screen.ScreenProfile)
            }
        }
    }
}