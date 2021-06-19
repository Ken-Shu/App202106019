package com.ken_shu.app_retrofit_animal.api

import com.ken_shu.app_retrofit_animal.model.Animal
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface JsonAnimal {
//    @GET("animal_place")
//    fun get_animal_place() :Call<List<Animal>>
//    @GET("animal_kind")
//    fun get_animal_kind() :Call<List<Animal>>
//    @GET("animal_file")
//    fun get_animal_file() :Call<List<Animal>>
    @GET("TransService.aspx")
    fun getAnimal(@Query("UnitId")uid : String) : Call<List<Animal>>
}