package com.example.matrix

import android.content.Context
import android.content.SharedPreferences

//یه کلاس تعریف کردیم که یه کانتکست میگیره
class SessionManager(var context: Context?) {
    // اومدیم یه نمونه از شیرد پرفرنس ساختیم
    private var sharedPreferences: SharedPreferences
    //یه کلید براش درنظر گرفتیم
    private var preferences = "pref"

        // اینجا هم اومدیم شیرد پرفرنس رو درواقع نمونه ازش ساختیم و آماده به کار هستش
    init {
        sharedPreferences = context!!.getSharedPreferences(preferences, Context.MODE_PRIVATE)
    }

    // اینجا هم یه تابع نوشتیم که میاد مقدار رو میگیره و داخل شیرد ذخیره میکنه
   // ببین اینجا یه کلید هم میگیره، اون کلید رو هم میگم چیشد
    // value هم که مقداریه که باید بهش بدیم تا ذخیره کنه
    fun saveStatePy(key:String, value:Boolean){
        return sharedPreferences.edit().putBoolean(key, value).apply()
    }

    // اینجا هم اومدیم مقداری که ذخیره شده رو گرفتیم ازش که یه بولین برمیگردونه
    // اون کلیدی که بهش دادیم اینجا نیازه، با اون کلید باید بیایم و مقدار رو بخونیم
    // مقدار پیشفرضش هم فالس هستش
    fun getStatePay(key: String) : Boolean {
        return sharedPreferences.getBoolean(key, false)
    }
}