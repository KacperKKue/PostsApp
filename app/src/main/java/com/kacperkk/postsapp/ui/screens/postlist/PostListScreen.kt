package com.kacperkk.postsapp.ui.screens.postlist

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kacperkk.postsapp.model.PostDetail
import com.kacperkk.postsapp.model.UserDetail
import com.kacperkk.postsapp.ui.components.PostListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Posts",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is PostListViewModel.UIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is PostListViewModel.UIState.Error -> {
                Text("Błąd: ${state.throwable.message}", modifier = Modifier.padding(16.dp))
            }

            is PostListViewModel.UIState.Success -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                ) {
                    items(state.data.size) { index ->
                        val postWithUser = state.data[index]
                        PostListItem(
                            postWithUser = postWithUser,
                            onPostClick = {
                                navController.navigate(PostDetail(postWithUser.post.id))
                            },
                            onUserClick = {
                                navController.navigate(UserDetail(postWithUser.user.id))
                            }
                        )
                    }
                }
            }
        }
    }
}