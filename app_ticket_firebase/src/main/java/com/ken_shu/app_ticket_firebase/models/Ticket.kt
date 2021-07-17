package com.ken_shu.app_ticket_firebase.models

data class Ticket(
    val userName : String,
    var allTickets : Int ,
    val roundTrip : Int ,
    val oneWay : Int ,
    val total : Int
)
