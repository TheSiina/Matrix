package com.example.matrix

data class Job(
    val id: String,
    val title: String,
    val category: String,
    val iconName: String,
    val introVoiceUrl: String = "",
    val income: String,
    val about: String,
    val forbiddenTruth: String,
    val stats: JobStats,
    val deadlyMistakes: List<String>,
    val compatibilityQuiz: List<QuizQuestion>,
    val successStories: List<SuccessStory>,
    val gameSteps: List<RoadmapStep>
)

// کلاس برای متریک‌های عددی و وضعیت‌ها
data class JobStats(
    val stressLevel: Int, // از ۱۰ (مثلا ۸)
    val jobSecurity: String, // "بالا"، "متوسط"، "پایین"
    val migrationScore: Int, // از ۱۰ (قابلیت مهاجرت)
    val aiThreat: String, // "کم"، "متوسط"، "زیاد" (تهدید هوش مصنوعی)
    val minCapital: String, // سرمایه اولیه (مثلا: "یک لپ‌تاپ معمولی")
    val learningCurve: String // منحنی یادگیری (مثلا: "۶ ماه فشرده")
)

// کلاس برای سوالات تست هوشمند
data class QuizQuestion(
    val question: String,
    val weight: Int = 1,
    val feedbackIfYes: String = "عالیه!"
)

// کلاس برای داستان‌های موفقیت
data class SuccessStory(
    val name: String,
    val role: String, // مثلا: فریلنسر بین‌المللی
    val storyTitle: String, // مثلا: "از کارگری تا کدنویسی"
    val fullNarrative: String
)

data class RoadmapStep(
    val level: String,
    val title: String,
    val description: String,
    val reward: String
)