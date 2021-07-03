package com.ken_shu.app_notification_firebase

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    var notificationManager: NotificationManagerCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        notificationManager = NotificationManagerCompat.from(this)
        // Write a message to the database
        val database = Firebase.database
        val myMessageRef = database.getReference("message")

        bt_submit.setOnClickListener {
            val data = et_message.text.toString()

            myMessageRef.setValue(data)
        }
        // Read from the database
        myMessageRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                val value = snapshot.getValue<String>()
                tv_message.setText(value)
                sendByChannel1(value.toString())
                Log.d(TAG, "Value is: " + value)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })

    }
    fun sendByChannel1(message:String) {
        val intent = Intent(this , MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this,0,intent, PendingIntent.FLAG_UPDATE_CURRENT) //設定轉跳 按下通知時 轉跳回APP
        //PendingIntent.FLAG_UPDATE_CURRENT 轉跳時 即時更新資訊
        val title : String = "Firebase"
        val message : String = et_message.text.toString()
        val notification : Notification = NotificationCompat.Builder(this , App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true) //讓通知無法移除 只能從設定內調整
            .setContentTitle(title)
            .setContentText(message)
            .setSubText("2021-7-3") //取代掉 ContentInfo 顯示資訊
            .setContentIntent(pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 紀錄資訊
            .build()
        notificationManager!!.notify(1001, notification) // id 是 這個通知的 id
        // 如果兩個id 重複 則在通知時 會覆蓋前面所通知內容
    }
}