package com.alihaidertest.feature_github_users.presentation.users.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.onFocusChanged
import com.alihaidertest.feature_github_users.presentation.users.UsersViewModel

@Composable
fun SearchField(
    modifier: Modifier,
    searchKey: String = "",
    onValueChange: (String) -> Unit,
) {
    var query by remember { mutableStateOf(searchKey) }

    val focusManager = LocalFocusManager.current
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Gray,
            )
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        BasicTextField(
            value = query,
            onValueChange = onValueChange,
//            onValueChange = {
//                query = it
//                if (query.isNotEmpty()) {
//                    exampleViewModel.performQuery(it)
//                }
//            },
            modifier = Modifier
                .background(
                    Color.White,
                )
                .height(38.dp)
                .fillMaxWidth(),
            textStyle = TextStyle(
                color = Color.Black,
                fontSize = 18.sp
            ),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
            maxLines = 1,
            decorationBox = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                ) {
                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (query.isEmpty()) {
                            Text(
                                "Search",
                                color = Color.Gray
                            )
                        }
                        it()
                    }
                    if (query.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                query = ""
                                focusManager.clearFocus()
                                onValueChange(query)
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear Icon",
                                tint = Color.Black
                            )
                        }
                    }
                }
            }
        )
    }
}