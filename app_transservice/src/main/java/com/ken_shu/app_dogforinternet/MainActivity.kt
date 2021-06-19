package com.ken_shu.app_dogforinternet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.ken_shu.app_dogforinternet.service.AnimalService
import com.ken_shu.app_dogforinternet.viewmodel.AnimalViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException
import kotlin.random.Random

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: AnimalViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this).get(AnimalViewModel::class.java)

        viewModel.currentImageURL.observe(this, Observer {

            try {
                Picasso.get().load(it).into(iv_album)
                Log.d("MainActivity" , "Picasso.get(iv_album)")
            }catch (e : IllegalArgumentException){
                e.printStackTrace()
                Picasso.get().load(R.drawable.image1).into(iv_album)
            }

        })
        viewModel.currentInfo.observe(this, Observer {

            tv_info.text = it.toString()

        })
        //直接先把資料存在 viewModel.animals
        //讓之後運行更快速 不用每次onClickListener 都在重抓一遍
        GlobalScope.launch {
            val path = resources.getString(R.string.path)

            viewModel.animals = AnimalService(path).getAnimals()
            val n = Random.nextInt(viewModel.animals!!.size)
            viewModel.animal = viewModel.animals!![n]
            runOnUiThread {
                Log.d("MainActivity", "runOnUiThread")
                if (viewModel.animal!!.album_file != null) {
                    viewModel.currentImageURL.value = viewModel.animal!!.album_file
                } else {
                    viewModel.currentImageURL.value =
                        iv_album.setImageResource(R.drawable.image1).toString()
                }

                viewModel.currentInfo.value = viewModel.animal.toString()
            }
        }
        iv_album.setOnClickListener {
            val n = Random.nextInt(viewModel.animals!!.size)
            viewModel.animal = viewModel.animals!![n]
            Log.d("MainActivity" , "setOnClickListener")

            viewModel.currentImageURL.value = viewModel.animal!!.album_file
            viewModel.currentInfo.value = viewModel.animal.toString()
        }
        iv_album.setOnLongClickListener OnLongClickListener@{
            if (tv_info.visibility == View.VISIBLE) {
                tv_info.visibility = View.GONE
            } else {
                tv_info.visibility = View.VISIBLE
            }
            return@OnLongClickListener true
        }
    }
}