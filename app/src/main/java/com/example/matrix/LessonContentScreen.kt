package com.example.matrix

import android.content.Context
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.annotations.SerializedName

data class LessonDetail(
    @SerializedName("lesson_id") val lessonId: String,
    @SerializedName("lesson_title") val title: String,
    val source: String? = null,
    val content: String,
    val quote: String? = null,
    @SerializedName("cheat_code") val cheatCode: String? = null,
    @SerializedName("real_story") val realStory: RealStory? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LessonContentScreen(
    lessonId: String,
    lessonTitle: String,
    navController: NavController
) {
    val sections = listOf(
        Triple("آموزش تکنیک", "متن و صوت آموزشی", Icons.Default.AutoStories), // آیکون آموزشی جدید
        Triple("داستان واقعی", "تجربه‌های فاتحان ماتریکس", Icons.Default.MenuBook),
        Triple("تمرین سناریو", "تست هوش مذاکره", Icons.Default.Psychology),
        Triple("برنامه عملی", "تمرین در دنیای واقعی", Icons.Default.AdsClick)
    )

    Box(modifier = Modifier.fillMaxSize()) {

        // --- بخش ۱: تصویر پس‌زمینه سرتاسری ---
        // وقتی عکس را به پروژه اضافه کردی، نام آن را اینجا جایگزین کن (مثلاً R.drawable.matrix_bg)

        // لایه مشکی ملایم برای خوانایی بهتر کارت‌ها روی عکس
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Scaffold(
            containerColor = Color.Transparent, // حتماً شفاف باشد تا عکس دیده شود
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // --- بخش ۲: هدر (فلش در کنج، متون در مرکز) ---
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                ) {
                    // فلش بازگشت کوچک و شیک در سمت راست (RTL)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldClassic,
                        modifier = Modifier
                            .size(22.dp) // کوچک‌تر شدن سایز
                            .align(Alignment.TopStart) // چسبیدن به کنج بالا سمت راست
                            .clickable { navController.popBackStack() }
                    )

                    // متون هدر در مرکز و کمی پایین‌تر
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp), // فاصله از سقف برای لوکس شدن
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "MATRIX ACADEMY",
                            color = GoldClassic.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 5.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = lessonTitle,
                            color = SoftWhite,
                            fontSize = 22.sp, // کمی درشت‌تر برای ابهت بیشتر
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                // --- بخش ۳: کارت‌ها (با فاصله بیشتر از بالا برای هل دادن به پایین) ---
                Spacer(modifier = Modifier.weight(0.2f)) // این بخش باعث می‌شود لیست به سمت پایین هل داده شود

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(sections) { section ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(105.dp)
                                // داخل فایل LessonContentScreen بخش clickable کارت اول را اینطوری بنویس:
                                .clickable {
                                    when (section.first) {
                                        "آموزش تکنیک" -> navController.navigate("lesson_detail_view/$lessonId")
                                        "داستان واقعی" -> navController.navigate("real_story_view/$lessonId")
                                        "تمرین سناریو" -> navController.navigate("scenario_view/$lessonId")
                                        "برنامه عملی" -> navController.navigate("action_plan_view/$lessonId")
                                    }
                                },

                            color = CardBg.copy(alpha = 0.75f), // شیشه‌ای تیره
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(0.5.dp, GoldClassic.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // آیکون در سمت راست (برای اپ فارسی)
                                Box(
                                    modifier = Modifier
                                        .size(48.dp)
                                        .background(DeepNavy.copy(alpha = 0.9f), CircleShape)
                                        .border(1.dp, GoldClassic.copy(alpha = 0.3f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(section.third, null, tint = GoldClassic, modifier = Modifier.size(24.dp))
                                }

                                Spacer(Modifier.width(16.dp))

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(section.first, color = SoftWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                    Text(section.second, color = GrayText, fontSize = 11.sp)
                                }

                                // فلش راهنما سمت چپ
                                Icon(Icons.Default.ChevronLeft, null, tint = GoldClassic.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.1f)) // فاصله کوچک از پایین صفحه
            }
        }
    }
}
fun loadLessonDetailFromJson(context: Context, lessonId: String): LessonDetail? {
    return try {
        val jsonString = context.assets.open("negotiation_data.json").bufferedReader().use { it.readText() }

        // ۱. ابتدا کل فایل را به عنوان یک Object می‌خوانیم
        val rootObject = JsonParser.parseString(jsonString).asJsonObject

        // ۲. حالا لیست دروس را از داخل کلید "negotiation_items" استخراج می‌کنیم
        val lessonsArray = rootObject.getAsJsonArray("negotiation_items")

        var foundDetail: LessonDetail? = null

        // ۳. جستجو در لیست دروس برای پیدا کردن ID مورد نظر
        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            if (lessonObject.get("lesson_id").asString == lessonId) {
                // استفاده از Gson برای تبدیل مستقیم JSON به Object کاتلین
                foundDetail = Gson().fromJson(lessonObject, LessonDetail::class.java)
                break
            }
        }
        foundDetail
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TechniqueDetailScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current

    // لود کردن دیتای واقعی از JSON
    val lesson = remember(lessonId) { loadLessonDetailFromJson(context, lessonId) }

    if (lesson == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("محتوای درس یافت نشد", color = Color.White)
        }
        return
    }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(lesson.title, color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // بخش محتوای متنی (اسکرول شونده)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // ۱. نقل قول ابتدایی
                Text(
                    text = lesson.quote ?: "«در ماتریکس، دانش قدرت است.»",
                    color = GoldClassic,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )

                // ۲. متن اصلی آموزش
                Text(
                    text = lesson.content,
                    color = SoftWhite.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(32.dp))

                // ۳. باکس کد تقلب (نمایش شرطی)
                lesson.cheatCode?.let { code ->
                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Terminal, "Cheat", tint = GoldClassic, modifier = Modifier.size(20.dp))
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "کد تقلب (CHEAT CODE)",
                                    color = GoldClassic,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    letterSpacing = 2.sp
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = code,
                                color = SoftWhite,
                                fontSize = 14.sp,
                                lineHeight = 24.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }

            // بخش پلیر صوتی ثابت در پایین صفحه
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = CardBg.copy(alpha = 0.95f),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.15f))
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Slider(
                        value = 0.4f,
                        onValueChange = {},
                        modifier = Modifier.height(20.dp),
                        colors = SliderDefaults.colors(
                            thumbColor = GoldClassic,
                            activeTrackColor = GoldClassic,
                            inactiveTrackColor = GrayText.copy(alpha = 0.2f)
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("01:12", color = GrayText, fontSize = 10.sp)
                        Text("02:30", color = GrayText, fontSize = 10.sp)
                    }

                    Spacer(Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Text("1x", color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Icon(Icons.Default.SkipPrevious, null, tint = SoftWhite, modifier = Modifier.size(28.dp))

                        Surface(
                            modifier = Modifier.size(56.dp).clickable { /* Play Action */ },
                            color = GoldClassic,
                            shape = CircleShape,
                            shadowElevation = 8.dp
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.PlayArrow, null, tint = DeepNavy, modifier = Modifier.size(32.dp))
                            }
                        }

                        Icon(Icons.Default.SkipNext, null, tint = SoftWhite, modifier = Modifier.size(28.dp))
                        Icon(Icons.Default.GraphicEq, null, tint = GoldClassic.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
                    }
                }
            }
        }
    }
}