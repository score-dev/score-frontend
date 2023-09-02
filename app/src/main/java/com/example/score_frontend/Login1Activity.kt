package com.example.score_frontend

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.score_frontend.databinding.ActivityLogin1Binding

class Login1Activity : AppCompatActivity() {
    private lateinit var binding: ActivityLogin1Binding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogin1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnStartKakao.setOnClickListener{
            val intent= Intent( this,KakaoLoginActivity::class.java)
            startActivity(intent)
        }
    }
}