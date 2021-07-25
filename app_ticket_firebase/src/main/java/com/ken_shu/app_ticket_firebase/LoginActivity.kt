package com.ken_shu.app_ticket_firebase

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ken_shu.app_ticket_firebase.models.User
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private val database = Firebase.database
    private val myUserRef = database.getReference("ticketsUser")
    lateinit var context: Context
    private var users =  mutableListOf<User>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        myUserRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                users.clear()
                snapshot.children.forEach {
                    val user = User("", 0)
                    it.children.forEach {
                        when(it.key.toString()) {
                            "name" -> user.name = it.value.toString()
                            "priority" -> user.priority = it.value.toString().toInt()
                        }
                    }
                    users.add(user)
                }
                //Toast.makeText(context, users.toString(), Toast.LENGTH_SHORT).show()
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })
    }

    fun orderLogin(view: View) {
        val userName = et_username.text.toString()
        val userOpt = users.stream().filter { it.name.equals(userName) }.findFirst()
        if(userOpt.isPresent) {
            val user = userOpt.get()
            if (user.priority >= 1) {
                val intent = Intent(context, MainActivity::class.java)
                intent.putExtra("userName", userName)
                startActivity(intent)
            } else {
                Toast.makeText(context, "無此權限", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "無此帳號", Toast.LENGTH_SHORT).show()
        }
    }

    // Homework
    fun consoleLogin(view: View) {
        val userName = et_username.text.toString()
        val intent = Intent(context, ConsoleActivity::class.java)
        intent.putExtra("userName", userName)
        startActivity(intent)
    }
}