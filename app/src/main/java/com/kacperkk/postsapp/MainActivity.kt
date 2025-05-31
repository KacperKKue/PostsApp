package com.kacperkk.postsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.kacperkk.postsapp.model.PostDetail
import com.kacperkk.postsapp.model.PostList
import com.kacperkk.postsapp.model.Profile
import com.kacperkk.postsapp.model.UserDetail
import com.kacperkk.postsapp.ui.screens.postdetail.PostDetailScreen
import com.kacperkk.postsapp.ui.screens.postlist.PostListScreen
import com.kacperkk.postsapp.ui.screens.postlist.PostListViewModel
import com.kacperkk.postsapp.ui.screens.profilescreen.ProfileScreen
import com.kacperkk.postsapp.ui.screens.profilescreen.ProfileViewModel
import com.kacperkk.postsapp.ui.screens.userdetail.UserDetailScreen
import com.kacperkk.postsapp.ui.theme.PostsAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PostsAppTheme {
                PostsApplication()
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun PostsApplication() {
    val navController = rememberNavController()

    val postListViewModel: PostListViewModel =
        viewModel(factory = PostListViewModel.Factory)

    val profileViewModel: ProfileViewModel =
        viewModel(factory = ProfileViewModel.Factory)

    val locationPermission = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = locationPermission.permissions) {
        locationPermission.launchMultiplePermissionRequest()
    }

    NavHost(navController = navController, startDestination = PostList) {
        composable<PostList> {
            PostListScreen(
                navController = navController,
                viewModel = postListViewModel
            )
        }
        composable<PostDetail> {
            val args = it.toRoute<PostDetail>()

            val post = postListViewModel.getPostById(args.postId)

            PostDetailScreen(
                navController = navController,
                post = post
            )
        }
        composable<UserDetail> {
            val args = it.toRoute<UserDetail>()

            val post = postListViewModel.getUserById(args.userId)

            UserDetailScreen(
                navController = navController,
                user = post
            )
        }
        composable<Profile> {
            ProfileScreen(
                navController = navController,
                viewModel = profileViewModel
            )
        }
    }
}