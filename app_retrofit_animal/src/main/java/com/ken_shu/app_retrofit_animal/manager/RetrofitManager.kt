package com.ken_shu.app_retrofit_animal.manager

import com.ken_shu.app_retrofit_animal.api.JsonAnimal
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitManager {
    //設定管理 api  interface 裡面的 東西
    //placeHolderApi 型別是 JsonPlaceHolderApi
    private val animalApi : JsonAnimal
    //因為 placeholderApi 是private 所以要給他一個 api 屬性 然後會回傳 JsonPlaceHolderApi
    val api : JsonAnimal
        //然後有一個 get 的方法 用來取得 placeholderApi
        get() = animalApi

    companion object{ //就等同於 java 的 static
        val instance = RetrofitManager()
    }

    //初始化 placeholderApi
    //init 只會做一次
    init {
        //建立 Retrofit 的物件
        val retrofit = Retrofit.Builder()
            //注意 最後要加上 / 不然會報錯
            .baseUrl("https://data.coa.gov.tw/Service/OpenData/")
            //轉換工具 Converter(轉換器)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        //寫這段就是 再 init 之後 未來使用者都是透過 JsonPlaceHolderApi 來取得所有資源
        animalApi = retrofit.create(JsonAnimal::class.java)
    }
}