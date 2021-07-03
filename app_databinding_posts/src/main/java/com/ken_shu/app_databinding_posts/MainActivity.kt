package com.ken_shu.app_databinding_posts

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.javafaker.Faker
import com.ken_shu.app_databinding_posts.manager.PostManager
import com.ken_shu.app_databinding_posts.model.Posts
import com.ken_shu.app_databinding_posts.viewmodel.PostViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: PostViewModel
lateinit var posts : List<Posts>
var count = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        viewModel = ViewModelProvider(this).get(PostViewModel::class.java)
        viewModel.posts.observe(this, Observer {
            /*GlobalScope.launch {
                recyclerView.adapter=PostAdapter(it)
                //it 要觀察者的資料
*/
            recyclerView.adapter = PostAdapter(it)

            recyclerView.adapter?.notifyDataSetChanged()

        //}
        })

    }

    fun add(view: View) {
        GlobalScope.launch {
            val api = PostManager.instance.api
            var id = 0
            val faker = Faker()
            count = ++count
            var post = Posts(count, faker.book().title(), faker.book().author())
            posts = mutableListOf(post)
            id += posts.size
            //.body()是新增完後回傳給你新增的資料
            var status = api.addPost(post).execute().isSuccessful//status 是 一整個 Posts 的每一項post資料
            Log.d("MainActivity",id.toString()+"=========id")
            Log.d("MainActivity",status.toString()+"++id++")
            runOnUiThread {
                //透過判斷式觸發觀察者模式
                if (viewModel.posts.value!!.add(posts[id-1])){
                if(status.toString().equals("false")){
                    Toast.makeText(applicationContext,"已存在資料",Toast.LENGTH_SHORT).show()
                    return@runOnUiThread
                }else {
                    //viewModel.posts.value!!.add(posts[id - 1]) //如果上方沒加判斷式 會變成 按List之後 不會更新畫面
                    viewModel.posts.value = viewModel.posts.value
                    recyclerView.adapter?.notifyDataSetChanged()
               }
                }
            }
        }
    }

    //重新整理鍵
    fun list(view: View) {
        GlobalScope.launch {
            val api = PostManager.instance.api
             posts= api.getposts().execute().body()!!
            runOnUiThread {

                recyclerView.adapter = PostAdapter(posts)

            }
        }
    }

    class PostAdapter(private var list: List<Posts>) :
        RecyclerView.Adapter<PostAdapter.ViewHolder>() {


        inner class ViewHolder(var dataBinding: ViewDataBinding) :
            RecyclerView.ViewHolder(dataBinding.root)

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            return ViewHolder(
                DataBindingUtil.inflate
                    (
                    LayoutInflater.from(parent.context),
                    R.layout.recyclerview_row, parent, false
                )
            )
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val binding: ViewDataBinding = holder.dataBinding
            // 数据绑定库在模块包中生成一个名为 BR 的类，其中包含用于数据绑定的资源的 ID。
            binding.setVariable(BR.post, list[position])

        }

        override fun getItemCount(): Int {
            return list.size
        }

    }

}
