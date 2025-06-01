package com.kacperkk.postsapp.ui.screens.postlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.kacperkk.postsapp.PostsApp
import com.kacperkk.postsapp.data.PostsRepository
import com.kacperkk.postsapp.model.posts.Post
import com.kacperkk.postsapp.model.posts.PostWithUser
import com.kacperkk.postsapp.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PostListViewModel(
    private val postsRepository: PostsRepository
) : ViewModel() {

    sealed interface UIState {
        object Loading : UIState
        data class Error(val throwable: Throwable) : UIState
        data class Success(val data: List<PostWithUser>) : UIState
    }

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState: StateFlow<UIState> = _uiState

    private var cachedPosts: List<Post> = emptyList()
    private var cachedUsers: List<User> = emptyList()

    init {
        fetchData()
    }

    internal fun fetchData() {
        viewModelScope.launch {
            try {
                cachedPosts = postsRepository.getPosts()
                cachedUsers = postsRepository.getUsers()

                val userMap = cachedUsers.associateBy { it.id }
                val postWithUsers = cachedPosts.mapNotNull { post ->
                    val user = userMap[post.userId]
                    user?.let { PostWithUser(post, it) }
                }

                _uiState.value = UIState.Success(postWithUsers)
            } catch (e: Exception) {
                _uiState.value = UIState.Error(e)
            }
        }
    }

    fun getAllUsers(): List<User> = cachedUsers

    fun getPostById(postId: Int): Post? =
        cachedPosts.find { it.id == postId }

    fun getUserById(userId: Int): User? =
        cachedUsers.find { it.id == userId }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as PostsApp)
                val postsRepository = application.container.postsRepository
                PostListViewModel(postsRepository)
            }
        }
    }
}