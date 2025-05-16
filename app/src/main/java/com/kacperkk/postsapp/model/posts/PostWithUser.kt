package com.kacperkk.postsapp.model.posts

import com.kacperkk.postsapp.model.User

data class PostWithUser(
    val post: Post,
    val user: User
)