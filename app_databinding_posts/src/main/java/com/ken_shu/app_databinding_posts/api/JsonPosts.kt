package com.ken_shu.app_databinding_posts.api

import com.ken_shu.app_databinding_posts.model.Posts
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface JsonPosts {
    @GET("/posts")
    fun getposts():Call<List<Posts>>
    @GET("/posts/{id}")
    fun getposts(@Path("id")id : Int) : Call<Posts>

    @POST("/posts")
    fun addPost(@Body post: Posts) : Call<Posts>

}