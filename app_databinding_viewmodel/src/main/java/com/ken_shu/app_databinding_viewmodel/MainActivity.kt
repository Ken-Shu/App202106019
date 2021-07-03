package com.ken_shu.app_databinding_viewmodel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ken_shu.app_databinding_viewmodel.databinding.ActivityMainBinding
import com.ken_shu.app_databinding_viewmodel.viewmodel.PostViewModel

class MainActivity : AppCompatActivity() {
    lateinit var viewModel : PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)

        DataBindingUtil
            .setContentView<ActivityMainBinding>(this , R.layout.activity_main)
            .apply { this.setLifecycleOwner ( this@MainActivity )
            this.vm = viewModel}
    }
}