package com.ken_shu.app_databinding_retrofit.api

import com.ken_shu.app_databinding_retrofit.model.Post
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

//取決於要怎麼使用 要有那些功能
interface JsonPosts {
    @GET("/posts")
    fun getPosts() : Call<List<Post>>

    @GET("/posts/{id}")
    fun getPosts(@Path("id") id :Int) : Call<Post>
}