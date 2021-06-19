package com.ken_shu.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ken_shu.retrofit.manager.RetrofitManager
import com.ken_shu.retrofit.model.Comments
import com.ken_shu.retrofit.model.Photo
import com.ken_shu.retrofit.model.Post
import com.ken_shu.retrofit.model.users.User
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    lateinit var recyclerViewAdapter : RecyclerViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            //建立 recyclerViewAdapter 實體
            recyclerViewAdapter = RecyclerViewAdapter()
            //再把實體給 adapter
            adapter = recyclerViewAdapter
            val divider =  DividerItemDecoration(applicationContext, StaggeredGridLayoutManager.VERTICAL)
            addItemDecoration(divider)
        }

        //因為要建立執行緒
        GlobalScope.launch {
            val api = RetrofitManager.instance.api


            //方法一:
            //execute 執行
            //val postList = call.execute().body()
            //Log.d("MainActivity" , postList.toString())

            //方法二:
            //call.enqueue(getPosts())
            btn_posts.setOnClickListener {
                //api.getPosts().enqueue(getPost())
                recyclerView.visibility = View.GONE
                ns_view.visibility = View.VISIBLE
                api.getPosts(2).enqueue(getPost())
                //api.getPosts(2 ,"id","desc").enqueue(getPost())
                //取得複數ID
                //api.getPosts(arrayOf(2,4),"id","desc").enqueue(getPosts())
            }
            btn_comments.setOnClickListener {
                recyclerView.visibility = View.GONE
                ns_view.visibility = View.VISIBLE
                //api.getComments().enqueue(getComments())
                //api.getComments("/posts/3/comments").enqueue(getComments())
                api.getComments(3).enqueue(getComments())
                val params = HashMap<String , String >()
                params.put("postId","4")
                params.put("_sort","id")
                params.put("_order","desc")
                api.getComments(params).enqueue(getComments())
            }
            btn_users.setOnClickListener {
                recyclerView.visibility = View.GONE
                ns_view.visibility = View.VISIBLE
                api.getUsers().enqueue(getUsers())
                //api.getUsers(1).enqueue(getUser())
            }
            btn_photos.setOnClickListener {
                recyclerView.visibility = View.VISIBLE
                ns_view.visibility = View.GONE
                api.getPhotos().enqueue(getPhotos())
            }
        }
    }

    fun getUser() : Callback<User>{
        val cb = object :Callback<User>{
            //server 端有回應 但回應可能是正確或是錯誤的
            override fun onResponse(call: Call<User>, response: Response<User>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val users = response.body()
                Log.d("MainActivity",users.toString())

                //UI呈現
                runOnUiThread {
                    tv_posts.text = users.toString()+"\n"+users.toString()
                }
            }
            //無法連線 ex : 404
            override fun onFailure(call: Call<User>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }
        }
        return cb
    }

    fun getUsers() : Callback<List<User>>{
        val cb = object  : Callback<List<User>>{
            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val users = response.body()
                Log.d("MainActivity",users.toString())
                Log.d("MainActivity",users!!.size.toString())
                //UI呈現
                runOnUiThread {
                    tv_posts.text = users.size.toString()+"\n"+users.toString()
                }
            }
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }

        }
        return cb
    }


    fun getPosts() : Callback<List<Post>>{
        val cb = object :Callback<List<Post>>{
            //server 端有回應 但回應可能是正確或是錯誤的
            override fun onResponse(call: Call<List<Post>>, response: Response<List<Post>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val posts = response.body()
                Log.d("MainActivity",posts.toString())
                Log.d("MainActivity",posts!!.size.toString())
                //UI呈現
                runOnUiThread {
                    tv_posts.text = posts.toString()+"\n"+posts.toString()
                }
            }
            //無法連線 ex : 404
            override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }
        }
        return cb
    }
    fun getComments() : Callback<List<Comments>>{
        val cb = object  : Callback<List<Comments>>{
            override fun onResponse(
                call: Call<List<Comments>>,
                response: Response<List<Comments>>
            ) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val comments = response.body()
                Log.d("MainActivity",comments.toString())
                Log.d("MainActivity",comments!!.size.toString())
                //UI呈現
                runOnUiThread {
                    tv_posts.text = comments.size.toString()+"\n"+comments.toString()
                }
            }

            override fun onFailure(call: Call<List<Comments>>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }

        }
        return cb
    }
    fun getPost() : Callback<Post>{
        val cb = object :Callback<Post>{
            //server 端有回應 但回應可能是正確或是錯誤的
            override fun onResponse(call: Call<Post>, response: Response<Post>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val posts = response.body()
                Log.d("MainActivity",posts.toString())

                //UI呈現
                runOnUiThread {
                    tv_posts.text = posts.toString()+"\n"+posts.toString()
                }
            }
            //無法連線 ex : 404
            override fun onFailure(call: Call<Post>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }
        }
        return cb
    }
    fun getPhotos() : Callback<List<Photo>>{
        val cb = object  : Callback<List<Photo>>{
            override fun onResponse(call: Call<List<Photo>>, response: Response<List<Photo>>) {
                if (!response.isSuccessful) {
                    Log.d("MainActivity","Is not successful : ${response.isSuccessful}")
                    return
                }
                val photos = response.body()
                Log.d("MainActivity",photos.toString())
                Log.d("MainActivity",photos!!.size.toString())
                //UI呈現
                runOnUiThread {
                    recyclerViewAdapter.setListData(photos)
                    recyclerViewAdapter.notifyDataSetChanged()
                }
            }
            override fun onFailure(call: Call<List<Photo>>, t: Throwable) {
                Log.d("MainActivity","Fail : ${t.message}")
            }

        }
        return cb
    }

}