package com.ken_shu.app_databinding_recyclerview

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.javafaker.Faker
import com.ken_shu.app_databinding_recyclerview.model.Post
import com.ken_shu.app_databinding_recyclerview.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.internal.notify

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel : PostViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutMandager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutMandager

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        viewModel.posts.observe(this, Observer { //Observer 觀察者模式
            //it 要觀察者的資料 把 PostAdapter 內容 丟進去 recyclerView 裡面
            recyclerView.adapter = PostAdapter(it)
        })
        viewModel.defaultData()
    }

    fun add(view: View) {
        val faker = Faker()
        val id = viewModel.posts.value!!.size+1
        val post = Post(id , faker.book().title(),faker.book().author())
        viewModel.posts.value?.add(post)

        recyclerView.adapter?.notifyDataSetChanged()
    }

    //回傳要是一個RecyclerView.Adapter 型別是<PostAdapter.ViewHolder> 的方法
    class PostAdapter(private var list : List<Post>)
        : RecyclerView.Adapter<PostAdapter.ViewHolder>(){


        inner class ViewHolder(var dataBinding : ViewDataBinding) : RecyclerView.ViewHolder(dataBinding.root)
        // 連結項目布局檔list_item
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(DataBindingUtil.inflate
                (LayoutInflater.from(parent.context),
                R.layout.recyclerview_row,parent,false))
        }
        // 設置Item要顯示的內容
        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val binding : ViewDataBinding= holder.dataBinding
            // 数据绑定库在模块包中生成一个名为 BR 的类，其中包含用于数据绑定的资源的 ID。
            binding.setVariable(BR.post,list[position])

        }

        override fun getItemCount(): Int {
            return list.size
        }

    }
}