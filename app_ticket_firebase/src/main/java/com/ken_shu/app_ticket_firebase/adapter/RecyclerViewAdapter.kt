package com.ken_shu.app_ticket_firebase.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ken_shu.app_ticket_firebase.R
import com.ken_shu.app_ticket_firebase.models.Order
import kotlinx.android.synthetic.main.order.view.*

//適配器 (配置每一筆訂單紀錄)
class RecyclerViewAdapter(val listener: OrderOnItemClickListener) :
    RecyclerView.Adapter<RecyclerViewAdapter.OrderViewHolder>() {
    private var orderList: List<Order> = ArrayList<Order>()
    fun setOrderList(orderList: List<Order>) {
        this.orderList = orderList
    }

    //View 配置方式
    //先撰寫資料源
    class OrderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val key: TextView = view.tv_key
        private val userName: TextView = view.tv_userName
        private val allTicket: TextView = view.tv_allTickets
        private val roundTrip: TextView = view.tv_roundTrip
        private val oneWay: TextView = view.tv_oneWay
        private val total: TextView = view.tv_total
        fun bind(order: Order) {
            key.text = "key : ${order.key}"
            userName.text = order.Ticket.userName
            allTicket.text = order.Ticket.allTickets.toString()
            roundTrip.text = order.Ticket.roundTrip.toString()
            oneWay.text = order.Ticket.oneWay.toString()
            total.text = String.format("%,d", order.Ticket.total)

            //讓文字靠右
            total.textAlignment = View.TEXT_ALIGNMENT_TEXT_END
        }
    }

    fun getOrderList(): List<Order> {
        return this.orderList
    }

    //設定要給予的 View
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.order, parent, false)
        return OrderViewHolder(view)
    }

    //設定要丟進去 資料要怎麼擺放
    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
        val order = orderList[position]
        holder.itemView.setOnClickListener {
            listener.onItemClickListener(order)
        }
        holder.itemView.setOnLongClickListener {
            listener.onItemLongClickListener(order)
            true
        }
        holder.bind(order)
    }

    override fun getItemCount(): Int {
        return orderList.size
    }

    interface OrderOnItemClickListener {
        fun onItemClickListener(order: Order)
        fun onItemLongClickListener(order: Order)
    }
}