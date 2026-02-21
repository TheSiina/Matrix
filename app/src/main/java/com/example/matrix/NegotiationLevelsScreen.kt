package com.example.matrix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NegotiationLevelsScreen(navController: NavController) {
    val levels = listOf("سطح مقدماتی ۱", "سطح مقدماتی ۲", "سطح متوسط ۱", "سطح متوسط ۲", "سطح پیشرفته")
    var isLevelMenuVisible by remember { mutableStateOf(false) }
    var selectedLevel by remember { mutableStateOf(levels[0]) }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(20.dp))
                            .clickable { isLevelMenuVisible = !isLevelMenuVisible }
                            .padding(horizontal = 12.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(selectedLevel, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SoftWhite)
                        Icon(
                            if (isLevelMenuVisible) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = null,
                            tint = GoldClassic
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                actions = {
                    IconButton(onClick = { /* امتیازات کاربر */ }) {
                        Icon(Icons.Default.Stars, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // ۱. هدر تصویری لوکس
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.header_negotiation),
                            contentDescription = "Header Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )

                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Transparent,
                                            DeepNavy.copy(alpha = 0.8f),
                                            DeepNavy
                                        )
                                    )
                                )
                        )

                        Column(
                            modifier = Modifier
                                .align(Alignment.BottomCenter)
                                .padding(bottom = 16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                "آکادمی استراتژی مذاکره",
                                color = GoldClassic,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "NEGOTIATION MASTERCLASS",
                                color = SoftWhite.copy(alpha = 0.6f),
                                fontSize = 10.sp,
                                letterSpacing = 3.sp
                            )
                        }
                    }
                }

                // ۲. لیست دروس و آیتم‌های آزمون
                val items = getNegotiationData()
                itemsIndexed(items) { index, item ->
                    when (item) {
                        is NegotiationItem.Lesson -> LessonRow(item)
                        is NegotiationItem.MidReview -> MidReviewRow(item)
                    }
                    if (index < items.lastIndex) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 85.dp),
                            thickness = 0.8.dp, // کمی ضخیم‌تر
                            color = GoldClassic.copy(alpha = 0.25f) // پررنگ‌تر شد
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(50.dp)) }
            }

            // ۳. منوی انتخاب سطح (Overlay)
            if (isLevelMenuVisible) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(DeepNavy.copy(alpha = 0.95f))
                        .clickable { isLevelMenuVisible = false }
                ) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        levels.forEach { level ->
                            Text(
                                text = level,
                                color = if (selectedLevel == level) GoldClassic else SoftWhite,
                                fontSize = 18.sp, // فونت کوچک‌تر شد
                                fontWeight = if (selectedLevel == level) FontWeight.ExtraBold else FontWeight.Normal,
                                modifier = Modifier
                                    .padding(vertical = 10.dp) // فاصله کمتر شد
                                    .clickable {
                                        selectedLevel = level
                                        isLevelMenuVisible = false
                                    }
                            )
                        }

                        Spacer(modifier = Modifier.height(30.dp))

                        IconButton(
                            onClick = { isLevelMenuVisible = false },
                            modifier = Modifier
                                .size(50.dp)
                                .background(GoldClassic, CircleShape)
                        ) {
                            Icon(Icons.Default.Close, null, tint = DeepNavy)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun LessonRow(lesson: NegotiationItem.Lesson) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { /* انتقال به صفحه محتوای درس */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(CardBg, shape = RoundedCornerShape(12.dp))
                .border(1.dp, GoldClassic.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("${lesson.id}", color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("گام ${lesson.id}", fontSize = 10.sp, color = GoldClassic, letterSpacing = 1.sp)
            Text(lesson.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SoftWhite)

            // نمایش کلمه رایگان زیر تایتل (مشابه لرنیت)
            if (lesson.isFree) {
                Text("رایگان", color = GoldClassic, fontSize = 11.sp, fontWeight = FontWeight.Light)
            }
        }

        // آیکون دانلود برای همه (جایگزین قفل شد)
        Icon(
            imageVector = Icons.Default.FileDownload,
            contentDescription = "Download",
            tint = if (lesson.isFree) GoldClassic else GrayText.copy(alpha = 0.6f),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun MidReviewRow(review: NegotiationItem.MidReview) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(CardBg.copy(alpha = 0.5f))
            .clickable { /* شروع آزمون */ }
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(54.dp)) {
                CircularProgressIndicator(
                    progress = { review.progress / 100f },
                    color = GoldClassic,
                    trackColor = DeepNavy,
                    strokeWidth = 3.dp
                )
                Text("${review.progress}%", color = GoldClassic, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.width(20.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text("ارزیابی استراتژیک", fontSize = 10.sp, color = GoldClassic)
                Text(review.title, fontSize = 15.sp, fontWeight = FontWeight.Bold, color = SoftWhite)
            }

            Icon(Icons.Default.FileDownload, null, tint = GrayText.copy(alpha = 0.6f), modifier = Modifier.size(24.dp))
        }
    }
}

fun getNegotiationData() = listOf(
    NegotiationItem.Lesson(1, "استراتژی لنگر انداختن", GoldClassic, true),
    NegotiationItem.Lesson(2, "سکوت تاکتیکی", GoldClassic, true),
    NegotiationItem.Lesson(3, "محدودیت زمانی", GoldClassic),
    NegotiationItem.Lesson(4, "روانشناسی تضاد", GoldClassic),
    NegotiationItem.Lesson(5, "بستن قرارداد", GoldClassic),
    NegotiationItem.MidReview(1, "آزمون ارزیابی سطح بقا", 0),
    NegotiationItem.Lesson(6, "زبان بدن قدرتمند", GoldClassic)
)

sealed class NegotiationItem {
    data class Lesson(val id: Int, val title: String, val color: Color, val isFree: Boolean = false) : NegotiationItem()
    data class MidReview(val id: Int, val title: String, val progress: Int = 0) : NegotiationItem()
}