package com.kacperkk.postsapp.data.network

import com.kacperkk.postsapp.model.Todo
import com.kacperkk.postsapp.model.posts.Post
import com.kacperkk.postsapp.model.User
import retrofit2.http.GET
import retrofit2.http.Path

interface PostsService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<User>

    @GET("users/{id}/todos")
    suspend fun getTodo(@Path("id") userId: Int): List<Todo>
}