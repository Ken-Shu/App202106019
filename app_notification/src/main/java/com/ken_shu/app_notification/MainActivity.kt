package com.ken_shu.app_notification

import android.app.Notification
import android.app.PendingIntent
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    //NotificationManagerCompat 多一個 compat 就是可以向下相容的版本
    var notificationManager: NotificationManagerCompat? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //建立實體 建構 notificationManager 把 this 這個環境 給他
        notificationManager = NotificationManagerCompat.from(this)

        //一旦進入 APP 先刪除 1001
        //notificationManager!!.cancel(1001)
    }

    fun updateByChannel1(view : View){
        //根據相同的id 在發送一次
        val id = 1001
        val intent = Intent(this , MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT) //設定轉跳 按下通知時 轉跳回APP
        //PendingIntent.FLAG_UPDATE_CURRENT 轉跳時 即時更新資訊
        val title : String = et_title.text.toString()
        val message : String = et_message.text.toString()
        val notification : Notification = NotificationCompat.Builder(this , App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true) //讓通知無法移除 只能從設定內調整
            .setContentTitle(title)
            .setContentText(message)
            .setSubText("continue...") //取代掉 ContentInfo 顯示資訊
            .setContentIntent(pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 紀錄資訊
            .build()
        notificationManager!!.notify(id, notification) // id 是 這個通知的 id
        // 如果兩個id 重複 則在通知時 會覆蓋前面所通知內容
    }

    //連續產生
    fun continueByChannel2(view : View){
        for (i in 1..5) {
            val title: String = et_title.text.toString() + " : " + i.toString()
            val message: String = et_message.text.toString() + " : " + i.toString()
            val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_2_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 紀錄資訊
                .build()
            notificationManager!!.notify(i, notification) // id 是 這個通知的 id
        }
    }

    fun deleteByChannel1(view : View){
        notificationManager!!.cancel(1001)

    }

    fun sendByChannel1(view: View) {
        val intent = Intent(this , MainActivity::class.java)
        val pIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT) //設定轉跳 按下通知時 轉跳回APP
        //PendingIntent.FLAG_UPDATE_CURRENT 轉跳時 即時更新資訊
        val title : String = et_title.text.toString()
        val message : String = et_message.text.toString()
        val notification : Notification = NotificationCompat.Builder(this , App.CHANNEL_1_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_alert)
            .setOngoing(true) //讓通知無法移除 只能從設定內調整
            .setContentTitle(title)
            .setContentText(message)
            .setSubText("2021-7-3 by channel1") //取代掉 ContentInfo 顯示資訊
            .setContentIntent(pIntent)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 紀錄資訊
            .build()
        notificationManager!!.notify(1001, notification) // id 是 這個通知的 id
    // 如果兩個id 重複 則在通知時 會覆蓋前面所通知內容
    }

    fun sendByChannel2(view: View) {
        val title : String = et_title.text.toString()
        val message : String = et_message.text.toString()
        val notification : Notification = NotificationCompat.Builder(this , App.CHANNEL_2_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setCategory(NotificationCompat.CATEGORY_MESSAGE) // 紀錄資訊
            .build()
        notificationManager!!.notify(1002, notification) // id 是 這個通知的 id
    }
}