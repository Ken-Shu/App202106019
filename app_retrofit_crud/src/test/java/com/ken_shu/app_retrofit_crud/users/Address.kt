package com.ken_shu.app_retrofit_crud.users

data class Address(
    var city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)