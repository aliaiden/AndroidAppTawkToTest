package com.alihaidertest.feature_github_users.presentation.users

import android.content.Intent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarDuration
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.alihaidertest.feature_github_users.presentation.user_profile.UserProfileActivity
import com.alihaidertest.feature_github_users.presentation.users.components.SearchField
import com.alihaidertest.feature_github_users.presentation.users.components.UserItem
import kotlinx.coroutines.launch

@Composable
fun UsersScreen(
    navController: NavController,
    viewModel: UsersViewModel = hiltViewModel()
) {

    val state = viewModel.state.value
    // to show snackbar
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(scaffoldState = scaffoldState) {
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (state.users == null) {
                // show loading ui
                Column(modifier = Modifier.align(Alignment.Center)) {
                    CircularProgressIndicator(
                        modifier = Modifier.then(
                            Modifier
                                .size(30.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    )
                    Text(
                        text = "Loading...",
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            } else {
                //show list of users with search field at the top
                Scaffold(topBar = {
//                        https://techavator.com/create-a-searchview/
                    SearchField(
                        modifier = Modifier,
                        searchKey = state.searchKey,
                        onValueChange = {
                            viewModel.onEvent(UsersEvent.SearchUsers(it))
                        }
                    )

                }) {

                    //pagination when scrolled to bottom
                    //https://levelup.gitconnected.com/effortlessly-implement-paging-in-jetpack-compose-5cae5579fdb3

                    //use of drivedStateOf
                    //https://proandroiddev.com/jetpack-compose-best-practices-you-must-always-remember-98382b132d44
                    val listState = rememberLazyListState()

                    val shouldLoadMore by remember {
                        derivedStateOf {
                            listState.firstVisibleItemIndex >= state.users!!.lastIndex - 5
                        }
                    }

                    val mContext = LocalContext.current
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(state.users.size) { index ->
                            UserItem(
                                user = state.users[index],
                                modifier = Modifier
                                    .fillMaxWidth()
//                                    .clickable {
//                                        navController.navigate(Screen.UserProfileScreen.route + "?id=" + state.users[index].id)
//                                    }
                                ,
                                onItemClick = {
//                                    navController.navigate(Screen.UserProfileScreen.route + "?username=" + state.users[index].login)
                                    val intent = Intent(mContext, UserProfileActivity::class.java)
                                    intent.putExtra("username", state.users[index].login)
                                    intent.putExtra("avatar", state.users[index].avatar_url)
                                    mContext.startActivity(intent)

                                }
                            )
                            if (index >= state.users!!.lastIndex) {
                                //load next page items
                                print("LOAD NEXT PAGE ITEMS")
//                                state.pageID.plus(1)
                                viewModel.getUsers(
                                    pageID = state.users!![state.users!!.lastIndex].pageID.plus(
                                        1
                                    )
                                )
                                Box(
                                    Modifier
                                        .fillMaxWidth()
                                ) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.then(
                                            Modifier
                                                .size(20.dp)
                                                .align(Alignment.Center)
                                        )
                                    )
                                }
                            }
                        }
                    }

                    LaunchedEffect(state.networkStatus) {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = state.networkStatus,
                                duration = SnackbarDuration.Short
                            )
                        }
                    }

                    Text(
                        text = state.error,
                        color = MaterialTheme.colors.error,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .align(Alignment.Center)
                    )
                }

                if (state.isLoading) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

            }
        }
    }
}


