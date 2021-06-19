package com.ken_shu.api

import com.google.gson.JsonParser
import okhttp3.OkHttpClient
import okhttp3.Request
import java.text.SimpleDateFormat
import java.util.*

fun main() {
    val q = "taoyuan,tw"
    val appid = "dbdd559ece9681a33845d41709506210"
    val path = "http://api.openweathermap.org/data/2.5/weather?q=${q}&appid=${appid}"
    val client = OkHttpClient()

    val request = Request.Builder()
        .url(path)
        .build()

    client.newCall(request).execute().use {
        val json = it.body!!.string()
        println("json:${json}")

        //利用Gson 分析出我要的資料
        //sys.country.name
        //weather.main , weather.description , weather.icon
        //main.temp , main.feels_like , main.humidity
        //clouds.all
        //dt
        val root = JsonParser.parseString(json).asJsonObject
        val name = root.get("name").toString().replace("\"", "")
        val country = root.getAsJsonObject("sys").get("country").toString().replace("\"", "")
        val weater = root.getAsJsonArray("weather")[0].asJsonObject
        val weather_main = weater.get("main").toString().replace("\"", "")
        val weather_description = weater.get("description").toString().replace("\"", "")
        val weather_icon = weater.get("icon").toString().replace("\"", "")
        val main = root.getAsJsonObject("main")
        val main_temp = main.get("temp").asDouble
        val main_feels_like = main.get("feels_like").asDouble
        val main_humidity = main.get("humidity").asDouble
        val clouds_all = root.getAsJsonObject("clouds").get("all").asInt
        val dt = root.get("dt").asInt


        println("${name}")
        println("${country}")
        println("${weather_main}")
        println("${weather_description}")
        println("${weather_icon}")
        println("${main_temp}")
        println("${main_feels_like}")
        println("${main_humidity}")
        println("${clouds_all}")
        println("${dt}")
        val transformedDate :String = SimpleDateFormat("yyyy/MM/dd HH:mm:ss E").format(Date(dt * 1000L))
        println(transformedDate)
        val ow = OpenWeather(
            name ,
            country ,
            weather_main,
            weather_description,
            weather_icon ,
            main_temp,
            main_feels_like ,
            main_humidity,
            clouds_all,
            dt)
        println(ow)
        print(ow.weather_icon_url)
    }
}