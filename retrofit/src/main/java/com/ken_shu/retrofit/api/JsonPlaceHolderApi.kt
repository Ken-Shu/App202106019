package com.ken_shu.retrofit.api

import com.ken_shu.retrofit.model.Comments
import com.ken_shu.retrofit.model.Photo
import com.ken_shu.retrofit.model.Post
import com.ken_shu.retrofit.model.users.User

import retrofit2.Call
import retrofit2.http.*

//定義這個API 的目的是為了給 (client)前端 使用
interface JsonPlaceHolderApi {
    //查詢 posts
    @GET("posts")
    fun getPosts(): Call<List<Post>>

    //posts?userId=2&_sort=id&_order=desc
    @GET("posts")
    fun getPosts(
        @Query("userId") userId: Int,
        @Query("_sort") _sort: String,
        @Query("_order") _order: String
    ): Call<List<Post>>
    //posts?userId=2&userId=4&_sort=id&_order=desc
    @GET("posts")
    fun getPosts(
        @Query("userId") userId: Array<Int>,
        @Query("_sort") _sort: String,
        @Query("_order") _order: String
    ): Call<List<Post>>

    //查詢 /posts/3/comments
    @GET("posts/{id}/comments")
    fun getComments(@Path("id")postId : Int) : Call<List<Comments>>

    //查詢 comments
    @GET("comments")
    fun getComments(): Call<List<Comments>>

    //查詢 URL 字串
    @GET
    fun getComments(@Url url : String) : Call<List<Comments>>

    //查詢 comments
    @GET("comments")
    fun getComments(@QueryMap params : Map<String , String>): Call<List<Comments>>

    //查詢 users
    @GET("users")
    fun getUsers()  : Call<List<User>>

    //單筆查詢 users/1
    @GET("users/{id}")
    fun getUsers(@Path("id")id:Int) : Call<User>

    //單筆查詢 ex: posts/2
    @GET("/posts/{id}")
    fun getPosts(@Path("id")id : Int):Call<Post>

    //查詢 photos
    @GET("photos")
    fun getPhotos() : Call<List<Photo>>

}