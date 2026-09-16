package com.example.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.PartnerCheckAnswerResult
import com.example.data.model.PartnerFinalReportResult
import com.example.data.model.PartnerQuestion
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.NeonPink
import com.example.ui.theme.SunsetAmber
import com.example.util.BillingManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LiveVoiceExamScreen(
    topic: String = "Daily Routine",
    viewModel: TranslatorViewModel = viewModel(),
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val questions by viewModel.partnerQuestions.collectAsState()
    val currentIndex by viewModel.currentQuestionIndex.collectAsState()
    val checkResult by viewModel.checkResult.collectAsState()
    val isListening by viewModel.isListening.collectAsState()
    val spokenText by viewModel.spokenText.collectAsState()
    val isLoadingQuestions by viewModel.isLoadingQuestions.collectAsState()
    val isEvaluating by viewModel.isEvaluating.collectAsState()
    val isGeneratingReport by viewModel.isGeneratingReport.collectAsState()
    val finalReport by viewModel.finalReport.collectAsState()

    var textInput by remember { mutableStateOf("") }
    var showManualInput by remember { mutableStateOf(false) }
    var showKeywordsHint by remember { mutableStateOf(false) }

    val currentQuestion: PartnerQuestion? = questions.getOrNull(currentIndex)

    // Load questions on initial enter
    LaunchedEffect(topic) {
        viewModel.loadQuestions(topic)
    }

    // Automatically speak question when it changes
    LaunchedEffect(currentIndex, questions) {
        currentQuestion?.let { q ->
            viewModel.speakText(q.questionEn)
        }
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.startListening(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "AI Conversation Partner",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Live Voice Exam Mode",
                            style = MaterialTheme.typography.labelSmall,
                            color = ElectricViolet
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("live_exam_back_btn")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.loadQuestions(topic) }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Restart")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        if (isLoadingQuestions) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    CircularProgressIndicator(color = ElectricViolet)
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "Preparing 10 conversation questions for \"$topic\"...",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else if (finalReport != null) {
            var showNameDialog by remember { mutableStateOf(false) }
            var showPaywall by remember { mutableStateOf(false) }
            var learnerName by remember { mutableStateOf("Learner") }

            // PAYWALL DIALOG
            if (showPaywall) {
                AlertDialog(
                    onDismissRequest = { showPaywall = false },
                    title = { Text("🔒 PRO Unlock Karo", fontWeight = FontWeight.Bold) },
                    text = {
                        Text(
                            "Aap ne free wala 1 certificate use kar liya hai.\n\nPRO Features:\n✅ Unlimited Certificates\n✅ Unlimited 50 Sentences\n✅ AI CV Maker\n✅ Offline Mode\n\nSirf $2.99 / Rs. 899 me",
                            lineHeight = 22.sp
                        )
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                // Yahan Google Play Billing ka code ayega
                                // Abhi ke liye test ke liye:
                                BillingManager.unlockPro(context)
                                showPaywall = false
                                Toast.makeText(context, "PRO Unlocked!", Toast.LENGTH_SHORT).show()
                                showNameDialog = true
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5))
                        ) {
                            Text("Buy PRO - Rs.899")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showPaywall = false }) {
                            Text("Baad me")
                        }
                    }
                )
            }

            if (showNameDialog) {
                AlertDialog(
                    onDismissRequest = { showNameDialog = false },
                    title = {
                        Text(
                            text = "📜 Certificate Name",
                            fontWeight = FontWeight.Bold
                        )
                    },
                    text = {
                        Column {
                            Text(
                                text = "Apna naam darj karein taake Certificate par print ho sake:",
                                fontSize = 14.sp
                            )
                            Spacer(Modifier.height(8.dp))
                            OutlinedTextField(
                                value = learnerName,
                                onValueChange = { learnerName = it },
                                label = { Text("Your Name") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                showNameDialog = false
                                try {
                                    val pdfFile = viewModel.repository.generateCertificatePdf(
                                        context = context,
                                        name = learnerName.ifBlank { "English Learner" },
                                        score = finalReport!!.total_score,
                                        topic = topic
                                    )
                                    BillingManager.incrementCertCount(context)
                                    val uri = FileProvider.getUriForFile(
                                        context,
                                        "${context.packageName}.fileprovider",
                                        pdfFile
                                    )
                                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                        type = "application/pdf"
                                        putExtra(Intent.EXTRA_STREAM, uri)
                                        putExtra(Intent.EXTRA_SUBJECT, "Live English Exam Certificate - $learnerName")
                                        putExtra(
                                            Intent.EXTRA_TEXT,
                                            "🎉 I just completed the Live Voice English Exam for '$topic' with a score of ${finalReport!!.total_score}/100 on Daily English Learning App!\n\nCheck out my completion certificate!"
                                        )
                                        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                                    }
                                    context.startActivity(Intent.createChooser(shareIntent, "Share Certificate PDF"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                    Toast.makeText(context, "Certificate PDF generated: ${e.message}", Toast.LENGTH_LONG).show()
                                }
                            }
                        ) {
                            Text("Generate & Share PDF")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showNameDialog = false }) {
                            Text("Cancel")
                        }
                    }
                )
            }

            // SHOW FINAL REPORT SCREEN
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                FinalReportScreen(
                    report = finalReport!!,
                    onShare = {
                        if (BillingManager.canGenerateCertificate(context)) {
                            // FREE - Generate PDF
                            showNameDialog = true
                        } else {
                            // LIMIT KHATAM - Show Paywall
                            showPaywall = true
                        }
                    }
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(Modifier.height(8.dp))

                // 1. TOPIC REPORT CARD
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("exam_topic_card"),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC7D2FE))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Surface(
                                    shape = CircleShape,
                                    color = ElectricViolet.copy(alpha = 0.15f),
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.RecordVoiceOver,
                                            contentDescription = null,
                                            tint = ElectricViolet,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                                Spacer(Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Topic: $topic",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 17.sp,
                                        color = Color(0xFF1E1B4B)
                                    )
                                    Text(
                                        text = "Live Interview Practice",
                                        fontSize = 12.sp,
                                        color = Color(0xFF4338CA)
                                    )
                                }
                            }

                            // Difficulty Badge
                            val diff = currentQuestion?.difficulty?.replaceFirstChar { it.uppercase() } ?: "Easy"
                            val diffColor = when (diff.lowercase()) {
                                "hard" -> Color(0xFFDC2626)
                                "medium" -> Color(0xFFD97706)
                                else -> Color(0xFF16A34A)
                            }
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = diffColor.copy(alpha = 0.12f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, diffColor.copy(alpha = 0.3f))
                            ) {
                                Text(
                                    text = diff,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = diffColor
                                )
                            }
                        }

                        Spacer(Modifier.height(12.dp))

                        // Progress Indicator
                        val total = questions.size.coerceAtLeast(1)
                        val progress = (currentIndex + 1).toFloat() / total.toFloat()
                        LinearProgressIndicator(
                            progress = { progress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = ElectricViolet,
                            trackColor = Color(0xFFE0E7FF)
                        )

                        Spacer(Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Question ${currentIndex + 1} / ${questions.size}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color(0xFF4B5563)
                            )
                            Text(
                                text = "${(progress * 100).toInt()}% Completed",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = ElectricViolet
                            )
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                // 2. SARA'S QUESTION CARD
                if (currentQuestion != null) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("sara_question_card"),
                        shape = RoundedCornerShape(18.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Column(modifier = Modifier.padding(18.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Surface(
                                        shape = CircleShape,
                                        color = NeonPink.copy(alpha = 0.2f),
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("👩‍🏫", fontSize = 14.sp)
                                        }
                                    }
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = "Sara (AI Partner)",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = NeonPink
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.speakText(currentQuestion.questionEn) },
                                    modifier = Modifier.size(36.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Listen to Question",
                                        tint = ElectricViolet,
                                        modifier = Modifier.size(22.dp)
                                    )
                                }
                            }

                            Spacer(Modifier.height(8.dp))

                            Text(
                                text = currentQuestion.questionEn,
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold,
                                    lineHeight = 28.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            if (currentQuestion.questionUr.isNotBlank()) {
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = currentQuestion.questionUr,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    lineHeight = 22.sp
                                )
                            }

                            // Expected Keywords Hint
                            if (currentQuestion.expectedKeywords.isNotEmpty()) {
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.clickable { showKeywordsHint = !showKeywordsHint },
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = SunsetAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    Text(
                                        text = if (showKeywordsHint) "Keywords: " + currentQuestion.expectedKeywords.joinToString(", ") else "Show Expected Keywords",
                                        fontSize = 12.sp,
                                        color = if (showKeywordsHint) SunsetAmber else MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(18.dp))

                // 3. USER VOICE INPUT / INTERACTION SECTION
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("user_answer_section"),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isListening) ElectricViolet else MaterialTheme.colorScheme.outlineVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (isListening) "🎙️ Listening... Speak your answer now!"
                            else if (spokenText.isNotBlank()) "Your Spoken Answer:"
                            else "Tap the microphone to speak your answer in English",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                            color = if (isListening) ElectricViolet else MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )

                        Spacer(Modifier.height(12.dp))

                        // Animated Mic Button
                        val infiniteTransition = rememberInfiniteTransition(label = "pulse")
                        val pulseScale by infiniteTransition.animateFloat(
                            initialValue = 1.0f,
                            targetValue = if (isListening) 1.18f else 1.0f,
                            animationSpec = infiniteRepeatable(
                                animation = tween(700, easing = FastOutSlowInEasing),
                                repeatMode = RepeatMode.Reverse
                            ),
                            label = "pulse_scale"
                        )

                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(88.dp)
                                .scale(if (isListening) pulseScale else 1.0f)
                        ) {
                            if (isListening) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(CircleShape)
                                        .background(ElectricViolet.copy(alpha = 0.2f))
                                )
                            }

                            FilledIconButton(
                                onClick = {
                                    if (isListening) {
                                        viewModel.stopListening()
                                    } else {
                                        val hasPermission = ContextCompat.checkSelfPermission(
                                            context,
                                            Manifest.permission.RECORD_AUDIO
                                        ) == PackageManager.PERMISSION_GRANTED

                                        if (hasPermission) {
                                            viewModel.startListening(context)
                                        } else {
                                            permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .size(64.dp)
                                    .testTag("live_exam_mic_button"),
                                colors = IconButtonDefaults.filledIconButtonColors(
                                    containerColor = if (isListening) NeonPink else ElectricViolet
                                )
                            ) {
                                Icon(
                                    imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                                    contentDescription = "Voice Input",
                                    tint = Color.White,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        // Spoken Text Display
                        if (spokenText.isNotBlank()) {
                            Spacer(Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "\"$spokenText\"",
                                    style = MaterialTheme.typography.bodyLarge.copy(fontStyle = FontStyle.Italic),
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Manual Text Option Toggle
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = { showManualInput = !showManualInput },
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(
                                text = if (showManualInput) "Hide text input" else "Or type your answer",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }

                        if (showManualInput) {
                            OutlinedTextField(
                                value = textInput,
                                onValueChange = {
                                    textInput = it
                                    viewModel.setSpokenText(it)
                                },
                                placeholder = { Text("Type English answer here...") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp),
                                shape = RoundedCornerShape(12.dp),
                                singleLine = false,
                                maxLines = 3
                            )
                        }

                        // Submit Button
                        if (spokenText.isNotBlank() && checkResult == null && !isEvaluating) {
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = { viewModel.evaluateAnswer() },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("submit_exam_answer_btn"),
                                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null)
                                Spacer(Modifier.width(8.dp))
                                Text("Check with Sara AI", fontWeight = FontWeight.Bold)
                            }
                        }

                        if (isEvaluating) {
                            Spacer(Modifier.height(8.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp,
                                    color = ElectricViolet
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(
                                    text = "Sara is checking your answer...",
                                    fontSize = 13.sp,
                                    color = ElectricViolet,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                // 4. SARA'S INSTANT FEEDBACK CARD
                if (checkResult != null) {
                    Spacer(Modifier.height(16.dp))
                    SaraFeedbackCard(
                        result = checkResult!!,
                        onSpeak = { text, isUrdu -> viewModel.speakText(text, isUrdu) },
                        onRetry = { viewModel.retryCurrentQuestion() },
                        onNext = { viewModel.nextQuestion() },
                        isLastQuestion = currentIndex >= questions.size - 1
                    )
                }

                if (isGeneratingReport) {
                    Spacer(Modifier.height(20.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFEEF2FF))
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = ElectricViolet)
                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Generating Final English Performance Report...",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1E1B4B)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(32.dp))
            }
        }
    }
}

@Composable
fun SaraFeedbackCard(
    result: PartnerCheckAnswerResult,
    onSpeak: (String, Boolean) -> Unit,
    onRetry: () -> Unit,
    onNext: () -> Unit,
    isLastQuestion: Boolean
) {
    val scoreColor = when {
        result.score >= 80 -> Color(0xFF16A34A)
        result.score >= 50 -> Color(0xFFD97706)
        else -> Color(0xFFDC2626)
    }

    val cardBg = when {
        result.score >= 80 -> Color(0xFFF0FDF4)
        result.score >= 50 -> Color(0xFFFFFBEB)
        else -> Color(0xFFFEF2F2)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("sara_feedback_card"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = androidx.compose.foundation.BorderStroke(1.dp, scoreColor.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header with Score
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = scoreColor.copy(alpha = 0.2f),
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (result.score >= 80) "🌟" else if (result.score >= 50) "👍" else "🎯",
                                fontSize = 18.sp
                            )
                        }
                    }
                    Spacer(Modifier.width(10.dp))
                    Column {
                        Text(
                            text = result.feedbackEn,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = scoreColor
                        )
                        if (result.feedbackUr.isNotBlank()) {
                            Text(
                                text = result.feedbackUr,
                                fontSize = 13.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = scoreColor,
                    modifier = Modifier.testTag("exam_score_badge")
                ) {
                    Text(
                        text = "${result.score}/100",
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = scoreColor.copy(alpha = 0.2f))
            Spacer(Modifier.height(14.dp))

            // Corrected Version
            Text(
                text = "✨ Perfect Spoken English:",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = result.userAnswerCorrectedEn,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = { onSpeak(result.userAnswerCorrectedEn, false) },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Speak Corrected",
                        tint = ElectricViolet,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            if (result.userAnswerUr.isNotBlank()) {
                Text(
                    text = result.userAnswerUr,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 20.sp
                )
            }

            // Teacher explanation/correction
            if (result.correctionEn.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Surface(
                    shape = RoundedCornerShape(10.dp),
                    color = SunsetAmber.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("💡", fontSize = 14.sp)
                            Spacer(Modifier.width(6.dp))
                            Text(
                                text = "Teacher Note:",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = Color(0xFFD97706)
                            )
                        }
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = result.correctionEn,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (result.correctionUr.isNotBlank()) {
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = result.correctionUr,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            // Better Native Version
            if (result.betterVersionEn.isNotBlank()) {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFEEF2FF))
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "🚀 Native / Advanced Way:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = ElectricViolet
                        )
                        Text(
                            text = result.betterVersionEn,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF1E1B4B)
                        )
                    }
                    IconButton(
                        onClick = { onSpeak(result.betterVersionEn, false) },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Listen",
                            tint = ElectricViolet,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onRetry,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("exam_retry_btn"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(Modifier.width(6.dp))
                    Text("Try Again")
                }

                Button(
                    onClick = onNext,
                    modifier = Modifier
                        .weight(1.3f)
                        .testTag("exam_next_btn"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet)
                ) {
                    Text(if (isLastQuestion) "Finish & View Report" else "Next Question")
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp).scale(-1f, 1f))
                }
            }
        }
    }
}

@Composable
fun FinalReportScreen(report: PartnerFinalReportResult, onShare: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text("🎉 Practice Complete!", fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(16.dp))
        // SCORE CIRCLE
        Card(
            Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF4F46E5)),
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("${report.total_score}/100", fontSize = 48.sp, color = Color.White, fontWeight = FontWeight.Bold)
                Text("Total Score", color = Color.White)
                Spacer(Modifier.height(12.dp))
                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(report.fluency, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Fluency", color = Color.White, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(report.grammar, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Grammar", color = Color.White, fontSize = 12.sp)
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(report.confidence, color = Color.White, fontWeight = FontWeight.Bold)
                        Text("Confidence", color = Color.White, fontSize = 12.sp)
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Text("Advice: ${report.final_advice_ur}", fontSize = 16.sp, lineHeight = 24.sp)
        Spacer(Modifier.height(8.dp))
        Text("Next Topic: ${report.next_topic_suggestion}", color = Color(0xFF4F46E5), fontWeight = FontWeight.Bold, fontSize = 15.sp)
        
        if (report.strengths.isNotEmpty() || report.weaknesses.isNotEmpty()) {
            Spacer(Modifier.height(16.dp))
            if (report.strengths.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF0FDF4),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF86EFAC)),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("💪 Strengths", fontWeight = FontWeight.Bold, color = Color(0xFF166534))
                        report.strengths.forEach { s ->
                            Text("• $s", fontSize = 13.sp, color = Color(0xFF166534))
                        }
                    }
                }
            }
            if (report.weaknesses.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFFBEB),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text("🎯 Areas to Improve", fontWeight = FontWeight.Bold, color = Color(0xFF92400E))
                        report.weaknesses.forEach { w ->
                            Text("• $w", fontSize = 13.sp, color = Color(0xFF92400E))
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))
        Button(
            onClick = onShare,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .testTag("share_certificate_btn"),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4F46E5)),
            shape = RoundedCornerShape(14.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Certificate Share Karo / PDF Banao", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        Spacer(Modifier.height(24.dp))
    }
}
