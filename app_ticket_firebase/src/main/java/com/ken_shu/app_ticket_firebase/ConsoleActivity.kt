package com.ken_shu.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_console.*
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.lang.Exception
import java.nio.charset.Charset

class ConsoleActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myRef = database.getReference("ticketsStock")

    private lateinit var userName : String
    private lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_console)
        context = this

        //讓 tv_totalamount 實現 selected 以便實現跑馬燈
        tv_totalamount.isSelected = true
        //Get userName
        userName = intent.getStringExtra("userName").toString()

        //把userName 丟進去 title 的 %s 裡面 寫入 activity title
        title = String.format(title.toString(), userName)

        //讓最上方Bar 左方產生倒回箭頭
        //然後再 AndroidManifest 裡的 MainActivity 加上
        //android:parentActivityName=".LoginActivity" 此為返回頁面
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //Read form the database
        myRef.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                //統計資料累計
                var sumAllTickets = 0
                var sumOneWay = 0
                var sumRoundTrip = 0
                var sumTotal = 0
                //個別訂購人的統計資料列表
                //[{'John' , 100},{'Helen' , 50}] Map
                var statListByUser = mutableListOf<Map<String , Int>>()

                //--- for Each begin ---------------------
                children.forEach {
                    when(it.key.toString()){
                        resources.getString(R.string.fb_discount) -> et_discount.setText(it.value.toString())
                        resources.getString(R.string.fb_price) -> et_price.setText(it.value.toString())
                        resources.getString(R.string.fb_totalamount) -> et_totalamount.setText(it.value.toString())
                        "transaction" -> {
                            it.children.forEach { //取的訂購人 ex:John
                                //當前訂購人與訂購總金額
                                var mapUser = mutableMapOf<String , Int>()
                                //當前訂購人名
                                val mapUserName = it.key.toString()
                                //預設當前訂購人與訂購總金額
                                mapUser.put(mapUserName , 0)

                                it.children.forEach { //取的訂票日期 ex :20210724091850378
                                    it.children.forEach { //取的 訂票內容
                                        when(it.key.toString()){
                                            "allTickets" -> sumAllTickets += it.value.toString().toInt()
                                            "oneWay" -> sumOneWay += it.value.toString().toInt()
                                            "roundTrip" -> sumRoundTrip += it.value.toString().toInt()
                                            "total" -> {
                                                val total = it.value.toString().toInt()
                                                sumTotal += total
                                                //累計當前訂購人的訂購總金額
                                                mapUser.put(mapUserName , mapUser.get(mapUserName)!! + total)
                                            }
                                        }
                                    }
                                }
                                //將訂購人與訂購總金額(mapUser)放入個別訂購人的統計資料列表(statListByUser)
                                statListByUser.add(mapUser)
                            }
                        }
                    }
                }
                //--- for Each End -----------------
                //顯示統計資料
                tv_stat.text =
                        "總賣票數 : ${String.format("%,d" , sumAllTickets)} 張 \n"+
                        "總單程票 : ${String.format("%,d" , sumOneWay)} 張 \n"+
                        "總來回票 : ${String.format("%,d" , sumRoundTrip)} 張 \n"+
                        "總銷售額 : ${String.format("%,d" , sumTotal)} 元 "
                //顯示統計圖
                loadChart(statListByUser)
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    private fun loadChart(statListByUser : List<Map<String , Int>>){
        //將 "[{'AAA', 10},{'BBB', 20}]"
        //轉成 "['AAA', 10],['BBB', 20]"
        var args : String = ""
        statListByUser.forEach {
            val key = it.keys.iterator().next() // KeySet 透過 iterator(輪巡)
            val value = it[key]
            args += "['$key',$value],"
        }
        var webSettings = web_view.settings
        webSettings.javaScriptEnabled = true
        webSettings.builtInZoomControls = true
        var asset_path = "file:///android_assets/"
        var html =getHTML("chart.html")
        html = String.format(html!! ,args )

        web_view.loadDataWithBaseURL(asset_path , html!! ,"text/html", "UTF-8" , null)
        web_view.requestFocusFromTouch()
    }

    private fun getHTML ( filename  : String ) : String?{
        var html : String? = null
        try {
            val inputstream : InputStream = assets.open(filename)
            val out = ByteArrayOutputStream()
            val buffer = ByteArray(inputstream.available())
            inputstream.read(buffer) //資料讀出
            out.write(buffer) // 資料寫入
            html = String(out.toByteArray() , Charset.forName("UTF-8"))
        }catch (e : Exception){
            e.printStackTrace()
        }
        return html
    }

    fun update(view : View){
        val tagName = view.tag.toString()
        Toast.makeText(context ,tagName,Toast.LENGTH_SHORT).show()
        var value = 0.0
        when(tagName){
            resources.getString(R.string.fb_discount) -> value = et_discount.text.toString().toDouble()
            resources.getString(R.string.fb_price) -> value = et_price.text.toString().toDouble()
            resources.getString(R.string.fb_totalamount) -> value = et_totalamount.text.toString().toDouble()
        }
        //update firebase
        myRef.child(tagName).setValue(value)
        val msg = String.format(resources.getString(R.string.updateOK_text),tagName)
        Toast.makeText(context, msg ,Toast.LENGTH_SHORT).show()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //group_id , item_id , order_id , name
        menu?.add(1,1,1,"[ - ]")?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
        menu?.add(1,9,5,"Back")
        menu?.add(1,2,2,"購票紀錄")
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            1 -> {
                //啟動QRCodeActivity
                val intent = Intent(context , QRCodeActivity::class.java)
                startActivity(intent)
            }
            2 -> {
                //啟動 OrderListActivity
                val intent = Intent(context , OrderListActivity::class.java)
                startActivity(intent)
            }
            9 -> {
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}