package com.example.ui.tutor

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun TenseDetailView(
    tense: EnglishTense,
    quizUserAnswers: Map<String, String>,
    quizSubmitted: Boolean,
    quizScore: Int,
    aiQuery: String,
    aiResponse: String?,
    isAskingAi: Boolean,
    onBackClick: () -> Unit,
    onSpeakText: (String) -> Unit,
    onSelectAnswer: (exerciseId: String, answer: String) -> Unit,
    onSubmitQuiz: () -> Unit,
    onResetQuiz: () -> Unit,
    onAiQueryChange: (String) -> Unit,
    onAskAi: () -> Unit
) {
    var activeTab by remember { mutableStateOf(0) }
    val tabTitles = listOf("Overview & Formulas", "Sentences & Mistakes", "Practice Quiz", "AI Tutor Support")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("tense_detail_screen")
    ) {
        // Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("back_to_tenses_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = ElectricViolet
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = tense.name,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = tense.urduName,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = ElectricViolet
                )
            }

            IconButton(
                onClick = { onSpeakText("${tense.name}. ${tense.easyEnglishExplanation}") },
                modifier = Modifier.testTag("speak_tense_explanation_button")
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Audio Explanation",
                    tint = ElectricViolet
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Scrollable Tab Row
        ScrollableTabRow(
            selectedTabIndex = activeTab,
            edgePadding = 0.dp,
            containerColor = Color.Transparent,
            contentColor = ElectricViolet,
            modifier = Modifier.fillMaxWidth()
        ) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = activeTab == index,
                    onClick = { activeTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (activeTab == index) FontWeight.Bold else FontWeight.Normal
                            )
                        )
                    },
                    modifier = Modifier.testTag("tense_tab_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (activeTab) {
            0 -> OverviewAndFormulasTab(tense = tense, onSpeakText = onSpeakText)
            1 -> SentencesAndMistakesTab(tense = tense, onSpeakText = onSpeakText)
            2 -> InteractiveQuizTab(
                tense = tense,
                quizUserAnswers = quizUserAnswers,
                quizSubmitted = quizSubmitted,
                quizScore = quizScore,
                onSelectAnswer = onSelectAnswer,
                onSubmitQuiz = onSubmitQuiz,
                onResetQuiz = onResetQuiz
            )
            3 -> AiTutorSupportTab(
                tense = tense,
                aiQuery = aiQuery,
                aiResponse = aiResponse,
                isAskingAi = isAskingAi,
                onAiQueryChange = onAiQueryChange,
                onAskAi = onAskAi
            )
        }
    }
}

@Composable
fun OverviewAndFormulasTab(
    tense: EnglishTense,
    onSpeakText: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // English Explanation Box
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ElectricViolet.copy(alpha = 0.1f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.MenuBook, contentDescription = null, tint = ElectricViolet)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Easy English Explanation",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tense.easyEnglishExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Urdu Explanation Box (اردو وضاحت)
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Translate, contentDescription = null, tint = SunsetAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "اردو وضاحت (Urdu Explanation)",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = tense.urduExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        // Formula & Structure Box
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Functions, contentDescription = null, tint = ElectricViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Sentence Structures / Formulas",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    FormulaItem(label = "Positive (+)", formulaText = tense.formula.positive, color = EmeraldGlow)
                    Spacer(modifier = Modifier.height(8.dp))
                    FormulaItem(label = "Negative (-)", formulaText = tense.formula.negative, color = SunsetAmber)
                    Spacer(modifier = Modifier.height(8.dp))
                    FormulaItem(label = "Question (?)", formulaText = tense.formula.question, color = ElectricViolet)
                }
            }
        }

        // Real Life Examples Section
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.RecordVoiceOver, contentDescription = null, tint = ElectricViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Real-Life Context Examples",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    tense.realLifeExamples.forEach { item ->
                        ExampleSentenceCard(sentence = item, onSpeakText = onSpeakText)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun FormulaItem(label: String, formulaText: String, color: Color) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = formulaText,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun SentencesAndMistakesTab(
    tense: EnglishTense,
    onSpeakText: (String) -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Positive Sentences
        item {
            SentenceSectionCard(
                title = "Positive Sentences (+)",
                titleColor = EmeraldGlow,
                sentences = tense.positiveSentences,
                onSpeakText = onSpeakText
            )
        }

        // Negative Sentences
        item {
            SentenceSectionCard(
                title = "Negative Sentences (-)",
                titleColor = SunsetAmber,
                sentences = tense.negativeSentences,
                onSpeakText = onSpeakText
            )
        }

        // Question Sentences
        item {
            SentenceSectionCard(
                title = "Question Sentences (?)",
                titleColor = ElectricViolet,
                sentences = tense.questionSentences,
                onSpeakText = onSpeakText
            )
        }

        // Common Mistakes Section
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ReportProblem, contentDescription = null, tint = SunsetAmber)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Common Mistakes & Corrections",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    tense.commonMistakes.forEach { mistake ->
                        MistakeCard(mistake = mistake)
                        Spacer(modifier = Modifier.height(10.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun SentenceSectionCard(
    title: String,
    titleColor: Color,
    sentences: List<SentenceExample>,
    onSpeakText: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = titleColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            sentences.forEach { sentence ->
                ExampleSentenceCard(sentence = sentence, onSpeakText = onSpeakText)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun ExampleSentenceCard(
    sentence: SentenceExample,
    onSpeakText: (String) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = sentence.english,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = sentence.urdu,
                    style = MaterialTheme.typography.bodySmall,
                    color = ElectricViolet
                )
            }

            IconButton(
                onClick = { onSpeakText(sentence.english) },
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                    contentDescription = "Speak",
                    tint = ElectricViolet,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
fun MistakeCard(mistake: CommonMistake) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.2f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Close, contentDescription = "Incorrect", tint = Color.Red, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = mistake.incorrect,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = Color.Red
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Check, contentDescription = "Correct", tint = EmeraldGlow, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = mistake.correct,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                    color = EmeraldGlow
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Explanation: ${mistake.explanationEn}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = mistake.explanationUrdu,
                style = MaterialTheme.typography.labelSmall,
                color = ElectricViolet
            )
        }
    }
}

