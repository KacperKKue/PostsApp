package com.kacperkk.postsapp.data

import com.kacperkk.postsapp.data.network.PostsService
import com.kacperkk.postsapp.model.Todo
import com.kacperkk.postsapp.model.posts.Post
import com.kacperkk.postsapp.model.User

interface PostsRepository {
    suspend fun getPosts(): List<Post>
    suspend fun getUsers(): List<User>
    suspend fun getTodo(userId: Int): List<Todo>
}

class NetworkPostsRepository(
    private val postsService: PostsService
) : PostsRepository {
    override suspend fun getPosts(): List<Post> = postsService.getPosts()
    override suspend fun getUsers(): List<User> = postsService.getUsers()
    override suspend fun getTodo(userId: Int): List<Todo> = postsService.getTodo(userId)
}