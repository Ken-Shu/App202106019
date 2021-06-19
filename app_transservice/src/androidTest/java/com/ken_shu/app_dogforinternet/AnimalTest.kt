package com.ken_shu.app_dogforinternet

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request

fun main() {
    val path = "https://data.coa.gov.tw/Service/OpenData/TransService.aspx?UnitId=QcbUEzN6E6DL"
    val client = OkHttpClient()
    val request = Request.Builder().url(path).build()

    client.newCall(request).execute().use {
        val json = it.body!!.string()
        val animal : List<Animal> = Gson().fromJson(json , Array<Animal>::class.java).toList()
        println("animal = ${animal.size}")
    }
    GlobalScope.launch {
        delay(10000)

        println("Hello")
    }

    println("World")
   Thread.sleep(11000)


}