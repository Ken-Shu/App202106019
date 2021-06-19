package com.ken_shu.retrofit.model.users

data class Address(
    val city: String,
    val geo: Geo,
    val street: String,
    val suite: String,
    val zipcode: String
)