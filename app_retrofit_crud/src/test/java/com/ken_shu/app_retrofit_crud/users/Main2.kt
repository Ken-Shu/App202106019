package com.ken_shu.app_retrofit_crud.users

import com.google.gson.JsonElement
import com.google.gson.JsonParser
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url


interface OpenWeatherService{
    @GET
    fun getStringResponse(@Url url : String ) : Call<String>
}

fun main() {

    //https://api.openweathermap.org/data/2.5/weather?q=taoyuan,tw&appid=dbdd559ece9681a33845d41709506210
    //透過 retrofit 可以取得 temp 現在溫度
    val path ="weather?q=taoyuan,tw&appid=dbdd559ece9681a33845d41709506210"
    val retrofit = Retrofit.Builder().addConverterFactory(ScalarsConverterFactory.create())
        .baseUrl("https://api.openweathermap.org/data/2.5/")
        .build()

    val api = retrofit.create(OpenWeatherService::class.java)

    val json = api.getStringResponse(path).execute().body()

    val jelment : JsonElement = JsonParser.parseString(json)
    val jobject = jelment.asJsonObject
    var temp = jobject.getAsJsonObject("main").get("temp").asDouble
    temp-=273.15
    println(temp)
    //println(api.getStringResponse(path).execute().body())
}