package com.example.matrix

import android.content.Context
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.google.gson.Gson
import com.google.gson.JsonParser
// کدای بخش برنامه عملی در بخش تکنیک های مذاکره
data class ActionPlan(
    val title: String? = null,
    val task: String? = null,
    val note: String? = null // اضافه کردن علامت سوال برای جلوگیری از ارور
)

// لودر اصلاح شده برای جلوگیری از کرش
fun loadActionPlanFromAsset(context: Context, lessonId: String): ActionPlan? {
    return try {
        val jsonString = context.assets.open("negotiation_data.json").bufferedReader().use { it.readText() }

        // ۱. اصلاح اصلی: خواندن به صورت Object
        val rootObject = JsonParser.parseString(jsonString).asJsonObject

        // ۲. وارد شدن به کلید اصلی دروس
        val lessonsArray = rootObject.getAsJsonArray("negotiation_items")

        var foundPlan: ActionPlan? = null
        for (element in lessonsArray) {
            val lessonObject = element.asJsonObject
            if (lessonObject.get("lesson_id").asString == lessonId) {
                if (lessonObject.has("action_plan")) {
                    val planObject = lessonObject.getAsJsonObject("action_plan")
                    foundPlan = Gson().fromJson(planObject, ActionPlan::class.java)
                }
                break
            }
        }
        foundPlan
    } catch (e: Exception) {
        Log.e("MATRIX_ERROR", "ActionPlan Load Failed: ${e.message}")
        null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionPlanScreen(lessonId: String, navController: NavController) {
    val context = LocalContext.current
    // استفاده از remember برای جلوگیری از تلاش مجدد و کرش در رکمپوز
    val actionPlan = remember(lessonId) { loadActionPlanFromAsset(context, lessonId) }

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("برنامه عملیاتی", color = SoftWhite, fontSize = 18.sp) },
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
            // نمایش وضعیت خطا به جای کرش
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("مأموریتی برای این درس تعریف نشده است.", color = GrayText)
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

                // باکس وظیفه
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
                    color = Color.White.copy(alpha = 0.05f), // رنگ بسیار ملایم
                    shape = RoundedCornerShape(16.dp),
                    border = BorderStroke(1.dp, GrayText.copy(alpha = 0.2f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.Lightbulb, // آیکون لامپ برای نکته
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
                    Text("پذیرفتن مأموریت", color = DeepNavy, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}