package com.ken_shu.app_databinding_posts.manager

import com.ken_shu.app_databinding_posts.api.JsonPosts
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PostManager {
    private val jsonDBApi: JsonPosts
    val api: JsonPosts
        get() = jsonDBApi

    companion object {
        val instance = PostManager()
    }

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:3000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        jsonDBApi = retrofit.create(JsonPosts::class.java)
    }
}