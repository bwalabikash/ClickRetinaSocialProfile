package com.clickretina.android.clickretinasocialprofile.presentation.screen.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.clickretina.android.clickretinasocialprofile.R
import com.clickretina.android.clickretinasocialprofile.data.model.ProfileResponseModel
import com.clickretina.android.clickretinasocialprofile.ui.theme.colorPrimary
import com.clickretina.android.clickretinasocialprofile.utils.LinkUtils.openInCustomTab
import com.clickretina.android.clickretinasocialprofile.utils.LinkUtils.openSocial

@Composable
fun ProfileScreen(
    navController: NavController
) {

    val viewModel: ProfileViewModel = viewModel()

    val uiState by viewModel.uiState.collectAsState()
    val errorEvent by viewModel.errorEvent.collectAsState()
    val context = LocalContext.current

    // Material3: use a SnackbarHostState instead of rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }

    // show snackbars on error events
    LaunchedEffect(errorEvent) {
        errorEvent?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearErrorEvent()
        }
    }

    Scaffold(
        // provide a SnackbarHost that uses the host state
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(padding)
        ) {
            when (uiState) {
                is UiState.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
                is UiState.Error -> {
                    val msg = (uiState as UiState.Error).message
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_icon_close),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Something went wrong", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(msg)
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { viewModel.retry() }) {
                            Icon(
                                painter = painterResource(R.drawable.ic_icon_refresh),
                                contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Retry")
                        }
                    }
                }
                is UiState.Success -> {
                    val data = (uiState as UiState.Success).profileResponse
                    UserProfileContent(
                        profile = data,
                        onOpenWebsite = {  url ->
                            openInCustomTab(context = context, rawUrl = url)
                                        },
                        onOpenSocial = {  platform: String, url: String ->
                            openSocial(context = context, platform = platform, rawUrl = url)
                        }
                    )
                }
                UiState.Idle -> {
                    // Optionally show placeholder or do nothing
                }
            }
        }
    }
}


@Composable
private fun UserProfileContent(
    profile: ProfileResponseModel,
    onOpenWebsite: (String) -> Unit,
    onOpenSocial: (platform: String, url: String) -> Unit
) {

    val user = profile.user

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(Color.Transparent)
        ) {

            // Background Image
            Image(
                painter = painterResource(id = R.drawable.ic_img_bg),
                contentDescription = null,
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.FillBounds
            )

            // Username
            Text(
                text = user?.username ?: "unknown",
                fontSize = 14.sp,
                lineHeight = 14.sp,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.Center)
            )

            // Setting Icon
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.CenterEnd)
                    .offset(x = (-10).dp)
                    .clickable {

                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_icon_setting),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            // Profile Image
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (60).dp)
            ) {
                if (user?.avatar != null) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.avatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = "avatar",
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                    )
                } else {
                    // placeholder circle
                    Box(modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                    )
                }
            }

        }

        // Full Name and Location
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 75.dp)
        ) {

            // Full Name
            Text(
                text = user?.name ?: "Unknown",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black
            )

            Spacer(Modifier.height(5.dp))
            // Location
            Text(
                text = listOfNotNull(user?.location?.city, user?.location?.country).joinToString(", "),
                color = Color.Black.copy(0.5f)
            )
        }

        // Followers and Following
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(54.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.Black.copy(0.1f))
        ) {

            // Followers
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user?.statistics?.followers ?: 0}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Followers",
                    color = Color.Black.copy(0.5f)
                )
            }

            // Following
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "${user?.statistics?.following ?: 0}",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color.Black
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "Following",
                    color = Color.Black.copy(0.5f)
                )
            }
        }

        // Social Links
        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {

            // Website
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        user?.social?.website?.let { website ->
                            onOpenWebsite(website)
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_icon_website),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(7.dp)
                    .background(colorPrimary, CircleShape)
            )

            // Instagram
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        val first = user?.social?.profiles?.firstOrNull()
                        first?.url?.let { url ->
                            onOpenSocial(first.platform ?: "other", url)
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_icon_instagram),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .size(7.dp)
                    .background(colorPrimary, CircleShape)
            )

            // Facebook
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape)
                    .clickable {
                        val secondSocialMediaItem = user?.social?.profiles?.getOrNull(1)
                        secondSocialMediaItem?.url?.let { url ->
                            onOpenSocial(secondSocialMediaItem.platform ?: "other", url)
                        }
                    }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_icon_facebook),
                    contentDescription = null,
                    tint = Color.Gray,
                    modifier = Modifier
                        .size(24.dp)
                )
            }

        }

        // Shots and Collection
        Spacer(Modifier.height(10.dp))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {

            // shots
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .clip(RoundedCornerShape(9.dp))
                    .background(colorPrimary.copy(0.1f))
            ) {
                Text(
                    text = "${user?.statistics?.activity?.shots ?: 0} shots",
                    style = MaterialTheme.typography.titleLarge,
                    color = colorPrimary
                )
            }


            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .weight(1f)
                    .height(54.dp)
                    .clip(RoundedCornerShape(9.dp))
            ) {
                Text(
                    text = "${user?.statistics?.activity?.collections ?: 0} Collections",
                    color = Color.Black
                )
            }

        }

        // Illustrator
        Spacer(Modifier.height(30.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
        ) {

            Image(
                painter = painterResource(R.drawable.ic_img_illustrator),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize(0.6f)
            )

        }


    }
}

@Preview
@Composable
fun ProfileScreenPreview() {
    ProfileScreen(rememberNavController())
}