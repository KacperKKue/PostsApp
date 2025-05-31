package com.kacperkk.postsapp.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.preferencesDataStore
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kacperkk.postsapp.data.network.PostsService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import androidx.datastore.preferences.core.Preferences

interface AppContainer {
    val postsRepository: PostsRepository
    val userPreferencesRepository: UserPreferencesRepository
}

class DefaultAppContainer(context: Context) : AppContainer {
    private val postsApiUrl = "https://jsonplaceholder.typicode.com/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(postsApiUrl)
        .build()

    private val postService: PostsService by lazy {
        retrofit.create(PostsService::class.java)
    }

    override val postsRepository: PostsRepository by lazy {
        NetworkPostsRepository(postService)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository(context)
    }
}