package com.theecoder.anonymus

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.theecoder.anonymus.Session.LoginPref

class Signup_Activity : AppCompatActivity() {

    private lateinit var  etUsername:EditText
    private lateinit var btnLogin : Button

    lateinit var session: LoginPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        session = LoginPref(this)
        if(session.isLoggedin()){
            var i: Intent = Intent(applicationContext, MainActivity::class.java)
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(i)
            finish()
        }

        etUsername = findViewById(R.id.editTextTextMultiLine)
        btnLogin = findViewById(R.id.signup)

        btnLogin.setOnClickListener{
            var username = etUsername.text.toString().trim()
            if(username.isEmpty()){
                Toast.makeText(this,"Enter a username that you will use",Toast.LENGTH_SHORT).show()
            }else{
                session.createLoginSession(username)
                var i: Intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(i)
                finish()
            }
        }
    }
}