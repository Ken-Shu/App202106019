package com.ken_shu.app_login_firebase

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var context: Context
    private lateinit var setting: SharedPreferences

    companion object {
        private const val DATA = "DATA"
        private const val EMAIL = "EMAIL"
        private const val PASSWORD = "PASSWORD"
        private const val MEMORY = "MEMORY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        context = this
        auth = Firebase.auth
        setting = getSharedPreferences(DATA, MODE_PRIVATE)

        et_email.setText(setting.getString(EMAIL, ""))
        et_password.setText(setting.getString(PASSWORD, ""))
        cb_memory.isChecked = setting.getBoolean(MEMORY, false)
    }

    fun forgot(view: View) {
        val intent = Intent(context, ForgotActivity::class.java)
        startActivity(intent)
    }

    fun signUp(view: View) {
        val intent = Intent(context, SignUpActivity::class.java)
        startActivity(intent)
    }


    fun signIn(view: View) {
        val email = et_email.text.toString()
        val password = et_password.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, OnCompleteListener<AuthResult?> { task ->
                //task.result
                if (task.isSuccessful) {
                    //是否此人的 email 有驗證?
                    if (task.result?.user?.isEmailVerified == true) {
                        //是否 memory ?
                        if (cb_memory.isChecked) {
                            setting.edit()
                                .putString(EMAIL, et_email.text.toString())
                                .putString(PASSWORD, et_password.text.toString())
                                .putBoolean(MEMORY, true)
                                .apply()
                        } else {
                            setting.edit()
                                .putString(EMAIL, "")
                                .putString(PASSWORD, "")
                                .putBoolean(MEMORY, false)
                                .apply()
                        }
                        val intent = Intent(context, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        val intent = Intent(context, ResultActivity::class.java)
                        intent.putExtra("message", "Email尚未驗證")
                        intent.putExtra("action","emailVerify")
                        startActivity(intent)
                    }
                } else {
                    Toast.makeText(context, "Login fail !", Toast.LENGTH_SHORT).show()

                }
            })
    }
}