package com.example.matrix

// صفحات چهارگانه تکنیک‌های زبان بدن (آموزش، داستان، سناریو، برنامه عملی)

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AdsClick
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.google.gson.reflect.TypeToken

// --- لود دیتای درس زبان بدن از JSON (استفاده مجدد از LessonDetail) ---
fun loadBodyLessonDetailFromJson(context: Context, lessonId: String): LessonDetail? {
    return try {
        val jsonString = context.assets.open("body_language.json").bufferedReader().use { it.readText() }
        val rootObject = JsonParser.parseString(jsonString).asJsonObject
        val lessonsArray = rootObject.getAsJsonArray("body_items")

        var foundDetail: LessonDetail? = null
        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            if (lessonObject.get("lesson_id").asString == lessonId) {
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

// --- صفحه انتخاب چهار بخش برای زبان بدن ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyLessonContentScreen(
    lessonId: String,
    lessonTitle: String,
    navController: NavController
) {
    val sections = listOf(
        Triple("آموزش زبان بدن", "متن و صوت آموزشی", Icons.Default.AutoStories),
        Triple("داستان واقعی زبان بدن", "نمونه‌های واقعی از میدان", Icons.Default.MenuBook),
        Triple("تمرین سناریو زبان بدن", "تحلیل زبان بدن در موقعیت‌ها", Icons.Default.Psychology),
        Triple("برنامه عملی زبان بدن", "تمارین روزمره در دنیای واقعی", Icons.Default.AdsClick)
    )

    Box(modifier = Modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f)))

        Scaffold(containerColor = Color.Transparent) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, start = 20.dp, end = 20.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = GoldClassic,
                        modifier = Modifier
                            .size(22.dp)
                            .align(Alignment.TopStart)
                            .clickable { navController.popBackStack() }
                    )

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "MATRIX BODY LANGUAGE",
                            color = GoldClassic.copy(alpha = 0.6f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 5.sp
                        )
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = lessonTitle,
                            color = SoftWhite,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(0.2f))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    items(sections.size) { index ->
                        val section = sections[index]
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(105.dp)
                                .clickable {
                                    when (section.first) {
                                        "آموزش زبان بدن" -> navController.navigate("body_lesson_detail_view/$lessonId")
                                        "داستان واقعی زبان بدن" -> navController.navigate("body_real_story_view/$lessonId")
                                        "تمرین سناریو زبان بدن" -> navController.navigate("body_scenario_view/$lessonId")
                                        "برنامه عملی زبان بدن" -> navController.navigate("body_action_plan_view/$lessonId")
                                    }
                                },
                            color = CardBg.copy(alpha = 0.75f),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(0.5.dp, GoldClassic.copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 20.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
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

                                Icon(Icons.Default.ChevronLeft, null, tint = GoldClassic.copy(alpha = 0.3f), modifier = Modifier.size(20.dp))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(0.1f))
            }
        }
    }
}

// --- صفحه آموزش متنی زبان بدن ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyTechniqueDetailScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current
    val lesson = remember(lessonId) { loadBodyLessonDetailFromJson(context, lessonId) }

    if (lesson == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("محتوای درس زبان بدن یافت نشد", color = Color.White)
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
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                Text(
                    text = lesson.quote ?: "«بدن تو قبل از کلماتت قضاوت می‌شود.»",
                    color = GoldClassic,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp)
                )

                Text(
                    text = lesson.content,
                    color = SoftWhite.copy(alpha = 0.9f),
                    fontSize = 16.sp,
                    lineHeight = 30.sp,
                    textAlign = TextAlign.Justify
                )

                Spacer(modifier = Modifier.height(32.dp))

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
                                    text = "کد تقلب زبان بدن (CHEAT CODE)",
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

