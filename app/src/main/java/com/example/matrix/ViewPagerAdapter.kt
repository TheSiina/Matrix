package com.example.matrix

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter

class ViewPagerAdapter(val context: Context): PagerAdapter() {

    var layoutInflater: LayoutInflater? = null

    //array of image, head, descriptions
    val imgArray = arrayOf(
        R.drawable.payment,
        R.drawable.advice,
        R.drawable.jobs,
        R.drawable.matrix
    )

    val headArray = arrayOf(
        "ورود به برنامه",
        "توصیه و نصیحت های مهم از افراد موفق جهان",
        "معرفی و آموزش مشاغل پردرآمد",
        "از سیستم ماتریکس خارج شو!"
    )

    val arrayDescription = arrayOf(
        "کاربر عزیز هم اکنون می توانید با فشردن کلید (ورود)، به برنامه وارد شوید. و همچنین می توانید تنها با پرداخت مبلغ شصت و نه هزار تومان نسخه کامل آن را خریداری نموده و از خدمات متنوع آن بهره مند شوید.",
        "معرفی و آموزش قوانین قدرت،قوانین موفقیت در کار و تجارت، نکات مهم کسب و کار برای تمامی سنین و قوانین هرگونه رابطه همراه با فیلم های آموزشی",
        "بهترین شغل هارا با بیشترین درآمد و کمترین هزینه(سرمایه) به شما معرفی خواهیم کرد و همچنین با آموزش آن ها به شما کمک خواهیم کرد تا در سریع ترین زمان به درآمد مناسبی برسید.",
        "با ما خیلی ساده در هر سنی کنترل ذهنتان را به دست بگیرید،موفق و ثروتمند شوید و همچنین به لحاظ فیزیکی قوی تر شوید."
    )
    override fun getCount(): Int {
        return headArray.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object` as RelativeLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = layoutInflater!!.inflate(R.layout.slider,container,false)

        val img = view.findViewById<ImageView>(R.id.image)
        val txt_head = view.findViewById<TextView>(R.id.txt_head)
        val txt_description = view.findViewById<TextView>(R.id.txt_description)

        img.setImageResource(imgArray[position])
        txt_head.text = headArray[position]
        txt_description.text = arrayDescription[position]

        container.addView(view)

        return view
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as RelativeLayout)
    }
}