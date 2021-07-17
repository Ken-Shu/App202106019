package com.ken_shu.app_ticket_firebase.services

import com.ken_shu.app_ticket_firebase.models.Ticket
import com.ken_shu.app_ticket_firebase.models.TicketsStock
import java.lang.Exception

class TicketServices {
    companion object{
        var errorMessages : Array<String>? = null
    }
    //檢驗票務資訊
    private fun checkTicket(allTickets: Int,
                            roundTrip: Int,
                            ticketsStock: TicketsStock) :Int{
        // 1 . 購買票數 > 0
        if (allTickets <= 0 ){
            return 1
        }
        // 2 . 剩餘票數是否足夠
        if (ticketsStock.totalamount < allTickets){
            return 2
        }
        // 3 . 來回票組數是否設定正確
        if (roundTrip * 2 > allTickets){
            return 3
        }
        return 0
    }

    //購票提交
    fun submit(allTickets: Int, roundTrip: Int, userName: String, ticketsStock: TicketsStock): Ticket? {
        val checkNo = checkTicket(allTickets, roundTrip, ticketsStock)
        var ticket: Ticket? = null
        if(checkNo == 0) {
            val oneWay = allTickets - (roundTrip * 2)
            val total = ((allTickets - oneWay) * ticketsStock.discount + oneWay) * ticketsStock.price
            ticket = Ticket(userName, allTickets, roundTrip, oneWay, total.toInt())
        } else {
            throw Exception(errorMessages?.get(checkNo))
        }
        return ticket
    }
}