// --- داستان زبان بدن ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyRealStoryScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current
    val lesson = remember(lessonId) { loadBodyLessonDetailFromJson(context, lessonId) }
    val storyData = lesson?.realStory

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("داستان واقعی زبان بدن", color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold) },
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
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("داستانی برای این درس زبان بدن یافت نشد.", color = GrayText)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
            ) {
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
                                Icons.Default.HistoryEdu,
                                contentDescription = null,
                                tint = GoldClassic,
                                modifier = Modifier.padding(15.dp)
                            )
                        }

                        Spacer(Modifier.height(12.dp))

                        storyData.title?.let {
                            Text(it, color = SoftWhite, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, textAlign = TextAlign.Center)
                        }
                        Text(lesson.source ?: "ماتریکس زبان بدن", color = GoldClassic.copy(alpha = 0.7f), fontSize = 12.sp, letterSpacing = 1.sp)
                    }
                }

                Column(modifier = Modifier.padding(horizontal = 24.dp)) {
                    Text(
                        text = "روایت زبان بدن در میدان",
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

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = Color.Black.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "نکته زبان بدن: ${storyData.moral}",
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

// --- سناریو زبان بدن ---
fun loadBodyScenariosFromJson(context: Context, lessonId: String): List<LessonScenario> {
    return try {
        val jsonString = context.assets.open("body_language.json").bufferedReader().use { it.readText() }
        val rootObject = JsonParser.parseString(jsonString).asJsonObject
        val lessonsArray = rootObject.getAsJsonArray("body_items")

        var foundScenarios = emptyList<LessonScenario>()

        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            if (lessonObject.get("lesson_id").asString == lessonId) {
                if (lessonObject.has("scenarios")) {
                    val scenariosArray = lessonObject.getAsJsonArray("scenarios")
                    val type = object : TypeToken<List<LessonScenario>>() {}.type
                    foundScenarios = Gson().fromJson(scenariosArray, type)
                }
                break
            }
        }
        foundScenarios
    } catch (e: Exception) {
        Log.e("MATRIX_BODY_ERROR", "Body Scenario Load Failed: ${e.message}")
        emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyScenarioScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current
    val scenarios = remember(lessonId) { loadBodyScenariosFromJson(context, lessonId) }

    var showSuccessDialog by remember { mutableStateOf(false) }
    var currentStep by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showAnalysis by remember { mutableStateOf(false) }

    if (scenarios.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("سناریویی برای این تکنیک زبان بدن یافت نشد.", color = Color.White)
        }
        return
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = CardBg,
            title = { Text("مأموریت زبان بدن انجام شد", color = GoldClassic, fontWeight = FontWeight.Bold) },
            text = { Text("شما تمام سناریوهای این درس زبان بدن را بررسی کردید.", color = SoftWhite) },
            confirmButton = {
                Button(
                    onClick = { navController.popBackStack() },
                    colors = ButtonDefaults.buttonColors(containerColor = GoldClassic)
                ) {
                    Text("بازگشت", color = DeepNavy)
                }
            }
        )
    }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("تمرین سناریو زبان بدن (${currentStep + 1}/${scenarios.size})", color = SoftWhite, fontSize = 16.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        Column(modifier = Modifier.fillMaxSize().padding(padding).padding(24.dp)) {

            LinearProgressIndicator(
                progress = { (currentStep + 1).toFloat() / scenarios.size.toFloat() },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                color = GoldClassic,
                trackColor = GrayText.copy(alpha = 0.2f),
            )

            Spacer(Modifier.height(32.dp))

            Text(
                text = scenarios[currentStep].question,
                color = SoftWhite,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                lineHeight = 28.sp
            )

            Spacer(Modifier.height(32.dp))

            scenarios[currentStep].options.forEachIndexed { index, option ->
                val isSelected = selectedOption == index
                val isCorrect = index == scenarios[currentStep].correctIndex

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .clickable(enabled = !showAnalysis) { selectedOption = index },
                    color = when {
                        showAnalysis && isCorrect -> Color(0xFF2E7D32).copy(alpha = 0.2f)
                        showAnalysis && isSelected -> Color(0xFFC62828).copy(alpha = 0.2f)
                        isSelected -> GoldClassic.copy(alpha = 0.1f)
                        else -> CardBg
                    },
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(
                        1.dp,
                        when {
                            showAnalysis && isCorrect -> Color(0xFF4CAF50)
                            showAnalysis && isSelected -> Color(0xFFEF5350)
                            isSelected -> GoldClassic
                            else -> Color.Transparent
                        }
                    )
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = option,
                            color = if (isSelected || (showAnalysis && isCorrect)) SoftWhite else GrayText,
                            modifier = Modifier.weight(1f)
                        )
                        if (showAnalysis && isCorrect) Icon(Icons.Default.CheckCircle, null, tint = Color(0xFF4CAF50))
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AnimatedVisibility(visible = showAnalysis) {
                Surface(
                    color = Color.Black.copy(alpha = 0.3f),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = scenarios[currentStep].analysis,
                        color = GrayText,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (!showAnalysis) {
                        showAnalysis = true
                    } else {
                        if (currentStep < scenarios.size - 1) {
                            currentStep++
                            selectedOption = null
                            showAnalysis = false
                        } else {
                            showSuccessDialog = true
                        }
                    }
                },
                enabled = selectedOption != null,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GoldClassic),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = if (!showAnalysis) "بررسی پاسخ" else if (currentStep < scenarios.size - 1) "سوال بعدی" else "اتمام تمرین زبان بدن",
                    color = DeepNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

// --- برنامه عملی زبان بدن ---
data class BodyActionPlan(
    val title: String? = null,
    val task: String? = null,
    val note: String? = null
)

fun loadBodyActionPlanFromAsset(context: Context, lessonId: String): BodyActionPlan? {
    return try {
        val jsonString = context.assets.open("body_language.json").bufferedReader().use { it.readText() }
        val rootObject = JsonParser.parseString(jsonString).asJsonObject
        val lessonsArray = rootObject.getAsJsonArray("body_items")

        var foundPlan: BodyActionPlan? = null
        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            if (lessonObject.get("lesson_id").asString == lessonId) {
                if (lessonObject.has("action_plan")) {
                    val planObject = lessonObject.getAsJsonObject("action_plan")
                    foundPlan = Gson().fromJson(planObject, BodyActionPlan::class.java)
                }
                break
            }
        }
        foundPlan
    } catch (e: Exception) {
        Log.e("MATRIX_BODY_ERROR", "Body ActionPlan Load Failed: ${e.message}")
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BodyActionPlanScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current
    val actionPlan = remember(lessonId) { loadBodyActionPlanFromAsset(context, lessonId) }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("برنامه عملیاتی زبان بدن", color = SoftWhite, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        if (actionPlan == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("مأموریت زبان بدن برای این درس تعریف نشده است.", color = GrayText)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Default.Assignment,
                    contentDescription = null,
                    tint = GoldClassic,
                    modifier = Modifier.size(64.dp)
                )

                Spacer(Modifier.height(24.dp))

                actionPlan.title?.let {
                    Text(
                        text = it,
                        color = GoldClassic,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(32.dp))

                Surface(
                    color = CardBg,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    actionPlan.task?.let {
                        Text(
                            text = it,
                            color = SoftWhite,
                            fontSize = 17.sp,
                            lineHeight = 30.sp,
                            modifier = Modifier.padding(20.dp),
                            textAlign = TextAlign.Right
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                Surface(
                    color = Color.White.copy(alpha = 0.05f),
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayText.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb,
                            contentDescription = null,
                            tint = GoldClassic,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(12.dp))
                        actionPlan.note?.let {
                            Text(
                                text = it,
                                color = GrayText,
                                fontSize = 14.sp,
                                lineHeight = 22.sp,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }

                Spacer(Modifier.weight(1f))

                Button(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldClassic),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("پذیرفتن مأموریت زبان بدن", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}

