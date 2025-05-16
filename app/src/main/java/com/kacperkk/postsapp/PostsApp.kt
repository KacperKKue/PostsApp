package com.kacperkk.postsapp

import android.app.Application
import com.kacperkk.postsapp.data.AppContainer
import com.kacperkk.postsapp.data.DefaultAppContainer

class PostsApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}