package com.alihaidertest.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.transform.CircleCropTransformation
import com.alihaidertest.model.UsersResponse
import com.alihaidertest.viewmodel.MainActivityViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UserDetailsBottomSheet(
    user: UsersResponse,
    coroutineScope: CoroutineScope,
    modalSheetState: ModalBottomSheetState,
    viewModel: MainActivityViewModel
) {
    var notes by remember { mutableStateOf("") }
    notes = user.notes

    val onTextChange = { text: String ->
        notes = text
    }

    val context = LocalContext.current
    // cache image for offline loading
    val imageLoader = ImageLoader(context).newBuilder().respectCacheHeaders(false).build()
    val placeholderImage = android.R.drawable.ic_menu_close_clear_cancel

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

    BackHandler(modalSheetState.isVisible) {
        coroutineScope.launch { modalSheetState.hide() }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        Button(
            onClick = {
                coroutineScope.launch { modalSheetState.hide() }
            }
        ) {
            Text(text = "←")
        }
        Box(
            modifier = Modifier
                .height(64.dp)
                .fillMaxWidth()
        ) {
            AsyncImage(
                model = imageRequest,
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center),
                contentDescription = null,
                imageLoader = imageLoader
            )
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = user.login,
                style = MaterialTheme.typography.h6,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Registered as " + user.type + "\nID: " + user.id,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = user.html_url,
            style = MaterialTheme.typography.body1
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = { onTextChange(it) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            ),
            singleLine = true,
            label = { Text("Notes") },
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )

        Box(modifier = Modifier.fillMaxWidth()) {
            Button(
                onClick = {
                    if (notes.isNotEmpty()) {
                        viewModel.insertUserNotes(notes, user.id)
                    }
                },
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text("Save")
            }
        }

    }

}


