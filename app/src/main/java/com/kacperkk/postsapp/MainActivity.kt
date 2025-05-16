package com.kacperkk.postsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.kacperkk.postsapp.model.PostDetail
import com.kacperkk.postsapp.model.PostList
import com.kacperkk.postsapp.model.UserDetail
import com.kacperkk.postsapp.ui.screens.postdetail.PostDetailScreen
import com.kacperkk.postsapp.ui.screens.postlist.PostListScreen
import com.kacperkk.postsapp.ui.screens.postlist.PostListViewModel
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

@Composable
fun PostsApplication() {
    val navController = rememberNavController()

    val viewModel: PostListViewModel =
        viewModel(factory = PostListViewModel.Factory)

    NavHost(navController = navController, startDestination = PostList) {
        composable<PostList> {
            PostListScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
        composable<PostDetail> {
            val args = it.toRoute<PostDetail>()

            val post = viewModel.getPostById(args.postId)

            PostDetailScreen(
                navController = navController,
                post = post
            )
        }
        composable<UserDetail> {
            val args = it.toRoute<UserDetail>()

            val post = viewModel.getUserById(args.userId)

            UserDetailScreen(
                navController = navController,
                user = post
            )
        }
    }
}