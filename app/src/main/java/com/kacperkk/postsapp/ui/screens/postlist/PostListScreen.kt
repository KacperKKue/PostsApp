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
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
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
import androidx.compose.ui.Alignment
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kacperkk.postsapp.model.PostDetail
import com.kacperkk.postsapp.model.Profile
import com.kacperkk.postsapp.model.UserDetail
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.kacperkk.postsapp.data.UserPreferencesRepository
import com.kacperkk.postsapp.ui.components.PostListItem
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostListScreen(
    navController: NavController,
    viewModel: PostListViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val context = LocalContext.current

    val userPreferencesRepository = remember { UserPreferencesRepository(context) }
    val avatarFileName by userPreferencesRepository.avatarPath.collectAsState(initial = "")
    val avatarFile = remember(avatarFileName) {
        if (avatarFileName.isNotBlank()) File(context.filesDir, avatarFileName) else null
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Posty",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                actions = {
                    IconButton(onClick = { navController.navigate(Profile) }) {
                        if (avatarFile?.exists() == true) {
                            AsyncImage(
                                model = avatarFile,
                                contentDescription = "User Avatar",
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.AccountCircle,
                                contentDescription = "Default Avatar",
                                modifier = Modifier
                                    .size(32.dp)
                                    .padding(end = 8.dp)
                            )
                        }
                    }
                },
            )
        }
    ) { paddingValues ->
        when (val state = uiState) {
            is PostListViewModel.UIState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            }

            is PostListViewModel.UIState.Error -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Nie udało się wczytać postów")

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(onClick = { viewModel.fetchData() }) {
                        Text("Spróbuj ponownie")
                    }
                }
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