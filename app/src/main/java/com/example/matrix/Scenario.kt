package com.example.matrix
//سناریو های شبیه ساز تصمیم گیری
data class Scenario(
    val id: Int,
    val question: String,
    val optionA: String,
    val optionB: String,
    val correctOption: Int,
    val feedbackRight: String,
    val feedbackWrong: String
)