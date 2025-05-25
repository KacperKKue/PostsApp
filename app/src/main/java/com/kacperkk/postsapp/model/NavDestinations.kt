package com.kacperkk.postsapp.model

import com.kacperkk.postsapp.model.posts.Post
import kotlinx.serialization.Serializable

@Serializable
object PostList

@Serializable
data class PostDetail(val postId: Int)

@Serializable
data class UserDetail(val userId: Int)

@Serializable
object Profile