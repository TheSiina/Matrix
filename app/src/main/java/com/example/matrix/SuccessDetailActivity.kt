package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matrix.databinding.ActivitySuccessDetailBinding

class SuccessDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivitySuccessDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySuccessDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        binding.txtSuccessDetail.text = text
    }
}