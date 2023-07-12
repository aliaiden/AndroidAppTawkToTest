package com.alihaidertest.feature_github_users.presentation.user_profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.alihaidertest.feature_github_users.presentation.user_profile.components.TransparentHintTextField
import kotlinx.coroutines.flow.collectLatest

@Composable
fun UserProfileScreen(
    navController: NavController,
    viewModel: UserProfileViewModel = hiltViewModel()
) {
    val noteState = viewModel.note.value
    val userState = viewModel.user.value

    //to show snackbar
    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    // cache image for offline loading
    val imageLoader = ImageLoader(context).newBuilder().respectCacheHeaders(false).build()
    val placeholderImage = android.R.drawable.ic_menu_close_clear_cancel

    val imageRequest = ImageRequest.Builder(context)
        .data(userState.avatar_url)
        .memoryCacheKey(userState.avatar_url)
        .diskCacheKey(userState.avatar_url)
        .placeholder(placeholderImage)
        .error(placeholderImage)
        .fallback(placeholderImage)
        .diskCachePolicy(CachePolicy.ENABLED)
        .memoryCachePolicy(CachePolicy.ENABLED)
        .transformations(CircleCropTransformation())
        .build()

    LaunchedEffect(key1 = true){
        viewModel.eventFlow.collectLatest {
            event ->
            when(event) {
                is UserProfileViewModel.UiEvent.ShowSnackbar -> {
                    scaffoldState.snackbarHostState.showSnackbar(
                        message = event.message
                    )
                }

                is UserProfileViewModel.UiEvent.SaveNote -> {
                    navController.navigateUp()
                }
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(UserProfileEvent.SaveNote)
                },
                backgroundColor = MaterialTheme.colors.primary
            ) {
//                Icon(imageVector = Icons.Default.Save, contentDescription = "Save note")
                Text(text = "SAVE")

            }
        },
        scaffoldState = scaffoldState
    ) {
        AsyncImage(
            model = imageRequest,
            modifier = Modifier
                .size(64.dp)
//                .align(Alignment.Center)
            ,
            contentDescription = null,
            imageLoader = imageLoader
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = userState.login,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Registered as " + userState.type + "\nID: " + userState.id,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = userState.html_url,
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))

        TransparentHintTextField(
            text = noteState.text,
            hint = noteState.hint,
            onValueChange ={
                           viewModel.onEvent(UserProfileEvent.EnteredNote(it))
            },
            onFocusChange = {UserProfileEvent.ChangeNoteFocus(it)},
            isHintVisible = noteState.isHintVisible,
            textStyle = MaterialTheme.typography.body1,
            modifier = Modifier.fillMaxHeight()
        )
    }

}