package com.ken_shu.app_dogforinternet.service


import com.google.gson.Gson
import com.google.gson.JsonParser

import com.ken_shu.app_dogforinternet.model.Animal
import com.ken_shu.app_dogforinternet.R
import okhttp3.OkHttpClient
import okhttp3.Request

class AnimalService (var path : String ){
    fun getAnimals() : List<Animal> {
        val  client = OkHttpClient()

        val request = Request.Builder().url(path).build()

        client.newCall(request).execute().use {
            val json = it.body!!.string()
            return Gson().fromJson(json , Array<Animal>::class.java).toList()
        }

    }

}