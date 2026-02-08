package com.example.matrix

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.matrix.databinding.ActivityJobsBinding

class JobsActivity : AppCompatActivity() {
    lateinit var binding: ActivityJobsBinding

    private val dataJobs = listOf(
        DataModel("سرمایه گذاری در بازار ارزهای دیجیتال"),
        DataModel("ترید و سرمایه گذاری در بازار بورس"),
        DataModel(" هودل کردن"),
        DataModel("ترید کردن"),
        DataModel("استیکینگ"),
        DataModel("بازی های بلاک چینی"),
        DataModel("طراحی نرم افزار تلفن همراه"),
        DataModel("توسعه دهنده وب سایت"),
        DataModel("یوتیوبر شدن"),
        DataModel("بازاریابی وابسته یا همکاری در فروش"),
        DataModel("بلاگر"),
        DataModel("حسابدار"),
        DataModel("بسته بندی حبوبات"),
        DataModel("دستگاه جوراب بافی"),
        DataModel("پرورش قارچ"),
        DataModel("تاسیس نمایندگی بیمه"),
        DataModel("طراح داخلی"),
        DataModel("جوشکار سیار"),
        DataModel("سبزی خردکن"),
        DataModel("آشپز غذای خانگی و دسر"),
        DataModel("طراح لباس"),
        DataModel("خیاط و تعمیرکار لباس"),
        DataModel("سازنده جعبه ی کادو"),
        DataModel("ساخت زیورآلات دست ساز"),
        DataModel("درست کردن غذاهای گیاهی"),
        DataModel("مشاور آنلاین در زمینه های تخصصی"),
        DataModel("بازاریابی تلفنی"),
        DataModel("تولید محتوای متنی"),
        DataModel("ساخت کتاب های pdf"),
        DataModel("ساخت ویدئوهای آموزشی"),
        DataModel("پرورش و فروش گل و گیاه"),
        DataModel("ارائه خدمات گرافیکی و چاپ"),
        DataModel("آموزش آنلاین"),
        DataModel("قالی بافی"),
        DataModel("ترجمه متون انگلیسی به فارسی"),
        DataModel("دوبله"),
        DataModel("عکس بردار/تصویر بردار"),
        DataModel("سازنده انیمیشن"),
        DataModel("طراحی UI و UX سایت و اپلیکیشن"),
        DataModel("آهنگسازی"),
        DataModel(" ادیتور فیلم و ویدیو"),
        DataModel("کسب درآمد از تایپ"),
        DataModel("تدریس زبان"),
        DataModel("پشتیبانی سایت"),
        DataModel("نویسندگی"),
        DataModel("فروش هنرهای دستی"),
        DataModel("پرستاری از کودکان"),
        DataModel("هوش مصنوعی"),
        DataModel("فروش مجدد"),
        DataModel("منبع")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJobsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerViewJobs.layoutManager = LinearLayoutManager(this)
        val adapterJobs = RecyclerJobsAdapter(dataJobs,this)
        binding.recyclerViewJobs.adapter = adapterJobs
    }
}