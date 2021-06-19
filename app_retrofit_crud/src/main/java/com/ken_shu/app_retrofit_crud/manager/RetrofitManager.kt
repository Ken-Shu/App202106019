package com.ken_shu.app_retrofit_crud.manager

import com.ken_shu.app_retrofit_crud.api.*
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


//會以 Singleton 來設計
class RetrofitManager {
    //設定管理 api  interface 裡面的 東西
    //placeHolderApi 型別是 JsonPlaceHolderApi
    private val placeHolderApi : JsonEmployees
    //因為 placeholderApi 是private 所以要給他一個 api 屬性 然後會回傳 JsonPlaceHolderApi
    val api : JsonEmployees
    //然後有一個 get 的方法 用來取得 placeholderApi
    get() = placeHolderApi

    companion object{ //就等同於 java 的 static
        val instance = RetrofitManager()
    }

    //初始化 placeholderApi
    //init 只會做一次
    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val okHttpClient = OkHttpClient().newBuilder()
            .addInterceptor(logging)
            .addInterceptor(object : Interceptor{
                override fun intercept(chain: Interceptor.Chain): Response {
                    val oldRequest = chain.request()
                    val newRequest = oldRequest.newBuilder()
                        .header("User-Agent","chrome")
                        .build()
                    return chain.proceed(newRequest)
                }
            })

        //建立 Retrofit 的物件
        val retrofit = Retrofit.Builder()
                //注意 最後要加上 / 不然會報錯
            .baseUrl("http://10.0.2.2:3000/")
                //轉換工具 Converter(轉換器)
            .addConverterFactory(GsonConverterFactory.create())

            .build()

        //寫這段就是 再 init 之後 未來使用者都是透過 JsonPlaceHolderApi 來取得所有資源
        placeHolderApi = retrofit.create(JsonEmployees::class.java)
    }
}