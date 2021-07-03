package com.ken_shu.app_tictactoe_firebase

import android.app.Notification
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val database = Firebase.database
    var notificationManager : NotificationManagerCompat? = null
    val myTTTRef = database.getReference("ttt/game")
    val myTTTLastMarkRef = database.getReference("ttt/last_mark")
    val myTTTWinnerRef = database.getReference("ttt/winner")
    lateinit var context: Context
    lateinit var lastMark: String

    //var buttons = mutableListOf<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this

        // 建構 notificationManager
        notificationManager = NotificationManagerCompat.from(this)


        bt_mark.setOnClickListener {
            var mark = bt_mark.tag.toString()
            if (mark.equals("O")) {
                bt_mark.text = "X"
                bt_mark.tag = "X"
            } else {
                bt_mark.text = "O"
                bt_mark.tag = "O"
            }
        }
        //觀察者
        myTTTRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val game = snapshot.children
                //Toast.makeText(context , game.toString(),Toast.LENGTH_SHORT).show()
                game.forEach {
                    Log.d("MainActivity", it.key + " : " + it.value)
                    //透過 it.key 值 的 id
                    val id = resources.getIdentifier(it.key, "id", context.packageName)
                    //再透過 id 值去尋找 再放進去 it.value 裡面
                    findViewById<Button>(id).text = it.value.toString()
                }
                //判斷贏家
                winner()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
        myTTTLastMarkRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                lastMark = snapshot.value.toString()
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
        myTTTWinnerRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                val winner = snapshot.value.toString()
                if(!winner.equals("")){
                    sendByChannel(winner)
                }
            }


            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun tttonClick(view: View) {
        var mark = bt_mark.tag.toString()
        if (mark.equals(lastMark)) {
            return
        }
        val tag = view.getTag().toString()
        val path = "b" + tag
        myTTTRef.child(path).setValue(mark)
        myTTTLastMarkRef.setValue(mark)
    }

    fun tttResetOnClick(view: View) {
        for (i in 1..9) {
            val path = "b" + i
            myTTTRef.child(path).setValue("")
        }
        myTTTLastMarkRef.setValue("")
        myTTTWinnerRef.setValue("")
    }

    fun winner(){
        var winner : String? = null
        val mark = bt_mark.text.toString()
        if (b1.text.toString().equals(mark) && b2.text.toString().equals(mark) && b3.text.toString().equals(mark)){
            winner = mark
        }else if (b4.text.toString().equals(mark) && b5.text.toString().equals(mark) && b6.text.toString().equals(mark)){
            winner = mark
        }else if (b7.text.toString().equals(mark) && b8.text.toString().equals(mark) && b9.text.toString().equals(mark)){
            winner = mark
        }else if (b1.text.toString().equals(mark) && b4.text.toString().equals(mark) && b7.text.toString().equals(mark)){
            winner = mark
        }else if (b2.text.toString().equals(mark) && b5.text.toString().equals(mark) && b8.text.toString().equals(mark)){
            winner = mark
        }else if (b3.text.toString().equals(mark) && b6.text.toString().equals(mark) && b9.text.toString().equals(mark)){
            winner = mark
        }else if (b1.text.toString().equals(mark) && b5.text.toString().equals(mark) && b9.text.toString().equals(mark)){
            winner = mark
        }else if (b3.text.toString().equals(mark) && b5.text.toString().equals(mark) && b7.text.toString().equals(mark)) {
            winner = mark
        }
        if (winner != null){
            myTTTWinnerRef.setValue(mark)
        }
    }
    fun sendByChannel(message : String ){
        val intent = Intent(this,MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT)
        val title : String = "Firebase"
        val notification: Notification =NotificationCompat.Builder(this,App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true)
            .setContentTitle("Firebase 井字遊戲")
            .setContentText("Winner: " + message + " 贏了")
            .setSubText("2021-7-3")
            .setContentIntent(pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
            .build()

        notificationManager!!.notify(1001,notification)
    }
}