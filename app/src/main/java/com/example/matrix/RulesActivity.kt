package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrix.databinding.ActivityRulesBinding

class RulesActivity : AppCompatActivity() {
    lateinit var binding: ActivityRulesBinding

    private val data = listOf(
        DataModel("قانون یک"),
        DataModel("قانون دو"),
        DataModel("قانون سه"),
        DataModel("قانون چهار"),
        DataModel("قانون پنج"),
        DataModel("قانون شش"),
        DataModel("قانون هفت"),
        DataModel("قانون هشت"),
        DataModel("قانون نه"),
        DataModel("قانون ده"),
        DataModel("قانون یازده"),
        DataModel("قانون دوازده"),
        DataModel("قانون سیزده"),
        DataModel("قانون چهارده"),
        DataModel("قانون پانزده"),
        DataModel("قانون شانزده"),
        DataModel("قانون هفده"),
        DataModel("قانون هجده"),
        DataModel("قانون نوزده"),
        DataModel("قانون بیست"),
        DataModel("قانون بیست و یک"),
        DataModel("قانون بیست و دو"),
        DataModel("قانون بیست و سه"),
        DataModel("قانون بیست و چهار"),
        DataModel("قانون بیست و پنج"),
        DataModel("قانون بیست و شش"),
        DataModel("قانون بیست و هفت"),
        DataModel("قانون بیست و هشت"),
        DataModel("قانون بیست و نه"),
        DataModel("قانون سی"),
        DataModel("قانون سی و یک"),
        DataModel("قانون سی و دو"),
        DataModel("قانون سی و سه"),
        DataModel("قانون سی و چهار"),
        DataModel("قانون سی و پنج"),
        DataModel("قانون سی و شش"),
        DataModel("قانون سی و هفت"),
        DataModel("قانون سی و هشت"),
        DataModel("قانون سی و نه"),
        DataModel("قانون چهل"),
        DataModel("قانون چهل و یک"),
        DataModel("قانون چهل و دو"),
        DataModel("قانون چهل و سه"),
        DataModel("قانون چهل و چهار"),
        DataModel("قانون چهل و پنج"),
        DataModel("قانون چهل و شش"),
        DataModel("قانون چهل و هفت"),
        DataModel("قانون چهل و هشت"),
        DataModel("منبع")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRulesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        val adapterTest = RecyclerAdapter(data,this)
        binding.recyclerView.adapter = adapterTest
    }
}