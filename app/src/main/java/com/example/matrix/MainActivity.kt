package com.example.matrix

import StrategyRoomScreen
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.Handyman
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Hub
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WorkHistory
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Log
import androidx.navigation.NavType
import androidx.navigation.navArgument

// --- پالت رنگی لوکس ماتریکس ---
val DeepNavy = Color(0xFF0A0E1A)
val GoldClassic = Color(0xFFD4AF37)
val SoftWhite = Color(0xFFFFFFFF)
val CardBg = Color(0xFF161B29)
val GrayText = Color(0xFFB0B0B0)

class MainActivity : ComponentActivity() {
    // تابع کمکی برای خواندن فایل
    fun getJsonFromAssets(context: Context, fileName: String): String {
        return context.assets.open(fileName).bufferedReader().use { it.readText() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val repository = JobRepository(applicationContext)

        setContent {
            // ۱. ویومدل مشاغل (موجود)
            val jobViewModel: JobViewModel = viewModel(
                factory = object : ViewModelProvider.Factory {
                    override fun <T : ViewModel> create(modelClass: Class<T>): T {
                        return JobViewModel(repository) as T
                    }
                }
            )

            // ۲. ویومدل آزمون (جدید و اصولی)
            val negViewModel: NegotiationViewModel = viewModel()
            val context = LocalContext.current

            LaunchedEffect(Unit) {
                try {
                    val jsonData = getJsonFromAssets(context, "negotiation_data.json")
                    negViewModel.loadData(jsonData) // نام تابع اصلاح شد
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                val navController = rememberNavController()
                var selectedJob by remember { mutableStateOf<Job?>(null) }

                Surface(modifier = Modifier.fillMaxSize(), color = DeepNavy) {
                    NavHost(navController = navController, startDestination = "dashboard") {

                        composable("dashboard") {
                            MatrixProDashboard(
                                onNavigateToJobs = { navController.navigate("job_bank") },
                                onNavigateToStrategy = { navController.navigate("strategy_room") }
                            )
                        }

                        composable("job_bank") {
                            JobsBankScreen(
                                jobs = jobViewModel.jobs,
                                isLoading = jobViewModel.isLoading,
                                navController = navController,
                                onJobClick = { job ->
                                    selectedJob = job
                                    navController.navigate("job_detail")
                                }
                            )
                        }

                        composable("job_detail") {
                            val job = remember { selectedJob }
                            if (job != null) {
                                JobDetailScreen(
                                    job = job,
                                    onBack = { navController.navigateUp() },
                                    onStartQuiz = { navController.navigate("quiz") }
                                )
                            }
                        }

                        composable("exam_screen/{levelName}") { backStackEntry ->
                            val levelName = backStackEntry.arguments?.getString("levelName") ?: "سطح مقدماتی 1"

                            // فیلتر کردن سوالات بر اساس سطح انتخابی
                            val filteredQuestions = negViewModel.finalExamQuestions.value.filter {
                                it.level.trim() == levelName.trim()
                            }

                            ExamScreen(
                                questions = filteredQuestions,
                                navController = navController,
                                onScoreSaved = { finalScore ->
                                    val prefs = context.getSharedPreferences("user_data", Context.MODE_PRIVATE)
                                    // ذخیره امتیاز با کلید مخصوص هر سطح (مثلاً score_مقدماتی_2)
                                    val keySuffix = levelName.replace("سطح ", "").replace(" ", "_")
                                    prefs.edit().putInt("score_$keySuffix", finalScore).apply()
                                }
                            )
                        }

                        // سایر مسیرهای شما بدون تغییر...
                        composable("quiz") {
                            val jobForQuiz = remember { selectedJob }
                            if (jobForQuiz != null) {
                                QuizScreen(job = jobForQuiz) { navController.popBackStack() }
                            }
                        }

                        composable("strategy_room") { StrategyRoomScreen(navController = navController) }

                        composable("negotiation_levels") { NegotiationLevelsScreen(navController) }

                        composable("lesson_content/{lessonId}/{lessonTitle}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            val lessonTitle = backStackEntry.arguments?.getString("lessonTitle") ?: ""
                            LessonContentScreen(lessonId, lessonTitle, navController)
                        }

                        composable("lesson_detail_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            TechniqueDetailScreen(lessonId, navController)
                        }

                        composable("real_story_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            RealStoryScreen(lessonId, navController)
                        }

                        composable("scenario_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            ScenarioScreen(lessonId, navController)
                        }

                        composable("action_plan_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "1"
                            ActionPlanScreen(lessonId, navController)
                        }

                        // --- مسیرهای اختصاصی تکنیک‌های فروش ---
                        composable("sales_levels") { SalesLevelsScreen(navController) }

                        composable("sales_lesson_content/{lessonId}/{lessonTitle}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            val lessonTitle = backStackEntry.arguments?.getString("lessonTitle") ?: ""
                            SalesLessonContentScreen(lessonId, lessonTitle, navController)
                        }

                        composable("sales_lesson_detail_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            SalesTechniqueDetailScreen(lessonId, navController)
                        }

                        composable("sales_real_story_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            SalesRealStoryScreen(lessonId, navController)
                        }

                        composable("sales_scenario_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            SalesScenarioScreen(lessonId, navController)
                        }

                        composable("sales_action_plan_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "1"
                            SalesActionPlanScreen(lessonId, navController)
                        }

                        composable("sales_exam_screen/{levelName}") { backStackEntry ->
                            val levelName = backStackEntry.arguments?.getString("levelName") ?: "سطح مقدماتی 1"
                            SalesExamScreen(levelName, navController)
                        }

                        // --- مسیرهای اختصاصی تکنیک‌های زبان بدن ---
                        composable("body_levels") { BodyLanguageLevelsScreen(navController) }

                        composable("body_lesson_content/{lessonId}/{lessonTitle}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            val lessonTitle = backStackEntry.arguments?.getString("lessonTitle") ?: ""
                            BodyLessonContentScreen(lessonId, lessonTitle, navController)
                        }

                        composable("body_lesson_detail_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            BodyTechniqueDetailScreen(lessonId, navController)
                        }

                        composable("body_real_story_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            BodyRealStoryScreen(lessonId, navController)
                        }

                        composable("body_scenario_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: ""
                            BodyScenarioScreen(lessonId, navController)
                        }

                        composable("body_action_plan_view/{lessonId}") { backStackEntry ->
                            val lessonId = backStackEntry.arguments?.getString("lessonId") ?: "1"
                            BodyActionPlanScreen(lessonId, navController)
                        }

                        composable("body_exam_screen/{levelName}") { backStackEntry ->
                            val levelName = backStackEntry.arguments?.getString("levelName") ?: "سطح مقدماتی 1"
                            BodyExamScreen(levelName, navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatrixProDashboard(
    onNavigateToJobs: () -> Unit,
    onNavigateToStrategy: () -> Unit) {
    Scaffold(
        bottomBar = { MatrixBottomNav() },
        containerColor = DeepNavy
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            UserHeaderSection()
            Spacer(modifier = Modifier.height(24.dp))
            AIConsultantCard()
            Spacer(modifier = Modifier.height(24.dp))
            Text("وضعیت خروج از ماتریکس", color = GoldClassic, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            ProgressAndMissionCard()
            Spacer(modifier = Modifier.height(24.dp))
            Text("بانک ابزارهای رشد", color = GoldClassic, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))

            ToolsGrid(
                onNavigateToJobs = onNavigateToJobs,
                onNavigateToStrategy = onNavigateToStrategy
            )
        }
    }
}

@Composable
fun UserHeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text("سلام سینا عزیز،", color = GrayText, fontSize = 13.sp)
            Text("آماده خروج از ماتریکسی؟", color = SoftWhite, fontSize = 17.sp, fontWeight = FontWeight.Bold)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator(
                    progress = { 0.6f }, // اصلاح شده برای نسخه جدید مدیا ۳
                    color = GoldClassic,
                    trackColor = GoldClassic.copy(alpha = 0.1f),
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(40.dp)
                )
                Text("60%", color = GoldClassic, fontSize = 10.sp, fontWeight = FontWeight.Bold)
            }
            Text("عادت روزانه", color = GoldClassic, fontSize = 10.sp)
        }
    }
}

@Composable
fun AIConsultantCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
            },
        colors = CardDefaults.cardColors(containerColor = GoldClassic),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp).background(DeepNavy, CircleShape), contentAlignment = Alignment.Center) {
                Icon(Icons.Default.AutoAwesome, null, tint = GoldClassic, modifier = Modifier.size(28.dp))
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text("دستیار هوش مصنوعی", color = DeepNavy, fontWeight = FontWeight.ExtraBold, fontSize = 17.sp)
                Text("سوالت رو بپرس، مسیرت رو پیدا کن...", color = DeepNavy.copy(alpha = 0.8f), fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ProgressAndMissionCard() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = CardBg,
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                val progressData = listOf(0.4f, 0.7f, 0.5f, 0.9f, 0.6f, 0.8f, 1f)
                progressData.forEach { h ->
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .fillMaxHeight(h)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (h == 1f) GoldClassic else GoldClassic.copy(alpha = 0.3f))
                    )
                }
            }
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = GrayText.copy(alpha = 0.1f))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Flag, null, tint = GoldClassic, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("ماموریت: مطالعه ۱۰ صفحه از کتاب 'پدر پولدار'", color = SoftWhite, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun ToolsGrid(
    onNavigateToJobs: () -> Unit,
    onNavigateToStrategy: () -> Unit // ورودی جدید اضافه شد
) {
    val tools = listOf(
        "بانک مشاغل" to Icons.Default.WorkHistory,
        "بانک کتاب" to Icons.Default.MenuBook,
        "اتاق استراتژی" to Icons.Default.Hub,
        "جعبه ابزار" to Icons.Default.Handyman
    )
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        tools.chunked(2).forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                row.forEach { tool ->
                    ToolCard(
                        name = tool.first,
                        icon = tool.second,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // مدیریت هوشمند کلیک‌ها
                            when (tool.first) {
                                "بانک مشاغل" -> onNavigateToJobs()
                                "اتاق استراتژی" -> onNavigateToStrategy()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ToolCard(name: String, icon: ImageVector, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        modifier = modifier
            .height(90.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        color = CardBg,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(0.5.dp, GrayText.copy(alpha = 0.2f))
    ) {
        Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, null, tint = GoldClassic, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(6.dp))
            Text(name, color = SoftWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JobsBankScreen(
    jobs: List<Job>,           // دیتای ورودی از ViewModel
    isLoading: Boolean,        // وضعیت لودینگ از ViewModel
    navController: NavController,
    onJobClick: (Job) -> Unit
) {
    // حذف کدهای LaunchedEffect و Gson قدیمی چون دیتا از ورودی میاد

    val categories = remember {
        listOf("همه", "دیجیتال", "مالی", "فنی", "محتوا", "سلامت", "انرژی", "حقوقی", "مدیریت", "هنر و زیبایی", "طراحی")
    }
    var selectedCategory by remember { mutableStateOf("همه") }
    var searchQuery by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // فیلتر کردن بر اساس لیست ورودی (jobs) + جستجو
    val filteredJobs = remember(selectedCategory, searchQuery, jobs) {
        val baseList = if (selectedCategory == "همه") {
            jobs
        } else {
            jobs.filter { it.category.equals(selectedCategory, ignoreCase = true) }
        }

        if (searchQuery.isBlank()) {
            baseList
        } else {
            val query = searchQuery.trim()
            baseList.filter { job ->
                job.title.contains(query, ignoreCase = true) ||
                        job.category.contains(query, ignoreCase = true) ||
                        job.about.contains(query, ignoreCase = true)
            }
        }
    }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("بانک مشاغل ماتریکس", color = SoftWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(40.dp)
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = null
                            ) {
                                navController.popBackStack()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldClassic
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = CardBg)
            )
        }
    ) { padding ->
        // مدیریت وضعیت لودینگ یا لیست خالی
        if (isLoading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = GoldClassic)
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(padding)) {
                // بخش انتخاب دسته‌بندی
                LazyRow(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories) { category ->
                        val isSelected = selectedCategory == category
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null
                                ) { selectedCategory = category },
                            color = if (isSelected) GoldClassic else CardBg,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) GoldClassic else GrayText.copy(alpha = 0.2f)
                            )
                        ) {
                            Text(
                                text = category,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                color = if (isSelected) DeepNavy else GrayText,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                // فیلد جستجو با تم مشکی و طلایی
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    singleLine = true,
                    placeholder = {
                        Text(
                            text = "جستجوی شغل بر اساس عنوان یا دسته‌بندی",
                            color = GrayText,
                            fontSize = 12.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = GoldClassic
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotBlank()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "پاک کردن جستجو",
                                tint = GrayText,
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null
                                    ) {
                                        searchQuery = ""
                                        focusManager.clearFocus()
                                    }
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = CardBg,
                        unfocusedContainerColor = CardBg,
                        disabledContainerColor = CardBg,
                        focusedIndicatorColor = GoldClassic,
                        unfocusedIndicatorColor = GrayText.copy(alpha = 0.5f),
                        cursorColor = GoldClassic,
                        focusedTextColor = SoftWhite,
                        unfocusedTextColor = SoftWhite,
                        focusedLeadingIconColor = GoldClassic,
                        unfocusedLeadingIconColor = GoldClassic
                    ),
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Search
                    ),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            focusManager.clearFocus()
                        }
                    )
                )

                // لیست مشاغل فیلتر شده
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            "مسیر ثروت ($selectedCategory):",
                            color = GrayText,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    if (filteredJobs.isEmpty() && searchQuery.isNotBlank()) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 32.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "موردی یافت نشد",
                                    color = GrayText,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    } else {
                        items(items = filteredJobs, key = { it.id }) { job -> // بهتره کلید بر اساس id باشه
                            JobCardItem(
                                job = job,
                                onClick = { onJobClick(job) },
                                modifier = Modifier.animateItem()
                            )
                        }
                    }
                    item { Spacer(modifier = Modifier.height(20.dp)) }
                }
            }
        }
    }
}

