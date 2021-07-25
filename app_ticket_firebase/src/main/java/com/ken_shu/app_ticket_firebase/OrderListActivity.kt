package com.ken_shu.app_ticket_firebase

import android.content.Context
import android.content.DialogInterface
import android.graphics.Bitmap
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import com.ken_shu.app_ticket_firebase.adapter.RecyclerViewAdapter
import com.ken_shu.app_ticket_firebase.models.Order
import com.ken_shu.app_ticket_firebase.models.Ticket
import com.ken_shu.app_ticket_firebase.models.TicketsStock
import kotlinx.android.synthetic.main.activity_order_list.*
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*

class OrderListActivity : AppCompatActivity() , RecyclerViewAdapter.OrderOnItemClickListener {
    val database = Firebase.database
    val myRef = database.getReference("ticketsStock")

    lateinit var context: Context
    lateinit var userName: String
    lateinit var recyclerViewAdapter : RecyclerViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_list)
        context = this
        //設定 app title
        title = resources.getString(R.string.activity_order_list_title_text)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //取得使用者名稱 (上一頁傳來的USER NAME)
        userName = intent.getStringExtra("userName").toString()
        //判斷是否有 userName 的資料 設定 app title
        title = if (userName == null || userName.equals("") || userName.equals("null")) {
            String.format(title.toString(), resources.getString(R.string.all_order_text))
        } else {
            String.format(title.toString(), userName)
        }

        //Read from the database
        myRef.addValueEventListener(object : ValueEventListener {
            lateinit var orderList : MutableList<Order>

            //snapshot = ticketsStock 的 children
            override fun onDataChange(snapshot: DataSnapshot) {
                orderList = mutableListOf<Order>()
                val children = snapshot.children
                children.forEach {
                    if (it.key.toString() == "transaction") {
                        //未指名 userName
                        if (userName == null || userName.equals("") || userName.equals("null")) {
                            it.children.forEach { record ->
                                    addRecord(it, record.key.toString())
                                }
                            } else {
                                //有指名 userName
                                addRecord(it, userName)
                            }
                        }
                    }
                    tv_info.text = orderList.toString()
                //更新 recycler view 的 orderList 資料
                recyclerViewAdapter.setOrderList(orderList)
                // 通資變更(刷新)
                recyclerViewAdapter.notifyDataSetChanged()
                }


            private fun addRecord(it: DataSnapshot, userName: String) {
                try { //解決按下記錄之後返回訂票成功之後會發生null 的問題
                    it.child(userName).children.forEach {
                        tv_info.text = tv_info.text.toString() + it.toString()
                        val key = it.key.toString()
                        val allTickets = it.child("allTickets").value.toString().toInt()
                        val roundTrip = it.child("roundTrip").value.toString().toInt()
                        val oneWay = it.child("oneWay").value.toString().toInt()
                        val total = it.child("total").value.toString().toInt()
                        val ticket = Ticket(userName, allTickets, roundTrip, oneWay, total)
                        val order = Order(key, ticket)
                        orderList.add(order)
                    }
                }catch (e:Exception){

                }

            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

        //Init RecyclerView
        recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            recyclerViewAdapter = RecyclerViewAdapter(this@OrderListActivity)
            adapter = recyclerViewAdapter
        }
    }

    //產生QR-Code
    private fun getQRCode(order: Order){
        //產生 Json 透過Gson 將 order 的物件 轉成 Json字串
        val orderJsonString = Gson().toJson(order).toString()
        Toast.makeText(context,"click:"+order.toString(),Toast.LENGTH_SHORT).show()
        //產生QR Code
        val writer = QRCodeWriter()
        //產生 bit 矩陣資料
        //encode 要甚麼轉成 BarcodeFormat.QR_CODE 橡數 512*512
        val bitMatrix =writer.encode(orderJsonString , BarcodeFormat.QR_CODE , 512 ,512)
        val width = bitMatrix.width
        val height = bitMatrix.height

        //產生 bitmap (圖像)空間
        val bitmap = Bitmap.createBitmap(width,height,Bitmap.Config.RGB_565)

        //將 bitMatrix 矩陣資料 注入到 bitmap 空間
        for (x in 0 until width){
            for (y in 0 until height){
                //有資料放黑色 , 無資料放白色
                bitmap.setPixel(x,y ,if (bitMatrix.get(x,y))Color.BLACK else Color.WHITE)
            }
        }
        //建立 自製 ImageView
        val qrcodeImageView = ImageView(context)
        qrcodeImageView.setImageBitmap(bitmap)
        //建立 AlterDialog 顯示 bitmap 圖像
        AlertDialog.Builder(context)
            .setView(qrcodeImageView)
            .create()
            .show()
    }

    //使用票卷
    private fun useTickest(order: Order){

            val result_txt = Gson().toJson(order,Order::class.java) //解碼內容
            AlertDialog.Builder(context)
                .setTitle("QRCode內容")
                .setMessage("${result_txt}")
                .setPositiveButton("使用",{
                        dialogInterface , i ->
                    //取的該票路徑
                    val path = order.Ticket.userName+ "/" + order.key
                    val transPath = "transaction/" + path
                    //刪除該票
                    myRef.child(transPath).removeValue()
                    //建立 transaction_history
                    val ts = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date()).toString()
                    myRef.child("transaction_history/${path}/ts").setValue(ts)
                    myRef.child("transaction_history/${path}/key").setValue(order.key)
                    myRef.child("transaction_history/${path}/userName").setValue(order.Ticket.userName)
                    myRef.child("transaction_history/${path}/allTickets").setValue(order.Ticket.allTickets)
                    myRef.child("transaction_history/${path}/oneWay").setValue(order.Ticket.oneWay)
                    myRef.child("transaction_history/${path}/roundTrip").setValue(order.Ticket.roundTrip)
                    myRef.child("transaction_history/${path}/total").setValue(order.Ticket.total)
                    myRef.child("transaction_history/${path}/json").setValue(result_txt)
                    //結束
                    finish()
                })
                .setNegativeButton("取消" , DialogInterface.OnClickListener{
                        dialogInterface , i -> finish()
                })
                .create()
                .show()

    }


    //按一下產生QR-code 或 使用票卷(後臺專用)
    override fun onItemClickListener(order: Order) {
      //沒有 userName -> 後臺專用
        if (userName == null || userName.equals("") || userName.equals("null")){
            useTickest(order)
        }else {
            getQRCode(order)
        }
    }
    //長按一下可以取消訂單(退票)
    override fun onItemLongClickListener(order: Order) {
        //組合訂單路徑
        val path = "transaction/" + order.Ticket.userName + "/" + order.key

        AlertDialog.Builder(context)
            .setTitle("票務處置")
            .setMessage("票券 : ${order.Ticket.userName} [${order.key}]")
            .setPositiveButton("退票"){ dialog, which ->
                //刪除訂單路徑資料
                myRef.child(path).removeValue()
                //加回所退回的數量
                //firebase'totalAmount 要加回所退回資料 (order.Ticket.allTickets)
                //針對單一欄位來抓值
                myRef.child("totalamount").addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                            val totalAmount = snapshot.value.toString().toInt()
                            val newtotalAmount = totalAmount + order.Ticket.allTickets
                            myRef.child("totalamount").setValue(newtotalAmount)

                    }
                    override fun onCancelled(error: DatabaseError) {
                    }
                })
                //寫入退票紀錄檔
                //firebase "transaction_refund"
                val sdf = SimpleDateFormat("yyyyMMddHHmmssSSS")
                val dateString = sdf.format(Date())
                val orderJsonString = Gson().toJson(order).toString()
                myRef.child("reansaction_refund/" + dateString +"/json").setValue(orderJsonString)
                myRef.child("reansaction_refund/" + dateString +"/order/key").setValue(order.key)
                myRef.child("reansaction_refund/" + dateString +"/order/ticket/userName").setValue(order.Ticket.userName)
                myRef.child("reansaction_refund/" + dateString +"/order/ticket/allTickets").setValue(order.Ticket.allTickets)
                myRef.child("reansaction_refund/" + dateString +"/order/ticket/roundTrip").setValue(order.Ticket.roundTrip)
                myRef.child("reansaction_refund/" + dateString +"/order/ticket/oneWay").setValue(order.Ticket.oneWay)
                myRef.child("reansaction_refund/" + dateString +"/order/ticket/total").setValue(order.Ticket.total)
            }
            .setNegativeButton("取消",null)
            .create()
            .show()
        Toast.makeText(context,"long click:"+order.toString(),Toast.LENGTH_SHORT).show()
    }
//根據tickets total 來排序
    fun ticket_total_Sort(view: View){
    //▲▼
        Order.orderDelta *=-1
        if (Order.orderDelta == -1){
            (view as TextView).text = resources.getString(R.string.total_text)+ "▼"
        }else{
            (view as TextView).text = resources.getString(R.string.total_text)+ "▲"
        }
        val orderList = recyclerViewAdapter.getOrderList()
        Collections.sort(orderList)
        recyclerViewAdapter.notifyDataSetChanged()
    }

}