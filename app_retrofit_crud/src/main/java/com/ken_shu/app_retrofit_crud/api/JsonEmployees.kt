package com.ken_shu.app_retrofit_crud.api


import com.ken_shu.app_retrofit_crud.model.Employee
import retrofit2.Call
import retrofit2.http.*


interface JsonEmployees {
    @GET("/employees")
    fun getEmployees(): Call<List<Employee>>

    @GET("/employees/{id}")
    fun getEmployees(@Path("id")id : Int) : Call<Employee>

    @POST("/employees")
    fun createEmployees(@Body employee: Employee): Call<Employee>

    //全部修改
    @PUT("/employees/{id}")
    fun updateEmployees(
        @Path("id") id: Int,
        @Body employee: Employee
    ): Call<Employee>

    //部分修改
    @PATCH("/employees/{id}")
    fun patchEmployees(@Path("id") id: Int, @Body employee: Employee): Call<Employee>

    @DELETE
    fun deleteEmployees(@Path("id")id: Int):Call<Void>
}
