import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.matrix.CardBg
import com.example.matrix.DeepNavy
import com.example.matrix.GoldClassic
import com.example.matrix.SoftWhite
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.example.matrix.Scenario
import kotlinx.coroutines.launch
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun loadScenariosFromAssets(context: Context): List<Scenario> {
    return try {
        // باز کردن فایل از پوشه assets
        val jsonString = context.assets.open("scenarios.json").bufferedReader().use { it.readText() }

        // تبدیل متن به لیست از سناریوها
        val listType = object : TypeToken<List<Scenario>>() {}.type
        Gson().fromJson(jsonString, listType)
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList() // در صورت خطا لیست خالی برمی‌گرداند
    }
}
val Context.dataStore by preferencesDataStore(name = "strategy_settings")

class StrategyStorage(private val context: Context) {
    companion object {
        val LAST_SCENARIO_KEY = intPreferencesKey("last_scenario_index")
    }

    // گرفتن آخرین شماره ذخیره شده (اگر چیزی نبود، 0 برمی‌گرداند)
    val lastScenarioIndex: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[LAST_SCENARIO_KEY] ?: 0
    }

    // ذخیره کردن شماره جدید
    suspend fun saveScenarioIndex(index: Int) {
        context.dataStore.edit { preferences ->
            preferences[LAST_SCENARIO_KEY] = index
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StrategyRoomScreen(navController: NavController) {
    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("اتاق فرماندهی استراتژی", color = SoftWhite, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // --- بخش ۱: نکات محرمانه بیزنس (۶ مربع تکنیک‌ها) ---
            item {
                SectionHeader("نکات محرمانه بیزنس", Icons.Default.Security)
                // اینجا navController را به گرید پاس می‌دهیم تا کارت‌ها بفهمند به کجا باید بروند
                TechniquesGrid(navController = navController)
            }

            // --- بخش ۲: شبیه‌ساز تصمیم‌گیری ---
            item {
                SectionHeader("شبیه‌ساز تصمیم‌گیری", Icons.Default.Psychology)
                ScenarioSimulator()
            }

            item { Spacer(modifier = Modifier.height(40.dp)) }
        }
    }
}

@Composable
fun TechniquesGrid(navController: NavController) { // اضافه شدن navController برای جابجایی بین صفحات
    val techniques = listOf(
        "تکنیک‌های مذاکره" to Icons.Default.Handshake,
        "تکنیک‌های فروش" to Icons.Default.Payments,
        "تکنیک‌های زبان بدن" to Icons.Default.AccessibilityNew,
        "تکنیک‌های قدرت" to Icons.Default.MilitaryTech,
        "تکنیک‌های کاریزما" to Icons.Default.AutoAwesome,
        "تکنیک‌های فن بیان" to Icons.Default.RecordVoiceOver
    )

    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        techniques.chunked(2).forEach { rowItems ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                rowItems.forEach { item ->
                    TechniqueCard(
                        title = item.first,
                        icon = item.second,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            // مدیریت کلیک برای هر کارت
                            when (item.first) {
                                "تکنیک‌های مذاکره" -> navController.navigate("negotiation_levels")
                                "تکنیک‌های فروش" -> navController.navigate("sales_levels")
                                "تکنیک‌های زبان بدن" -> navController.navigate("body_levels")
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun TechniqueCard(
    title: String,
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit // پارامتر کلیک اضافه شد
) {
    Card(
        modifier = modifier
            .height(110.dp)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick() // اجرای عملیات کلیک
            },
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(icon, null, tint = GoldClassic, modifier = Modifier.size(30.dp))
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = SoftWhite,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun ScenarioSimulator() {
    val context = LocalContext.current
    val storage = remember { StrategyStorage(context) }
    val scope = rememberCoroutineScope()

    // ۱. لود کردن لیست سناریوها از فایل JSON (فقط یک‌بار)
    val scenarioList = remember { loadScenariosFromAssets(context) }

    // ۲. خواندن آخرین ایندکس ذخیره شده
    val savedIndex by storage.lastScenarioIndex.collectAsState(initial = 0)

    var currentScenarioIndex by remember { mutableIntStateOf(0) }
    var screenState by remember { mutableStateOf("asking") }
    var userSelection by remember { mutableIntStateOf(0) }

    // همگام‌سازی ایندکس با حافظه
    LaunchedEffect(savedIndex) {
        currentScenarioIndex = savedIndex
    }

    // بررسی وضعیت لیست (اگر خالی بود یا هنوز لود نشده بود)
    if (scenarioList.isEmpty()) {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Text("در حال رمزگشایی سناریوها...", color = GoldClassic)
        }
        return
    }

    // جلوگیری از خروج از محدوده لیست (مثلاً اگر تعداد سوالات در JSON کم شده باشد)
    if (currentScenarioIndex >= scenarioList.size) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("تمام سناریوها با موفقیت به پایان رسید!", color = GoldClassic, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(16.dp))
            MatrixButton("شروع مجدد دوره", Modifier.fillMaxWidth(), isPrimary = true) {
                scope.launch {
                    storage.saveScenarioIndex(0)
                    currentScenarioIndex = 0
                    screenState = "asking"
                }
            }
        }
        return
    }

    val currentScenario = scenarioList[currentScenarioIndex]

    // ۳. نوار پیشرفت (ProgressBar)
    val progress by animateFloatAsState(
        targetValue = (currentScenarioIndex.toFloat() / scenarioList.size.toFloat()),
        animationSpec = tween(durationMillis = 800),
        label = "progressAnim"
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        // --- هدر نوار پیشرفت ---
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text("سطح نفوذ در بازار", color = SoftWhite.copy(0.7f), fontSize = 11.sp)
                Text("مرحله عملیاتی ${currentScenarioIndex + 1}", color = GoldClassic, fontSize = 13.sp, fontWeight = FontWeight.Bold)
            }
            Text("${(progress * 100).toInt()}%", color = GoldClassic, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }

        // --- نوار پیشرفت بصری ---
        Box(modifier = Modifier.fillMaxWidth().height(6.dp).background(GoldClassic.copy(0.1f), CircleShape)) {
            Box(modifier = Modifier.fillMaxWidth(progress).fillMaxHeight().background(
                brush = androidx.compose.ui.graphics.Brush.horizontalGradient(listOf(GoldClassic.copy(0.7f), GoldClassic)),
                shape = CircleShape
            ))
        }

        Spacer(modifier = Modifier.height(20.dp))

        // --- بدنه اصلی سناریو ---
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = CardBg,
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.1f))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    "سناریوی ${currentScenarioIndex + 1} از ${scenarioList.size}",
                    color = GoldClassic.copy(0.6f),
                    fontSize = 11.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (screenState == "asking") {
                    Text(currentScenario.question, color = SoftWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp, lineHeight = 22.sp)
                    Spacer(modifier = Modifier.height(24.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        MatrixButton(currentScenario.optionA, Modifier.fillMaxWidth(), false) {
                            userSelection = 1
                            screenState = "result"
                        }
                        MatrixButton(currentScenario.optionB, Modifier.fillMaxWidth(), false) {
                            userSelection = 2
                            screenState = "result"
                        }
                    }
                } else {
                    val isCorrect = userSelection == currentScenario.correctOption

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Error,
                            contentDescription = null,
                            tint = if (isCorrect) Color.Green else Color(0xFFFF5252),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (isCorrect) "تحلیل موفق" else "خطای استراتژیک",
                            color = if (isCorrect) Color.Green else Color(0xFFFF5252),
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = if (isCorrect) currentScenario.feedbackRight else currentScenario.feedbackWrong,
                        color = SoftWhite.copy(0.8f),
                        fontSize = 14.sp,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    MatrixButton(
                        text = if (currentScenarioIndex < scenarioList.size - 1) "مأموریت بعدی" else "اتمام دوره",
                        modifier = Modifier.fillMaxWidth(),
                        isPrimary = true
                    ) {
                        scope.launch {
                            val nextIndex = currentScenarioIndex + 1
                            storage.saveScenarioIndex(nextIndex)
                            currentScenarioIndex = nextIndex
                            screenState = "asking"
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, icon: ImageVector) {
    Row(
        modifier = Modifier.padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, null, tint = GoldClassic, modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Text(title, color = GoldClassic, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
fun MatrixButton(text: String, modifier: Modifier, isPrimary: Boolean, onClick: () -> Unit) {
    Box(
        modifier = modifier
            .height(50.dp)
            .background(if (isPrimary) GoldClassic else Color.Transparent, RoundedCornerShape(12.dp))
            .border(1.dp, if (isPrimary) Color.Transparent else GoldClassic.copy(0.5f), RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = if (isPrimary) DeepNavy else GoldClassic, fontWeight = FontWeight.Bold, fontSize = 13.sp)
    }
}