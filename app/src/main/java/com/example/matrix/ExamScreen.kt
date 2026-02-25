package com.example.matrix

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExamScreen(
    questions: List<Question>,
    navController: NavController,
    onScoreSaved: (Int) -> Unit
) {
    var currentIndex by remember { mutableIntStateOf(0) }
    var selectedOption by remember { mutableIntStateOf(-1) }
    var showExplanation by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }

    if (questions.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("سوالات این آزمون هنوز بارگذاری نشده است.", color = Color.White)
        }
        return
    }

    val scrollState = rememberScrollState()
    val currentQuestion = questions.getOrNull(currentIndex)

    Scaffold(
        containerColor = DeepNavy,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("ارزیابی استراتژیک", color = GoldClassic, fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = GoldClassic)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = DeepNavy)
            )
        }
    ) { padding ->
        if (currentQuestion == null) {
            val score = if (questions.isNotEmpty()) (correctAnswersCount * 100) / questions.size else 0

            LaunchedEffect(Unit) {
                onScoreSaved(score)
            }

            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(Icons.Default.EmojiEvents, null, Modifier.size(120.dp), tint = GoldClassic)
                    Spacer(Modifier.height(16.dp))
                    Text("آزمون به پایان رسید", color = SoftWhite, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                    Text("امتیاز نهایی شما: $score از ۱۰۰", color = GoldClassic, fontSize = 18.sp, modifier = Modifier.padding(16.dp))

                    Button(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth(0.7f).height(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldClassic),
                        shape = RoundedCornerShape(15.dp)
                    ) {
                        Text("بازگشت به آکادمی", color = DeepNavy, fontWeight = FontWeight.Bold)
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp)
            ) {
                // ۱. بخش محتوای سوال (قابل اسکرول)
                Column(
                    modifier = Modifier
                        .weight(1f) // این بخش فضای میانی را پر می‌کند
                        .verticalScroll(scrollState)
                ) {
                    Spacer(modifier = Modifier.height(16.dp))

                    LinearProgressIndicator(
                        progress = { (currentIndex + 1).toFloat() / questions.size },
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                        color = GoldClassic,
                        trackColor = CardBg
                    )

                    Text(
                        "سوال ${currentIndex + 1} از ${questions.size}",
                        color = GrayText,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 8.dp).align(Alignment.CenterHorizontally)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = CardBg),
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, GoldClassic.copy(alpha = 0.2f))
                    ) {
                        Text(
                            text = currentQuestion.scenario,
                            modifier = Modifier.padding(20.dp),
                            color = SoftWhite,
                            fontSize = 18.sp,
                            lineHeight = 28.sp,
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    currentQuestion.options.forEachIndexed { index, option ->
                        val isSelected = selectedOption == index
                        // تعیین رنگ‌ها بر اساس وضعیت پاسخ‌دهی
                        val containerColor = when {
                            showExplanation && index == currentQuestion.correctIndex -> Color(0xFF2E7D32).copy(alpha = 0.2f)
                            showExplanation && isSelected && index != currentQuestion.correctIndex -> Color(0xFFC62828).copy(alpha = 0.2f)
                            isSelected -> GoldClassic.copy(alpha = 0.15f)
                            else -> CardBg
                        }

                        val borderColor = when {
                            showExplanation && index == currentQuestion.correctIndex -> Color.Green
                            showExplanation && isSelected && index != currentQuestion.correctIndex -> Color.Red
                            isSelected -> GoldClassic
                            else -> Color.Transparent
                        }

                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp)
                                .clickable(enabled = !showExplanation) { selectedOption = index },
                            shape = RoundedCornerShape(16.dp),
                            color = containerColor,
                            border = BorderStroke(1.dp, borderColor)
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(10.dp)
                                        .background(if (isSelected) GoldClassic else GrayText.copy(alpha = 0.4f), CircleShape)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(text = option, color = SoftWhite, fontSize = 16.sp)
                            }
                        }
                    }

                    if (showExplanation) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = currentQuestion.explanation,
                            color = GoldClassic,
                            fontSize = 14.sp,
                            lineHeight = 22.sp,
                            modifier = Modifier.fillMaxWidth().background(GoldClassic.copy(alpha = 0.05f), RoundedCornerShape(8.dp)).padding(12.dp),
                            textAlign = TextAlign.Right
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp)) // فاصله برای انتهای اسکرول
                }

                // ۲. بخش دکمه ثابت در پایین
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp)
                ) {
                    Button(
                        onClick = {
                            if (!showExplanation) {
                                if (selectedOption == currentQuestion.correctIndex) {
                                    correctAnswersCount++
                                }
                                showExplanation = true
                            } else {
                                currentIndex++
                                selectedOption = -1
                                showExplanation = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        enabled = selectedOption != -1,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GoldClassic,
                            disabledContainerColor = GrayText.copy(alpha = 0.5f)
                        )
                    ) {
                        Text(
                            text = if (!showExplanation) "بررسی پاسخ" else "سوال بعدی",
                            color = DeepNavy,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}