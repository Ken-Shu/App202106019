package com.ken_shu

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.ken_shu.model.OpenWeather
import com.ken_shu.service.OpenWeatherService
import com.ken_shu.viewmodel.OpenWeatherViewModel
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    lateinit var viewModel: OpenWeatherViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //建立ViewModle 實體
        viewModel = ViewModelProvider(this).get(OpenWeatherViewModel::class.java)

        viewModel.currentImageURL.observe(this , Observer {
            Picasso.get().load(it).into(iv_icon)
        })

        viewModel.currentLog.observe(this, Observer {
            tv_log.text = it.toString()
        })
        GlobalScope.launch {
            val q = "taoyuan,tw"
            val appid = resources.getString(R.string.app_id)
            val path = resources.getString(R.string.path)
            viewModel.ow= OpenWeatherService(appid,path).getOpenWeather(q)
            runOnUiThread{
                viewModel.currentImageURL.value = viewModel.ow!!.weather_icon_url
                viewModel.currentLog.value = viewModel.ow.toString()
            }
        }
    }

    fun changeOpenWeather(view: View) {
        val q = view.tag.toString()
        Toast.makeText(applicationContext, q, Toast.LENGTH_SHORT).show()

        GlobalScope.launch {
            val appid = resources.getString(R.string.app_id)
            val path = resources.getString(R.string.path)
            viewModel.ow = OpenWeatherService(appid , path).getOpenWeather(q)
            runOnUiThread {
                viewModel.currentImageURL.value = viewModel.ow!!.weather_icon_url.toString()
                viewModel.currentLog.value = viewModel.ow.toString()
            }
        }


    }

}