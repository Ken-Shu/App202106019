package com.ken_shu.app_databinding_viewmodel.viewmodel

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.javafaker.Faker
import com.ken_shu.app_databinding_viewmodel.model.Post
import java.util.Random

class PostViewModel : ViewModel() {
    var post = MutableLiveData<Post>()

    fun click(){
        val jf = Faker()
        post.value = Post(Random().nextInt(100), jf.book().title(), jf.book().author())

    }
    fun click2(view : View){
        val jf = Faker()
        post.value = Post(Random().nextInt(100), jf.book().title(), jf.book().author())
    }
}