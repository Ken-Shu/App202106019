package com.ken_shu.app_dogforinternet.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ken_shu.app_dogforinternet.model.Animal

class AnimalViewModel : ViewModel(){
    var animals : List<Animal> ?= null
    var animal : Animal ?= null
    val currentImageURL : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
    val currentInfo : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}