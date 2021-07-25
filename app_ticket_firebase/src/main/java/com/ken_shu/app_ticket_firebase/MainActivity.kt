package com.ken_shu.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ken_shu.app_ticket_firebase.models.TicketsStock
import com.ken_shu.app_ticket_firebase.services.TicketServices
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    val database = Firebase.database
    val myRef = database.getReference("ticketsStock")


    lateinit var ticketsStock: TicketsStock
    lateinit var context: Context
    lateinit var userName: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this
        //讓最上方Bar 左方產生倒回箭頭
        //然後再 AndroidManifest 裡的 MainActivity 加上
        //android:parentActivityName=".LoginActivity"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Get userName
        userName = intent.getStringExtra("userName").toString()

        //把userName 丟進去 title 的 %s 裡面
        title = String.format(title.toString(), userName)

        //new TicketsStock
        ticketsStock = TicketsStock(0.0, 0, 0)

        // Read from the database
        myRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                children.forEach {
                    when (it.key.toString()) {
                        "discount" -> {
                            ticketsStock.discount = it.value.toString().toDouble()
                        }
                        "price" -> {
                            ticketsStock.price = it.value.toString().toInt()
                        }
                        "totalamount" -> {
                            ticketsStock.totalamount = it.value.toString().toInt()
                        }
                    }
                }
                //update ui
                refreshUI()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show()

            }

        })

        //update 結帳資訊的內容
        var result =
            resources.getString(R.string.submit_detail_txt)// 總張數:%d\n來回票:%d\n單程票:%d\n總金額:$%d\n
        tv_result.text = String.format(result, 0, 0, 0, 0)

        //clear 訂單資料
        clear(null)
    }

    fun refreshUI() {
        tv_ticket_discount.text = ticketsStock.discount.toString()
        tv_ticket_price.text = ticketsStock.price.toString()
        tv_totalamount.text = ticketsStock.totalamount.toString()
    }

    //清除按鈕
    fun clear(view: View?) {
            //清除結帳內容
            var result =
                resources.getString(R.string.submit_detail_txt)// 總張數:%d\n來回票:%d\n單程票:%d\n總金額:$%d\n
            tv_result.text = String.format(result, 0, 0, 0, 0)
            //清除購買張數 跟 來回組數
            et_all_tickets.setText("0") //購買張數 清除
            et_round_trip.setText("0") //來回組數 清除
            tv_warning.text = "" //警告訊息 清除
    }

    //購票流程(按下購買鈕)
    fun buyTicket(view: View) {
        //檢查UI欄位資訊
        //isNaN = is Not a Number
        if(et_all_tickets.text.toString().length == 0){
            //et_all_tickets.setText("0")
            return
        }
        if(et_round_trip.text.toString().length == 0){
            //et_all_tickets.setText("0")
            return
        }
        //進行購票
        val allTickets = et_all_tickets.text.toString().toInt()
        val roundTrip = et_round_trip.text.toString().toInt()

        try {
            TicketServices.errorMessages = resources.getStringArray(R.array.exception_message_array)
            val ticket = TicketServices().submit(allTickets, roundTrip, userName, ticketsStock)
            if (ticket != null) {
                var result = resources.getString(R.string.submit_detail_txt)

                tv_result.text = String.format(
                    result,
                    ticket.allTickets,
                    ticket.oneWay,
                    ticket.roundTrip,
                    ticket.total.toInt()
                )
                //通知 firebase---------------
                // 1.變更 totalAmount 剩餘張數
                val updateAmount = ticketsStock.totalamount - ticket.allTickets
                myRef.child("totalamount").setValue(updateAmount)

                // 2. 新增訂單紀錄資料
                val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS")
                val dateString = sdf.format(Date())
                val subPath = "transaction/" + userName + "/" + dateString + "/"
                myRef.child(subPath + "allTickets").setValue(ticket.allTickets)
                myRef.child(subPath + "oneWay").setValue(ticket.oneWay)
                myRef.child(subPath + "roundTrip").setValue(ticket.roundTrip)
                myRef.child(subPath + "total").setValue(ticket.total)

                //購買成功訊息
                tv_warning.text = resources.getString(R.string.warning_txt)
            }
        } catch (e: Exception) {
            tv_warning.text = e.message
        }

    }

    //紀錄按鈕
    fun viewOrderList(view : View){
        val intent = Intent(context , OrderListActivity::class.java)
        //將userName 代給 orderListActivity
        intent.putExtra("userName" , userName)
        startActivity(intent)
    }

}