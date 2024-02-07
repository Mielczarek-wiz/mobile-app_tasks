package com.example.zadanie1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class logoScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logo_screen)
        val thread = Thread(){
            run{
                Thread.sleep(3000)
            }
            runOnUiThread(){
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        thread.start()
}}