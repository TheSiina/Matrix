package com.example.matrix

// صفحه تکنیک‌های فروش: ساختار آینه‌ایِ NegotiationLevelsScreen

import android.content.Context
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
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.JsonParser

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SalesLevelsScreen(navController: NavController) {
    val context = LocalContext.current
    val prefs = remember { context.getSharedPreferences("sales_user_settings", Context.MODE_PRIVATE) }

    // همان ۵ سطح، برای فروش
    val levels = listOf("سطح مقدماتی 1", "سطح مقدماتی 2", "سطح متوسط 1", "سطح متوسط 2", "سطح پیشرفته")
    var isLevelMenuVisible by remember { mutableStateOf(false) }
    var selectedLevel by remember {
        mutableStateOf(prefs.getString("sales_last_selected_level", "سطح مقدماتی 1") ?: "سطح مقدماتی 1")
    }

    // هر بار سطح عوض شود، درس‌ها از فایل فروش لود می‌شود
    val items = remember(selectedLevel, navController.currentBackStackEntry) {
        prefs.edit().putString("sales_last_selected_level", selectedLevel).apply()
        loadSalesLessonsByLevel(context, selectedLevel)
    }

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
                    IconButton(onClick = { /* امتیازات کاربر برای فروش */ }) {
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
                // هدر تصویری (می‌توانی در آینده تصویر جدا برای فروش بگذاری)
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.header_negotiation),
                            contentDescription = "Sales Header Image",
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
                                "آکادمی استراتژی فروش",
                                color = GoldClassic,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 2.sp
                            )
                            Text(
                                "SALES MASTERCLASS",
                                color = SoftWhite.copy(alpha = 0.6f),
                                fontSize = 10.sp,
                                letterSpacing = 3.sp
                            )
                        }
                    }
                }

                itemsIndexed(items) { index, item ->
                    when (item) {
                        is SalesItem.Lesson -> {
                            SalesLessonRow(lesson = item, navController = navController)
                        }
                        is SalesItem.MidReview -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        navController.navigate("sales_exam_screen/$selectedLevel")
                                    }
                            ) {
                                SalesMidReviewRow(item)
                            }
                        }
                    }

                    if (index < items.lastIndex && item is SalesItem.Lesson) {
                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 85.dp),
                            thickness = 0.8.dp,
                            color = GoldClassic.copy(alpha = 0.25f)
                        )
                    }
                }

                item { Spacer(modifier = Modifier.height(50.dp)) }
            }

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
                                fontSize = 18.sp,
                                fontWeight = if (selectedLevel == level) FontWeight.ExtraBold else FontWeight.Normal,
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
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
fun SalesLessonRow(lesson: SalesItem.Lesson, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                navController.navigate("sales_lesson_content/${lesson.id}/${lesson.title}")
            }
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
            Text(lesson.id, color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(modifier = Modifier.width(20.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text("گام ${lesson.id}", fontSize = 10.sp, color = GoldClassic, letterSpacing = 1.sp)
            Text(lesson.title, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = SoftWhite)

            if (lesson.isFree) {
                Text("رایگان", color = GoldClassic, fontSize = 11.sp, fontWeight = FontWeight.Light)
            }
        }

        Icon(
            imageVector = Icons.Default.FileDownload,
            contentDescription = "Download",
            tint = if (lesson.isFree) GoldClassic else GrayText.copy(alpha = 0.6f),
            modifier = Modifier.size(22.dp)
        )
    }
}

@Composable
fun SalesMidReviewRow(review: SalesItem.MidReview) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .heightIn(min = 120.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(CardBg, DeepNavy)
                ),
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(listOf(GoldClassic, Color.Transparent)),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.size(70.dp)) {
                androidx.compose.material3.CircularProgressIndicator(
                    progress = { review.progress / 100f },
                    color = GoldClassic,
                    trackColor = DeepNavy.copy(alpha = 0.5f),
                    strokeWidth = 5.dp,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    "${review.progress}%",
                    color = GoldClassic,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    "ارزیابی نهایی سطح فروش",
                    fontSize = 12.sp,
                    color = GoldClassic,
                    letterSpacing = 1.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    review.title,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = SoftWhite
                )
                Text(
                    "تسلط بر ۲۰ تکنیک فروش",
                    fontSize = 11.sp,
                    color = GrayText,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = GoldClassic,
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

fun loadSalesLessonsByLevel(context: Context, levelName: String): List<SalesItem> {
    return try {
        val jsonString = context.assets.open("sales_techniques.json").bufferedReader().use { it.readText() }
        val rootObject = JsonParser.parseString(jsonString).asJsonObject
        val lessonsArray = rootObject.getAsJsonArray("sales_items")
        val items = mutableListOf<SalesItem>()

        val targetLevel = levelName.replace("سطح ", "").trim()
        val filteredLessons = lessonsArray.filter {
            it.asJsonObject.get("level").asString.trim() == targetLevel
        }

        filteredLessons.forEach { element ->
            val obj = element.asJsonObject
            items.add(
                SalesItem.Lesson(
                    id = obj.get("lesson_id").asString,
                    title = obj.get("lesson_title").asString,
                    isFree = obj.get("lesson_id").asString.toIntOrNull() ?: 0 <= 2
                )
            )
        }

        val prefs = context.getSharedPreferences("sales_user_data", Context.MODE_PRIVATE)
        val dynamicKey = "sales_score_${targetLevel.replace(" ", "_")}"
        val savedScore = prefs.getInt(dynamicKey, 0)

        if (items.isNotEmpty()) {
            items.add(
                SalesItem.MidReview(
                    id = 100,
                    title = "آزمون جامع فروش",
                    progress = savedScore
                )
            )
        }
        items
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

sealed class SalesItem {
    data class Lesson(
        val id: String,
        val title: String,
        val isFree: Boolean = false
    ) : SalesItem()

    data class MidReview(val id: Int, val title: String, val progress: Int = 0) : SalesItem()
}

