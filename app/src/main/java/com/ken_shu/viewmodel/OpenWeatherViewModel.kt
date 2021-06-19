package com.ken_shu.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ken_shu.model.OpenWeather

class OpenWeatherViewModel :ViewModel() {
    var ow : OpenWeather ? = null
    val currentImageURL : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currentLog : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}