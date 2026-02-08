package com.example.matrix


import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import com.example.matrix.databinding.ActivityMainBinding
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator

class MainActivity : AppCompatActivity() {

    lateinit var onBoardingScreen: SharedPreferences
    lateinit var binding: ActivityMainBinding


    private lateinit var viewPager: ViewPager
    private lateinit var viewPagerAdapter: ViewPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBoardingScreen = getSharedPreferences("onBoardingScreen", MODE_PRIVATE)
        val edit = onBoardingScreen.edit()

        if (onBoardingScreen.getBoolean("firstTime", true) == false) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnNext.visibility = View.VISIBLE

        binding.btnVorod.setOnClickListener {

                edit.putBoolean("firstTime", false)
                edit.apply()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()

        }

        binding.btnNext.setOnClickListener {
            viewPager.setCurrentItem(getItem(-1), true)
        }

        val relativeLayout = findViewById<RelativeLayout>(R.id.layout1)
        val dotsIndicator = findViewById<DotsIndicator>(R.id.dots_indicator)

        viewPager = findViewById(R.id.viewpager)
        viewPagerAdapter = ViewPagerAdapter(this)
        viewPager.adapter = viewPagerAdapter
        viewPager.currentItem = taskId

        dotsIndicator.attachTo(viewPager)

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {

            }

            override fun onPageSelected(position: Int) {
                if (position == 0) {
                    binding.btnVorod.visibility = View.VISIBLE
                    binding.btnNext.visibility = View.INVISIBLE
                }
                if (position == 1) {
                    binding.btnVorod.visibility = View.INVISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                }
                if (position == 2) {
                    binding.btnVorod.visibility = View.INVISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                }
                if (position == 3) {
                    binding.btnVorod.visibility = View.INVISIBLE
                    binding.btnNext.visibility = View.VISIBLE
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        }
        )
    }

    private fun getItem(i: Int): Int {
        return viewPager.currentItem + i
    }
}

//if (kharid == true) {
//                edit.putBoolean("firstTime", false)
//                edit.apply()
//                val intent = Intent(this, HomeActivity::class.java)
//                startActivity(intent)
//                finish()
//            } else {
//                Toast.makeText(
//                    this,
//                    "لطفا ابتدا اشتراک برنامه را خریداری نمایید",
//                    Toast.LENGTH_LONG
//                ).show()
//            }