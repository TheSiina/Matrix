package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrix.databinding.ActivityAdviceBinding

class AdviceActivity : AppCompatActivity() {
    lateinit var binding: ActivityAdviceBinding

    private val dataAdvice = listOf(
        DataModel("توصیه اول..."),
        DataModel("توصیه دوم..."),
        DataModel("توصیه سوم..."),
        DataModel("توصیه چهارم..."),
        DataModel("توصیه پنجم..."),
        DataModel("توصیه ششم..."),
        DataModel("توصیه هفتم..."),
        DataModel("توصیه هشتم..."),
        DataModel("توصیه نهم..."),
        DataModel("توصیه دهم..."),
        DataModel("توصیه یازدهم..."),
        DataModel("توصیه دوازدهم..."),
        DataModel("توصیه سیزدهم..."),
        DataModel("توصیه چهاردهم..."),
        DataModel("توصیه پانزدهم..."),
        DataModel("توصیه شانزدهم..."),
        DataModel("توصیه هفدهم..."),
        DataModel("توصیه هجدهم..."),
        DataModel("توصیه نوزدهم..."),
        DataModel("توصیه بیستم..."),
        DataModel("توصیه بیست و یکم..."),
        DataModel("توصیه بیست و دوم..."),
        DataModel("توصیه بیست و سوم..."),
        DataModel("توصیه بیست و چهارم..."),
        DataModel("توصیه بیست و پنجم..."),
        DataModel("توصیه بیست وششم..."),
        DataModel("توصیه بیست و هفتم..."),
        DataModel("توصیه بیست و هشتم..."),
        DataModel("توصیه بیست و نهم..."),
        DataModel("توصیه سی..."),
        DataModel("توصیه سی و یکم..."),
        DataModel("توصیه سی و دوم..."),
        DataModel("توصیه سی و سوم..."),
        DataModel("توصیه سی و چهارم..."),
        DataModel("توصیه سی و پنجم..."),
        DataModel("توصیه سی و ششم..."),
        DataModel("توصیه سی و هفتم..."),
        DataModel("توصیه سی و هشتم..."),
        DataModel("توصیه سی و نهم..."),
        DataModel("توصیه چهلم..."),
        DataModel("توصیه چهل و یکم..."),
        DataModel("توصیه چهل و دوم..."),
        DataModel("توصیه چهل و سوم..."),
        DataModel("توصیه چهل و چهارم..."),
        DataModel("توصیه چهل و پنجم..."),
        DataModel("توصیه چهل و ششم..."),
        DataModel("توصیه چهل و هفتم..."),
        DataModel("توصیه چهل و هشتم..."),
        DataModel("توصیه چهل و نهم..."),
        DataModel("توصیه پنجاهم..."),
        DataModel("منبع")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdviceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewAdvice.layoutManager = LinearLayoutManager(this)
        val adapterAdvice = RecyclerAdviceAdapter(dataAdvice,this)
        binding.recyclerViewAdvice.adapter = adapterAdvice
    }
}