@Composable
fun JobCardItem(job: Job, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { onClick() },
        color = CardBg,
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(50.dp).background(DeepNavy, RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = getIconFromName(job.iconName),
                    contentDescription = null,
                    tint = GoldClassic
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(job.title, color = SoftWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(job.category, color = GrayText, fontSize = 12.sp)
            }
        }
    }
}

@Composable
fun MatrixBottomNav() {
    NavigationBar(containerColor = CardBg) {
        val navColor = NavigationBarItemDefaults.colors(
            selectedIconColor = GoldClassic,
            unselectedIconColor = GrayText,
            selectedTextColor = GoldClassic,
            unselectedTextColor = GrayText,
            indicatorColor = DeepNavy
        )
        NavigationBarItem(selected = true, onClick = {}, icon = { Icon(Icons.Default.Home, null) }, label = { Text("خانه", fontSize = 11.sp) }, colors = navColor)
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.PlayCircle, null) }, label = { Text("دوره‌ها", fontSize = 11.sp) }, colors = navColor)
        NavigationBarItem(selected = false, onClick = {}, icon = { Icon(Icons.Default.Person, null) }, label = { Text("پروفایل", fontSize = 11.sp) }, colors = navColor)
    }
}

@Composable
fun JobDetailScreen(job: Job, onBack: () -> Unit, onStartQuiz: (Job) -> Unit) {
    val scrollState = rememberScrollState()
    BackHandler { onBack() }

    Scaffold(
        containerColor = DeepNavy,
        bottomBar = {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // راه حل اصلی: دکمه را دقیقاً بالای دکمه‌های گوشی نگه می‌دارد
                    .background(DeepNavy),
                color = DeepNavy, // حتماً رنگ پس‌زمینه Surface را هم ست کن
                shadowElevation = 0.dp // اگر هاله سفیدی می‌بینی، سایه را صفر کن
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp) // پدینگ استاندارد
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .clickable { onStartQuiz(job) },
                        colors = CardDefaults.cardColors(containerColor = GoldClassic),
                        border = BorderStroke(1.dp, Color.Black.copy(alpha = 0.1f)),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(0.dp) // حذف سایه کارت برای از بین بردن سفیدی دور
                    ) {
                        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(
                                text = "شروع تستِ لیاقت ماتریکس",
                                color = DeepNavy,
                                style = TextStyle(
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp,
                                    fontFamily = FontFamily.SansSerif
                                )
                            )
                        }
                    }
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(scrollState)
        ) {
            JobDetailHeader(job, onBack)

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {

                // --- بخش ۱: معرفی جامع شغل (۱۰ خط) ---
                // داخل بخش UI صفحه جزییات


                // --- بخش ۲: آمار سریع (شامل درآمد و مدت زمان) ---
                QuickStatsRow(job)

                Spacer(modifier = Modifier.height(15.dp))

                JobVoiceIntroPlayer(job.title)

                Text(
                    text = "درباره این تخصص",
                    color = GoldClassic,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = job.about,
                    color = SoftWhite.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                // --- بخش ۳: مراحل فتح مأموریت ---
                Text("🗺️ نقشه راه و مراحل فتح", color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                job.gameSteps.forEachIndexed { index, step ->
                    RoadmapStepItem(step, index == job.gameSteps.size - 1)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- بخش ۴: اشتباهات مرگبار ---
                DangerZoneSection(job.deadlyMistakes)

                Spacer(modifier = Modifier.height(24.dp))

                // --- بخش ۵: حقیقت ممنوعه ---
                ForbiddenTruthSection(job.forbiddenTruth)

                Spacer(modifier = Modifier.height(32.dp))

                // --- بخش ۶: داستان‌های واقعی (با جزئیات بیشتر) ---
                Text("🌟 فاتحانِ این مسیر (داستان‌های واقعی)", color = SoftWhite, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                job.successStories.forEach { story ->
                    DetailedSuccessStoryCard(story)
                }

                Spacer(modifier = Modifier.height(120.dp))
            }
        }
    }
}

@Composable
fun QuickStatsRow(job: Job) {
    val statItems = listOf(
        "درآمد" to job.income,
        "یادگیری" to job.stats.learningCurve,
        "استرس" to "${job.stats.stressLevel}/10",
        "امنیت" to job.stats.jobSecurity,
        "سرمایه" to job.stats.minCapital,
        "تهدید AI" to job.stats.aiThreat
    )
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(statItems) { item ->
            Card(
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(0.5.dp, GoldClassic.copy(0.3f)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(Modifier.padding(horizontal = 16.dp, vertical = 10.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(item.first, color = GrayText, fontSize = 10.sp)
                    Text(item.second, color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun DangerZoneSection(mistakes: List<String>) {
    Surface(
        color = Color(0xFFFF5252).copy(alpha = 0.05f),
        border = BorderStroke(1.dp, Color(0xFFFF5252).copy(alpha = 0.2f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("❌ اگر این کارها را بکنی، شکست می‌خوری!", color = Color(0xFFFF5252), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
            mistakes.forEach { mistake ->
                Text("• $mistake", color = SoftWhite.copy(0.8f), fontSize = 13.sp, modifier = Modifier.padding(vertical = 2.dp))
            }
        }
    }
}

@Composable
fun DetailedSuccessStoryCard(story: SuccessStory) {
    Card(
        modifier = Modifier
            .fillMaxWidth() // تغییر از 300dp به fillMaxWidth برای پر کردن عرض صفحه
            .padding(horizontal = 16.dp, vertical = 8.dp), // پدینگ متوازن برای حذف فضای خالی
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, GoldClassic.copy(0.15f)) // کمی پررنگ‌تر برای وضوح بیشتر در سایز بزرگ
    ) {
        Column(Modifier.padding(20.dp)) { // افزایش پدینگ داخلی برای ابهت بیشتر
            // هدر کارت (نام و نقش)
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier
                        .size(50.dp) // بزرگتر شدن دایره پروفایل
                        .background(GoldClassic, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = story.name.take(1),
                        color = DeepNavy,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp // درشت‌تر شدن حرف اول
                    )
                }
                Spacer(Modifier.width(16.dp))
                Column {
                    Text(
                        text = story.name,
                        color = SoftWhite,
                        fontSize = 15.sp, // درشت‌تر شدن نام
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = story.role,
                        color = GoldClassic,
                        fontSize = 14.sp // درشت‌تر شدن نقش
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            // تیتر داستان
            Text(
                text = story.storyTitle,
                color = SoftWhite,
                fontSize = 15.sp, // درشت‌تر شدن تیتر
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 24.sp
            )

            // متن یکپارچه داستان
            Text(
                text = story.fullNarrative,
                color = GrayText,
                fontSize = 13.sp, // درشت‌تر شدن متن اصلی (طبق درخواست شما)
                lineHeight = 22.sp, // افزایش فاصله خطوط برای خوانایی بهتر در سایز بزرگ
                textAlign = TextAlign.Justify, // تراز کردن متن برای پر کردن فضاهای خالی
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}

// کدهای کمکی هدر و رودمپ (مشابه قبل با کمی بهبود بصری)
@Composable
fun JobDetailHeader(job: Job, onBack: () -> Unit) {
    Box(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
        // دکمه بازگشت
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = null,
            tint = GoldClassic,
            modifier = Modifier.size(24.dp).clickable { onBack() }
        )

        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            // اصلاح خط ارور: تبدیل نام رشته‌ای به آیکون واقعی
            Icon(
                imageVector = getIconFromName(job.iconName), // اینجا از تابع مبدل استفاده می‌کنیم
                contentDescription = null,
                tint = GoldClassic,
                modifier = Modifier.size(40.dp)
            )

            Text(job.title, color = SoftWhite, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text(job.income, color = GrayText, fontSize = 13.sp)
        }
    }
}

@Composable
fun ForbiddenTruthSection(truth: String) {
    Surface(color = CardBg, shape = RoundedCornerShape(16.dp), border = BorderStroke(1.dp, GoldClassic.copy(0.1f))) {
        Row(Modifier.padding(16.dp)) {
            Icon(Icons.Default.Warning, null, tint = GoldClassic, modifier = Modifier.size(24.dp))
            Spacer(Modifier.width(12.dp))
            Column {
                Text("حقیقت ممنوعه", color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(truth, color = SoftWhite, fontSize = 13.sp, lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun RoadmapStepItem(step: RoadmapStep, isLast: Boolean) {
    Row(modifier = Modifier.height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(Modifier.size(28.dp).background(GoldClassic, CircleShape), contentAlignment = Alignment.Center) {
                Text(step.level.replace("Level ", ""), color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            if (!isLast) Box(Modifier.width(1.dp).fillMaxHeight().background(GoldClassic.copy(0.3f)))
        }
        Spacer(Modifier.width(16.dp))
        Column(Modifier.padding(bottom = 20.dp)) {
            Text(step.title, color = SoftWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            Text(step.description, color = GrayText, fontSize = 13.sp)
        }
    }
}

@Composable
fun QuizScreen(job: Job, onFinish: () -> Unit) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showDialog by remember { mutableStateOf(false) }

    val quiz = job.compatibilityQuiz

    if (quiz.isEmpty()) {
        LaunchedEffect(Unit) { onFinish() }
        return
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (currentQuestionIndex < quiz.size) {
            Text(
                text = "سوال ${currentQuestionIndex + 1} از ${quiz.size}",
                color = GoldClassic,
                fontSize = 14.sp
            )
            Spacer(Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth().height(200.dp),
                colors = CardDefaults.cardColors(containerColor = CardBg),
                border = BorderStroke(1.dp, GoldClassic.copy(0.2f))
            ) {
                Box(Modifier.fillMaxSize().padding(20.dp), contentAlignment = Alignment.Center) {
                    Text(
                        text = quiz[currentQuestionIndex].question,
                        color = SoftWhite,
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(Modifier.height(40.dp))

            // --- استفاده از دکمه‌های دستی بدون انیمیشن ریپل برای جلوگیری از کرش ---
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                // دکمه بله (دستی)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .background(GoldClassic, RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // حذف کامل افکت کلیک که باعث کرش می‌شد
                        ) {
                            score++
                            if (currentQuestionIndex < quiz.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showDialog = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("بله", color = DeepNavy, fontWeight = FontWeight.Bold)
                }

                // دکمه خیر (دستی)
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(50.dp)
                        .border(1.dp, SoftWhite, RoundedCornerShape(12.dp))
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null // حذف کامل افکت کلیک
                        ) {
                            if (currentQuestionIndex < quiz.size - 1) {
                                currentQuestionIndex++
                            } else {
                                showDialog = true
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Text("خیر", color = SoftWhite, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (showDialog) {
            val finalPercentage = remember(score) {
                if (quiz.isNotEmpty()) (score.toFloat() / quiz.size * 100).toInt() else 0
            }

            ResultDialog(
                percentage = finalPercentage,
                onDismiss = {
                    showDialog = false // ۱. اول دیالوگ را می‌بندیم
                    onFinish()         // ۲. بعد به صفحه قبلی می‌رویم
                }
            )
        }
    }
}

@Composable
fun ResultDialog(percentage: Int, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = CardBg,
        title = {
            Text(
                "تحلیل نهایی ماتریکس",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = GoldClassic,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                Text("$percentage%", fontSize = 48.sp, fontWeight = FontWeight.Black, color = SoftWhite)
                Spacer(Modifier.height(10.dp))
                val message = when {
                    percentage >= 80 -> "تبریک! تو برای این شغل ساخته شدی."
                    percentage >= 50 -> "شما پتانسیل خوبی دارید، اما نیاز به آموزش دارید."
                    else -> "این مسیر با روحیه شما سازگار نیست."
                }
                Text(message, color = GrayText, textAlign = TextAlign.Center)
            }
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .background(GoldClassic, RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = null
                    ) {
                        // ترفند اصلی اینجاست:
                        // اول به دیالوگ می‌گوییم غیب شو، بعد دستور خروج را اجرا می‌کنیم
                        onDismiss()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text("تایید و بازگشت", color = DeepNavy, fontWeight = FontWeight.Bold)
            }
        },
        shape = RoundedCornerShape(24.dp)
    )
}

//کد پخش ویس برای هر شغل
@Composable
fun JobVoiceIntroPlayer(jobTitle: String) {
    var isPlaying by remember { mutableStateOf(false) }
    var progress by remember { mutableFloatStateOf(0.3f) } // مقدار فرضی برای دمو

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        color = CardBg.copy(alpha = 0.5f),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.2f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // دکمه پلی/پوز
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(GoldClassic, CircleShape)
                    .clickable { isPlaying = !isPlaying },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = null,
                    tint = DeepNavy,
                    modifier = Modifier.size(28.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "بررسی چالش‌\u200cهای $jobTitle",
                    color = SoftWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(8.dp))

                // نوار پیشرفت صدا
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)
                        .clip(CircleShape),
                    color = GoldClassic,
                    trackColor = SoftWhite.copy(alpha = 0.1f),
                )

                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("0:45", color = GrayText, fontSize = 10.sp)
                    Text("2:15", color = GrayText, fontSize = 10.sp)
                }
            }
        }
    }
}