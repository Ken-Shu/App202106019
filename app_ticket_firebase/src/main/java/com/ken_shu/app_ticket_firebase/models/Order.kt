package com.ken_shu.app_ticket_firebase.models

data class Order(val key : String, val Ticket : Ticket) :Comparable<Order>{
    companion object{
        var orderDelta = 1
    }
    override fun compareTo(order: Order): Int {
        return (Ticket.total - order.Ticket.total) * orderDelta
    }
}
