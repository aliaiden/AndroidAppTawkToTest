package com.alihaidertest.feature_github_users.presentation.users.components

import android.R
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.alihaidertest.feature_github_users.domain.model.User
import kotlinx.coroutines.launch

@Composable
fun UserItem(
    user: User,
    modifier: Modifier,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                println("CLICKED")
//                coroutineScope.launch {
//                    if (modalSheetState.isVisible)
//                        modalSheetState.hide()
//                    else {
//                        sheetContentState.value = user
//                        modalSheetState.animateTo(ModalBottomSheetValue.Expanded)
//                    }
//                }
            },
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            val context = LocalContext.current
            // cache image for offline loading
            val imageLoader = ImageLoader(context).newBuilder().respectCacheHeaders(false).build()
            val placeholderImage = R.drawable.ic_menu_close_clear_cancel
            val imageRequest = ImageRequest.Builder(context)
                .data(user.avatar_url)
                .memoryCacheKey(user.avatar_url)
                .diskCacheKey(user.avatar_url)
                .placeholder(placeholderImage)
                .error(placeholderImage)
                .fallback(placeholderImage)
                .diskCachePolicy(CachePolicy.ENABLED)
                .memoryCachePolicy(CachePolicy.ENABLED)
                .transformations(CircleCropTransformation())
                .build()

            Row(modifier = Modifier) {
                AsyncImage(
                    model = imageRequest,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    imageLoader = imageLoader
                )
                Box(modifier = Modifier.padding(10.dp, 15.dp)){

                    Text(
                        text = user.login,
                        style = MaterialTheme.typography.h6

                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Registered as " + user.type,
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = user.html_url,
                style = MaterialTheme.typography.body1
            )
        }
    }
}