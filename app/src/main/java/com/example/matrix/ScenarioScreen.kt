package com.example.matrix

import android.content.Context
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.google.gson.reflect.TypeToken
// کدای بخش تمرین سناریو در بخش تکنیک های مذاکره
fun loadScenariosFromJson(context: Context, lessonId: String): List<LessonScenario> {
    return try {
        val jsonString = context.assets.open("negotiation_data.json").bufferedReader().use { it.readText() }

        // ۱. اصلاح حیاتی: فایل با { شروع می‌شود پس JsonObject است
        val rootObject = JsonParser.parseString(jsonString).asJsonObject

        // ۲. ورود به لیست اصلی دروس
        val lessonsArray = rootObject.getAsJsonArray("negotiation_items")

        var foundScenarios = emptyList<LessonScenario>()

        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            // ۳. پیدا کردن درس مورد نظر
            if (lessonObject.get("lesson_id").asString == lessonId) {
                // ۴. چک کردن اینکه آیا بخش سناریوها (scenarios) در این درس وجود دارد
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
        Log.e("MATRIX_ERROR", "Scenario Load Failed: ${e.message}")
        emptyList()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScenarioScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current

    // لود کردن دیتا
    val scenarios = remember(lessonId) {
        loadScenariosFromJson(context, lessonId)
    }

    // دیالوگ موفقیت
    var showSuccessDialog by remember { mutableStateOf(false) }
    var currentStep by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableStateOf<Int?>(null) }
    var showAnalysis by remember { mutableStateOf(false) }

    if (scenarios.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("محتوایی برای ID: $lessonId یافت نشد", color = Color.White)
        }
        return
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { },
            containerColor = CardBg,
            title = { Text("مأموریت انجام شد", color = GoldClassic, fontWeight = FontWeight.Bold) },
            text = { Text("شما با موفقیت تمام سناریوها را تحلیل کردید.", color = SoftWhite) },
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
                title = { Text("تمرین سناریو (${currentStep + 1}/${scenarios.size})", color = SoftWhite, fontSize = 16.sp) },
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
                    text = if (!showAnalysis) "بررسی پاسخ" else if (currentStep < scenarios.size - 1) "سوال بعدی" else "اتمام تمرین",
                    color = DeepNavy,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class LessonScenario(
    val id: Int,
    val question: String,
    val options: List<String>,
    val correctIndex: Int,
    val analysis: String
)