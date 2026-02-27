package com.example.matrix

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// ۱. تعریف خانواده فونت وزیر
val VazirFamily = FontFamily(
    Font(R.font.vazir_regular, FontWeight.Normal),
    Font(R.font.vazir_medium, FontWeight.Medium),
    Font(R.font.vazir_bold, FontWeight.Bold),
    // اگر فایل‌های دیگر مثل Light یا Black را هم داری اینجا اضافه کن
)

// ۲. تنظیم تایپوگرافی پیش‌فرض برای استفاده در پروژه
val AppTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = VazirFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp
    ),
    bodyLarge = TextStyle(
        fontFamily = VazirFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    labelMedium = TextStyle(
        fontFamily = VazirFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp
    )
    // می‌توانی بقیه استایل‌ها مثل bodyMedium, titleLarge و غیره را هم اینجا تعریف کنی
)