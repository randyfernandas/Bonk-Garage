package com.example.bonkgarage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         val handler = Handler()

            val runnable = Runnable {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }

            handler.postDelayed(runnable, 2000)
    }
}