package com.kacperkk.postsapp.data.network

import com.kacperkk.postsapp.model.posts.Post
import com.kacperkk.postsapp.model.User
import retrofit2.http.GET

interface PostsService {
    @GET("posts")
    suspend fun getPosts(): List<Post>

    @GET("users")
    suspend fun getUsers(): List<User>
}