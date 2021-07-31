package com.ken_shu.app_login_firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_result.*

class ResultActivity : AppCompatActivity() {
    private  lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_result)
        val message = intent.getStringExtra("message")
        val action = intent.getStringExtra("action")
        auth = Firebase.auth
        if (message != null){
            tv_result.setText(message)
        }
        if(action != null && !action.equals("") && action.equals("emailVerify")){
            bt_action.visibility = View.VISIBLE
        }else {
            bt_action.visibility = View.GONE
        }
    }

    fun back(view: View){
        finish()
    }
    fun actionClick(view :View){
        auth.currentUser?.sendEmailVerification()
        val intent = Intent(this , ResultActivity::class.java)
        startActivity(intent)
        finish()
    }
}