package com.ken_shu.app_databinding_basic

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.ken_shu.app_databinding_basic.model.Post
//根據 layout 配置是否支援 databinding 來動態產生
import com.ken_shu.app_databinding_basic.databinding.ActivityMainBinding //動態產生
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var mBinding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //使用databinding 就不用使用 setContentView
        //取代傳統 setContentView(R.layout.activity_main) 的配置
        //setContentView(R.layout.activity_main)
        mBinding = DataBindingUtil.setContentView(this,R.layout.activity_main)

    //建立資料
    val post = Post(12,"Kotlin","Java")
    //Binding data 繫結資料
    mBinding.post = post
    }
    fun click(view: View){
        val post = Post(Random.nextInt(100),"Python","Vincent")
        Toast.makeText(applicationContext,post.toString(),Toast.LENGTH_SHORT).show()
    }
}