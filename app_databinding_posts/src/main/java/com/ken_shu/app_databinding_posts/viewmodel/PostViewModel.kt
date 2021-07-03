package com.ken_shu.app_databinding_posts.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.ken_shu.app_databinding_posts.model.Posts

class PostViewModel : ViewModel(){
    var posts = MutableLiveData<MutableList<Posts>>()

    init {
        posts.value = ArrayList()
    }
    fun defaultData(){
        //假設 posts 內容的大小 = 0
        if (posts.value!!.size ==0){
            val faker = Faker()
            var tempList = mutableListOf<Posts>()
            tempList.add(Posts(1,faker.book().title(),faker.book().author()))
            tempList.add(Posts(2,faker.book().title(),faker.book().author()))
            tempList.add(Posts(3,faker.book().title(),faker.book().author()))
            posts.postValue(tempList)
        }
    }

}