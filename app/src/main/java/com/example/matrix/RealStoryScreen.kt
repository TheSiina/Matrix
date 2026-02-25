package com.example.matrix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

// داستان واقعی در بخش تکنیک های مذاکره
data class RealStory(
    val title: String? = null,
    val story: String? = null,
    val moral: String? = null // اضافه کردن علامت سوال
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealStoryScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current

    // ۱. لود کردن دیتای درس از JSON
    // فرض بر این است که تابع loadLessonDetailFromJson را در پروژه دارید
    val lesson = remember(lessonId) { loadLessonDetailFromJson(context, lessonId) }
    val storyData = lesson?.realStory

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("داستان واقعی", color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        if (storyData == null) {
            // نمایش وضعیت در حال بارگذاری یا خطا
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("داستانی برای این درس یافت نشد.", color = GrayText)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- بخش ۱: هدر تصویری و نام منبع/قهرمان ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(GoldClassic.copy(alpha = 0.15f), Color.Transparent)
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Surface(
                            modifier = Modifier.size(80.dp),
                            shape = CircleShape,
                            color = CardBg,
                            border = BorderStroke(2.dp, GoldClassic)
                        ) {
                            Icon(
                                Icons.Default.HistoryEdu, // تغییر آیکون به حالت روایی
                                contentDescription = null,
                                tint = GoldClassic,
                                modifier = Modifier.padding(15.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        // نمایش نام منبع یا قهرمان از JSON (در اینجا همان تایتل داستان)
                        storyData.title?.let { Text(it, color = SoftWhite, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center) }
                        Text(lesson.source ?: "ماتریکس مذاکره", color = GoldClassic.copy(alpha = 0.7f), fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }

                // --- بخش ۲: محتوای داستان ---
                Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                    Text(
                        text = "روایت واقعی",
                        color = GoldClassic,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(vertical = 16.dp)
                    )

                    Box(
                        modifier = Modifier
                            .width(40.dp)
                            .height(3.dp)
                            .background(GoldClassic, RoundedCornerShape(2.dp))
                    )

                    Spacer(Modifier.height(24.dp))

                    // متن اصلی داستان از JSON
                    storyData.story?.let {
                        Text(
                            text = it,
                            color = SoftWhite.copy(alpha = 0.9f),
                            fontSize = 17.sp,
                            lineHeight = 34.sp,
                            textAlign = TextAlign.Justify
                        )
                    }

                    Spacer(Modifier.height(40.dp))

                    // بخش نتیجه‌گیری ماتریکسی (Moral) از JSON
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "نکته ماتریکسی: ${storyData.moral}",
                            color = GrayText,
                            fontSize = 14.sp,
                            fontStyle = FontStyle.Italic,
                            modifier = Modifier.padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }

                    Spacer(Modifier.height(30.dp))
                }
            }
        }
    }
}