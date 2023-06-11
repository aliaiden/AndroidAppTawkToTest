package com.alihaidertest.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.alihaidertest.model.UsersResponse
import com.alihaidertest.viewmodel.MainActivityViewModel
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun UsersScreen(viewModel: MainActivityViewModel) {

    val users by viewModel.users.observeAsState(null)

    LaunchedEffect(Unit) {
        viewModel.fetchUsers()
    }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmStateChange = { it != ModalBottomSheetValue.HalfExpanded }
    )
    val sheetContentState = remember {
        mutableStateOf(
            UsersResponse(
                -1,
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                false,
                -1,
                ""
            )
        )
    }

    //https://proandroiddev.com/bottom-sheet-in-jetpack-compose-d7e106422606
    ModalBottomSheetLayout(
        sheetShape = RoundedCornerShape(topStart = 0.dp, topEnd = 0.dp),
        sheetState = modalSheetState,
        sheetContent = {
            UserDetailsBottomSheet(
                user = sheetContentState.value,
                coroutineScope,
                modalSheetState,
                viewModel
            )
        }
    ) {
        Scaffold {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                if (users == null) {
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
                        SearchView(viewModel)
                    }) {

                        //pagination when scrolled to bottom
                        //https://levelup.gitconnected.com/effortlessly-implement-paging-in-jetpack-compose-5cae5579fdb3

                        //use of drivedStateOf
                        //https://proandroiddev.com/jetpack-compose-best-practices-you-must-always-remember-98382b132d44
                        val listState = rememberLazyListState()

//                        val shouldLoadMore by remember {
//                            derivedStateOf {
//                                listState.firstVisibleItemIndex >= users!!.lastIndex - 5
//                            }
//                        }


                        LazyColumn(state = listState) {
                            items(users!!.size) { index ->
                                UserListItem(
                                    users!![index],
                                    coroutineScope,
                                    modalSheetState,
                                    sheetContentState
                                )
                                if (index >= users!!.lastIndex) {
                                    viewModel.fetchUsers()
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

                    }
                }
            }
        }
    }


}


