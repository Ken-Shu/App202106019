package com.ken_shu.app_notification_firebase

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class App : Application(){
    //頻道
    companion object {
        val CHANNEL_1_ID = "channel1"
        val CHANNEL_2_ID = "channel2"
    }
    override fun onCreate() {
        super.onCreate()
        createNotificationChannels()
    }

    fun createNotificationChannels(){

        /*此段主要功能在註冊channel*/
        //判斷版本 (Android 8.0 (SDK26) 以上才有支援 channel)
        //Build.VERSION_CODES.O 這裡的 O 代表 8.0 版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel1 = NotificationChannel(
                //NotificationManager.IMPORTANCE_HIGH 重要性最高
                //頻道1 , 給名稱名稱 , 通知級別
                CHANNEL_1_ID, "Channel 1", NotificationManager.IMPORTANCE_HIGH
            )
            //頻道1 的 敘述
            channel1.description = "This is channel 1"
            val channel2 = NotificationChannel(
                //NotificationManager.IMPORTANCE_HIGH 重要性最高
                //頻道1 , 給名稱名稱 , 通知級別
                CHANNEL_2_ID, "Channel 2", NotificationManager.IMPORTANCE_HIGH
            )
            //頻道1 的 敘述
            channel1.description = "This is channel 2"
            // 註冊 channel
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel1)
            manager.createNotificationChannel(channel2)
        }

    }
}