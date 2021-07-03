package com.ken_shu.app_tictactoe_firebase

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val database = Firebase.database
    val myTTTRef = database.getReference("ttt/game")
    lateinit var context: Context
    //var buttons = mutableListOf<Button>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        context = this


        bt_mark.setOnClickListener {
           var mark = bt_mark.tag.toString()
            if (mark.equals("O")){
                bt_mark.text = "X"
                bt_mark.tag = "X"
            }else{
                bt_mark.text = "O"
                bt_mark.tag = "O"
            }
        }
        //觀察者
        myTTTRef.addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
               val game = snapshot.children
                //Toast.makeText(context , game.toString(),Toast.LENGTH_SHORT).show()
                game.forEach {
                    Log.d("MainActivity",it.key+" : " + it.value)
                    //透過 it.key 值 的 id
                    val id = resources.getIdentifier(it.key,"id" , context.packageName)
                    //再透過 id 值去尋找 再放進去 it.value 裡面
                    findViewById<Button>(id).text = it.value.toString()
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })
    }
    fun tttonClick(view :View){
        val tag = view.getTag().toString()
        //Toast.makeText(this,tag.toString(),Toast.LENGTH_SHORT).show()
        val path = "b" + tag
        var mark = bt_mark.tag.toString()
        myTTTRef.child(path).setValue(mark)

    }
}