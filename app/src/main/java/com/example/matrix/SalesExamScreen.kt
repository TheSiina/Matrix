package com.example.matrix

// آزمون جامع تکنیک‌های فروش (استفاده مجدد از ExamScreen و Question)

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.gson.JsonParser

fun loadSalesExamQuestions(context: Context): List<Question> {
    return try {
        val jsonString = context.assets.open("sales_techniques.json").bufferedReader().use { it.readText() }
        val rootObject = JsonParser.parseString(jsonString).asJsonObject
        val questionsArray = rootObject.getAsJsonArray("final_exam")
        val list = mutableListOf<Question>()

        questionsArray.forEachIndexed { index, element ->
            val obj = element.asJsonObject
            val optionsJson = obj.getAsJsonArray("options")
            val options = mutableListOf<String>()
            for (i in 0 until optionsJson.size()) {
                options.add(optionsJson[i].asString)
            }

            list.add(
                Question(
                    id = obj.get("id")?.asInt ?: index,
                    level = obj.get("level")?.asString ?: "بدون سطح",
                    scenario = obj.get("scenario")?.asString ?: "",
                    options = options,
                    correctIndex = obj.get("correctIndex")?.asInt ?: 0,
                    explanation = obj.get("explanation")?.asString ?: ""
                )
            )
        }
        list
    } catch (e: Exception) {
        e.printStackTrace()
        emptyList()
    }
}

@Composable
fun SalesExamScreen(
    levelName: String,
    navController: NavController
) {
    val context = LocalContext.current
    val allQuestions = remember { loadSalesExamQuestions(context) }

    val filtered = remember(levelName, allQuestions) {
        allQuestions.filter { it.level.trim() == levelName.trim() }
    }

    ExamScreen(
        questions = filtered,
        navController = navController,
        onScoreSaved = { score ->
            val prefs = context.getSharedPreferences("sales_user_data", Context.MODE_PRIVATE)
            val keySuffix = levelName.replace("سطح ", "").replace(" ", "_")
            prefs.edit().putInt("sales_score_$keySuffix", score).apply()
        }
    )
}

