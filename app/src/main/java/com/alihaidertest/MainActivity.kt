package com.alihaidertest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptions
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alihaidertest.feature_github_users.presentation.users.UsersScreen
import com.alihaidertest.feature_github_users.presentation.util.Screen
import com.alihaidertest.ui.theme.MyAppTheme
import com.alihaidertest.viewmodel.MainActivityViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.UsersScreen.route,
                        builder = {
                            composable(route = Screen.UsersScreen.route) {
                                UsersScreen(navController = navController)
                            }
//                            composable(
//                                route = Screen.UserProfileScreen.route +
//                                        "?username={username}",
//                                arguments = listOf(
//                                    navArgument(
//                                        name = "username"
//                                    ) {
//                                        type = NavType.StringType
//                                        defaultValue = ""
//                                    }
//                                )
//                            ) {
//                                UserProfileScreen(navController = navController)
//                            }
                        })


////                    GreetingText("Happy Birthday to Ali Haider", "Haider")
//                    UsersScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GreetingText(message: String, from: String, modifier: Modifier = Modifier) {
    Text(text = message, fontSize = 70.sp)
    Text(text = "From: $from")
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DefaultPreview() {
    MyAppTheme {
        GreetingText("Happy Birthday to Ali", "Haider")
    }
}