package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.matrix.databinding.ActivityRulesDetailBinding

class RulesDetailActivity : AppCompatActivity() {
    lateinit var binding: ActivityRulesDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val text = intent.getStringExtra("text")
        binding.txtRulesDetail.text = text
    }
}