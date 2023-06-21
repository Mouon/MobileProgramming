package com.example.foodjoa.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.foodjoa.databinding.ActivitySplashBinding

class SplashActivity:AppCompatActivity() {
    lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadSlash()
    }

    fun loadSlash(){
        var handler=Handler(Looper.getMainLooper())
        handler.postDelayed({
            val intent=Intent(this, StartActivity::class.java)
            startActivity(intent)
            finish()
        },3000)
    }
}