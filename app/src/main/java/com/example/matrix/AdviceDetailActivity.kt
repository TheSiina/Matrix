package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matrix.databinding.ActivityAdviceDetailBinding

class AdviceDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdviceDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        binding.txtAdviceDetail.text = text
    }
}