@Composable
fun InteractiveQuizTab(
    tense: EnglishTense,
    quizUserAnswers: Map<String, String>,
    quizSubmitted: Boolean,
    quizScore: Int,
    onSelectAnswer: (String, String) -> Unit,
    onSubmitQuiz: () -> Unit,
    onResetQuiz: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        if (quizSubmitted) {
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = if (quizScore >= 70) EmeraldGlow.copy(alpha = 0.15f) else SunsetAmber.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (quizScore >= 70) EmeraldGlow else SunsetAmber
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Icon(
                            imageVector = if (quizScore >= 70) Icons.Default.EmojiEvents else Icons.Default.School,
                            contentDescription = null,
                            tint = if (quizScore >= 70) EmeraldGlow else SunsetAmber,
                            modifier = Modifier.size(40.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = if (quizScore >= 70) "Awesome Job! Lesson Mastered" else "Keep Practicing!",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Text(
                            text = "Score: $quizScore%",
                            style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (quizScore >= 70) EmeraldGlow else SunsetAmber
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = onResetQuiz,
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                        ) {
                            Text("Retake Practice Quiz")
                        }
                    }
                }
            }
        }

        items(tense.exercises, key = { it.id }) { exercise ->
            val userSelected = quizUserAnswers[exercise.id].orEmpty()

            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 2.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = ElectricViolet.copy(alpha = 0.12f)
                    ) {
                        Text(
                            text = exercise.type.label,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = ElectricViolet,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = exercise.question,
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (exercise.options.isNotEmpty()) {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            exercise.options.forEach { opt ->
                                val isSelected = userSelected.equals(opt, ignoreCase = true)
                                val isCorrect = opt.equals(exercise.correctAnswer, ignoreCase = true)

                                val chipBg = when {
                                    quizSubmitted && isCorrect -> EmeraldGlow.copy(alpha = 0.2f)
                                    quizSubmitted && isSelected && !isCorrect -> Color.Red.copy(alpha = 0.15f)
                                    isSelected -> ElectricViolet.copy(alpha = 0.2f)
                                    else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                                }

                                Surface(
                                    onClick = { onSelectAnswer(exercise.id, opt) },
                                    shape = RoundedCornerShape(12.dp),
                                    color = chipBg,
                                    border = androidx.compose.foundation.BorderStroke(
                                        1.dp,
                                        when {
                                            quizSubmitted && isCorrect -> EmeraldGlow
                                            quizSubmitted && isSelected && !isCorrect -> Color.Red
                                            isSelected -> ElectricViolet
                                            else -> Color.Transparent
                                        }
                                    ),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(12.dp)
                                    ) {
                                        RadioButton(
                                            selected = isSelected,
                                            onClick = { onSelectAnswer(exercise.id, opt) },
                                            enabled = !quizSubmitted,
                                            colors = RadioButtonDefaults.colors(selectedColor = ElectricViolet)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = opt,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (quizSubmitted) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Text(
                                    text = "Explanation: ${exercise.explanationEn}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = exercise.explanationUrdu,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = ElectricViolet
                                )
                            }
                        }
                    }
                }
            }
        }

        if (!quizSubmitted) {
            item {
                Button(
                    onClick = onSubmitQuiz,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_tense_quiz_button")
                ) {
                    Text(
                        text = "Submit Quiz & Verify Answers",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}

@Composable
fun AiTutorSupportTab(
    tense: EnglishTense,
    aiQuery: String,
    aiResponse: String?,
    isAskingAi: Boolean,
    onAiQueryChange: (String) -> Unit,
    onAskAi: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = ElectricViolet.copy(alpha = 0.12f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = ElectricViolet)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "AI Grammar Tutor for ${tense.name}",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Ask any grammar question, ask for sentence corrections, or request personalized study advice in English & Urdu!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = aiQuery,
                    onValueChange = onAiQueryChange,
                    placeholder = { Text("e.g. When should I use ${tense.name} vs Present Simple?") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(110.dp)
                        .testTag("ai_tense_query_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricViolet)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Button(
                    onClick = onAskAi,
                    enabled = aiQuery.isNotBlank() && !isAskingAi,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("ask_ai_tense_button")
                ) {
                    if (isAskingAi) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Analyzing with AI Tutor...")
                    } else {
                        Icon(Icons.Default.Send, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ask AI Tutor")
                    }
                }
            }
        }

        if (!aiResponse.isNullOrBlank()) {
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = ElectricViolet)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "AI Tutor Response",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = aiResponse,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
