package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matrix.databinding.ActivityJobsDetailBinding

class JobsDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityJobsDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        binding.txtJobsDetail.text = text
    }
}