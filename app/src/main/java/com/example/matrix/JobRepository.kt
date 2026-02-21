package com.example.matrix

// JobRepository.kt
import android.content.Context
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobRepository(private val context: Context) {

    // تابع suspend یعنی این تابع میتونه متوقف بشه و در پس‌زمینه اجرا بشه
    suspend fun getAllJobs(): List<Job> = withContext(Dispatchers.IO) {
        try {
            // خواندن فایل از پوشه assets
            val jsonString = context.assets.open("jobs.json").bufferedReader().use { it.readText() }

            // تبدیل متن JSON به لیست اشیاء Job
            val listType = object : TypeToken<List<Job>>() {}.type
            Gson().fromJson(jsonString, listType)
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList() // اگه خطایی داد لیست خالی برگردون
        }
    }
}