package com.example.matrix

import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.json.JSONObject

data class Question(
    val id: Int = 0,
    val level: String,
    val scenario: String,
    val options: List<String>,
    val correctIndex: Int,
    val explanation: String
)

class NegotiationViewModel : ViewModel() {

    private val _negotiationItems = mutableStateOf<List<NegotiationItem>>(emptyList())
    val negotiationItems: State<List<NegotiationItem>> = _negotiationItems

    private val _finalExamQuestions = mutableStateOf<List<Question>>(emptyList())
    val finalExamQuestions: State<List<Question>> = _finalExamQuestions

    fun loadData(jsonString: String) {
        if (jsonString.isEmpty()) return

        try {
            val rootObject = JSONObject(jsonString)

            // ۱. بارگذاری لیست دروس
            val itemsList = mutableListOf<NegotiationItem>()
            if (rootObject.has("negotiation_items")) {
                val itemsArray = rootObject.getJSONArray("negotiation_items")
                for (i in 0 until itemsArray.length()) {
                    val obj = itemsArray.getJSONObject(i)
                    itemsList.add(
                        NegotiationItem.Lesson(
                            id = obj.optString("lesson_id", i.toString()),
                            title = obj.optString("lesson_title", "بدون عنوان"),
                            isFree = true
                        )
                    )
                }
            }
            // آیتم آزمون جامع در انتهای لیست
            itemsList.add(NegotiationItem.MidReview(id = 100, title = "آزمون جامع نهایی", progress = 0))
            _negotiationItems.value = itemsList

            // ۲. بارگذاری سوالات آزمون
            val questionsList = mutableListOf<Question>()
            if (rootObject.has("final_exam")) {
                val questionsArray = rootObject.getJSONArray("final_exam")
                for (i in 0 until questionsArray.length()) {
                    val q = questionsArray.getJSONObject(i)
                    val optArray = q.getJSONArray("options")
                    val options = mutableListOf<String>()
                    for (j in 0 until optArray.length()) {
                        options.add(optArray.getString(j))
                    }

                    questionsList.add(
                        Question(
                            id = q.optInt("id", i),
                            level = q.optString("level", "بدون سطح"),
                            scenario = q.optString("scenario", ""),
                            options = options,
                            correctIndex = q.optInt("correctIndex", 0),
                            explanation = q.optString("explanation", "")
                        )
                    )
                }
            }
            _finalExamQuestions.value = questionsList

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}