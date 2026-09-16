package com.example.ui.home

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.ConversionSentence
import com.example.data.model.ConversionTopic
import com.example.data.model.DailyConversionDatabase
import com.example.ui.theme.*
import java.util.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DailyConversionScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val sharedPrefs = remember { context.getSharedPreferences("daily_conversion_prefs", Context.MODE_PRIVATE) }

    var selectedTopic by remember { mutableStateOf<ConversionTopic?>(null) }
    var currentSentenceIndex by remember { mutableStateOf(0) }
    
    // Topic progress map (topicId -> currentSentenceIndex/completedCount)
    var progressMap by remember {
        mutableStateOf(
            DailyConversionDatabase.topics.associate { topic ->
                topic.id to sharedPrefs.getInt("topic_${topic.id}_progress", 0)
            }
        )
    }

    // TTS state
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    var ttsInitialized by remember { mutableStateOf(false) }

    DisposableEffect(context) {
        var speech: TextToSpeech? = null
        speech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.ERROR) {
                ttsInitialized = true
                try {
                    speech?.language = Locale.US
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        tts = speech
        onDispose {
            try {
                speech?.stop()
                speech?.shutdown()
            } catch (_: Exception) {}
        }
    }

    fun speak(text: String, isUrdu: Boolean = false) {
        if (text.isBlank()) return
        if (tts == null) {
            tts = TextToSpeech(context) { status ->
                if (status != TextToSpeech.ERROR) {
                    ttsInitialized = true
                    speak(text, isUrdu)
                }
            }
            return
        }
        val locale = if (isUrdu) Locale("ur", "PK") else Locale.US
        val res = try {
            tts?.setLanguage(locale)
        } catch (e: Exception) {
            TextToSpeech.LANG_NOT_SUPPORTED
        }
        if (res == TextToSpeech.LANG_MISSING_DATA || res == TextToSpeech.LANG_NOT_SUPPORTED) {
            try {
                if (isUrdu) {
                    val urRes = tts?.setLanguage(Locale("ur"))
                    if (urRes == TextToSpeech.LANG_MISSING_DATA || urRes == TextToSpeech.LANG_NOT_SUPPORTED) {
                        tts?.language = Locale.US
                    }
                } else {
                    tts?.language = Locale.US
                }
            } catch (e: Exception) {
                tts?.language = Locale.US
            }
        }
        try {
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "daily_conv_tts_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    if (selectedTopic == null) {
        // Render Topic Selection Screen
        TopicSelectionView(
            topics = DailyConversionDatabase.topics,
            progressMap = progressMap,
            onTopicSelected = { topic ->
                selectedTopic = topic
                // Start from the user's last saved progress index (clamped safety)
                val savedProgress = progressMap[topic.id] ?: 0
                currentSentenceIndex = savedProgress.coerceIn(0, (topic.sentences.size - 1).coerceAtLeast(0))
            },
            onBackClick = onBackClick
        )
    } else {
        val topic = selectedTopic!!
        val sentence = topic.sentences[currentSentenceIndex]

        PracticeDashboardView(
            topic = topic,
            sentence = sentence,
            currentIndex = currentSentenceIndex,
            totalCount = topic.sentences.size,
            onSpeakText = { text, isUr -> speak(text, isUr) },
            onNextClick = {
                if (currentSentenceIndex < 49) {
                    currentSentenceIndex++
                    // Save progress if higher
                    val currentProgress = progressMap[topic.id] ?: 0
                    if (currentSentenceIndex > currentProgress) {
                        sharedPrefs.edit().putInt("topic_${topic.id}_progress", currentSentenceIndex).apply()
                        progressMap = progressMap + (topic.id to currentSentenceIndex)
                    }
                } else {
                    Toast.makeText(context, "Congratulations! You completed this topic!", Toast.LENGTH_LONG).show()
                }
            },
            onPrevClick = {
                if (currentSentenceIndex > 0) {
                    currentSentenceIndex--
                }
            },
            onCloseClick = {
                selectedTopic = null
            }
        )
    }
}

@Composable
fun TopicSelectionView(
    topics: List<ConversionTopic>,
    progressMap: Map<Int, Int>,
    onTopicSelected: (ConversionTopic) -> Unit,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("daily_conversion_screen")
    ) {
        // Header
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.testTag("back_button")
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Go Back",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "Daily Conversion",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "Master 1000 English-Urdu Conversational Sentences",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Grid of Topics
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(topics) { topic ->
                val progress = progressMap[topic.id] ?: 0
                val maxCount = topic.sentences.size
                val percent = (((progress + 1).toFloat() / maxCount.toFloat()) * 100f).coerceIn(0f, 100f).toInt()
                
                Card(
                    onClick = { onTopicSelected(topic) },
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("topic_card_${topic.id}")
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Themed icon with gradient-like background
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(44.dp)
                                .background(
                                    color = IndigoPrimary.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Icon(
                                imageVector = mapIconNameToVector(topic.iconName),
                                contentDescription = topic.name,
                                tint = IndigoPrimary,
                                modifier = Modifier.size(24.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = topic.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1
                        )

                        Text(
                            text = topic.urduName,
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = IndigoPrimary,
                            modifier = Modifier.padding(top = 2.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Progress Row
                        Row(
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            LinearProgressIndicator(
                                progress = { ((progress + 1).toFloat() / maxCount.toFloat()).coerceIn(0f, 1f) },
                                color = IndigoPrimary,
                                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                modifier = Modifier
                                    .weight(1f)
                                    .height(6.dp)
                                    .clip(RoundedCornerShape(3.dp))
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "$percent%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Text(
                            text = "${(progress + 1).coerceAtMost(maxCount)}/$maxCount Learned",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PracticeDashboardView(
    topic: ConversionTopic,
    sentence: ConversionSentence,
    currentIndex: Int,
    totalCount: Int,
    onSpeakText: (String, Boolean) -> Unit,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onCloseClick: () -> Unit
) {
    val context = LocalContext.current
    var hideTranslation by remember { mutableStateOf(false) }
    var hideRomanUrdu by remember { mutableStateOf(false) }

    // Audio recording state
    var isRecording by remember { mutableStateOf(false) }
    var spokenText by remember { mutableStateOf("") }
    var evaluationResult by remember { mutableStateOf<String?>(null) }
    var isMatchResult by remember { mutableStateOf(false) }

    fun processSpokenText(spoken: String) {
        spokenText = spoken
        val target = sentence.english.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").trim()
        val cleanSpoken = spoken.lowercase().replace(Regex("[^a-zA-Z0-9 ]"), "").trim()
        val similarity = calculateStringSimilarity(cleanSpoken, target)
        isMatchResult = similarity > 0.65 || cleanSpoken.contains(target) || target.contains(cleanSpoken)
        evaluationResult = if (isMatchResult) {
            "🎯 Brilliant! Perfect pronunciation match!"
        } else {
            "👍 Good try! Let's polish and try again."
        }
    }

    // System activity fallback launcher
    val speechIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isRecording = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spoken = matches?.firstOrNull()
            if (!spoken.isNullOrBlank()) {
                processSpokenText(spoken)
            } else {
                evaluationResult = "No speech detected. Tap mic to try again."
            }
        }
    }

    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }

    DisposableEffect(Unit) {
        onDispose {
            try {
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
            speechRecognizer = null
        }
    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the English sentence...")
            }
            try {
                if (SpeechRecognizer.isRecognitionAvailable(context)) {
                    if (speechRecognizer == null) {
                        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                    }
                    speechRecognizer?.startListening(intent)
                } else {
                    speechIntentLauncher.launch(intent)
                }
            } catch (e: Exception) {
                try {
                    speechIntentLauncher.launch(intent)
                } catch (ex: Exception) {
                    Toast.makeText(context, "Voice recognition unavailable", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Toast.makeText(context, "Microphone permission is required to practice speaking.", Toast.LENGTH_SHORT).show()
        }
    }

    fun startListening() {
        spokenText = ""
        evaluationResult = null
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak the English sentence...")
        }

        val permissionCheck = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (SpeechRecognizer.isRecognitionAvailable(context)) {
                try {
                    if (speechRecognizer == null) {
                        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                            setRecognitionListener(object : RecognitionListener {
                                override fun onReadyForSpeech(params: Bundle?) {
                                    isRecording = true
                                    spokenText = "Listening..."
                                    evaluationResult = null
                                }
                                override fun onBeginningOfSpeech() {}
                                override fun onRmsChanged(rmsdB: Float) {}
                                override fun onBufferReceived(buffer: ByteArray?) {}
                                override fun onEndOfSpeech() {
                                    isRecording = false
                                }
                                override fun onError(error: Int) {
                                    isRecording = false
                                    if (error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_SERVER || error == SpeechRecognizer.ERROR_CLIENT) {
                                        try {
                                            speechIntentLauncher.launch(intent)
                                            return
                                        } catch (_: Exception) {}
                                    }
                                    evaluationResult = "Unable to hear clearly. Please tap the button to try again."
                                    isMatchResult = false
                                }
                                override fun onResults(results: Bundle?) {
                                    isRecording = false
                                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                                    val spoken = matches?.firstOrNull()
                                    if (!spoken.isNullOrBlank()) {
                                        processSpokenText(spoken)
                                    }
                                }
                                override fun onPartialResults(partialResults: Bundle?) {}
                                override fun onEvent(eventType: Int, params: Bundle?) {}
                            })
                        }
                    }
                    isRecording = true
                    speechRecognizer?.startListening(intent)
                } catch (e: Exception) {
                    try {
                        speechIntentLauncher.launch(intent)
                    } catch (_: Exception) {
                        evaluationResult = "Unable to start voice input."
                    }
                }
            } else {
                try {
                    speechIntentLauncher.launch(intent)
                } catch (_: Exception) {
                    evaluationResult = "Voice recognition service unavailable on device."
                }
            }
        } else {
            requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (_: Exception) {}
        isRecording = false
    }

    // Auto-read English when a new card loads
    LaunchedEffect(sentence.id) {
        onSpeakText(sentence.english, false)
        spokenText = ""
        evaluationResult = null
        isRecording = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("practice_dashboard")
    ) {
        // Practice Top Bar
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onCloseClick) {
                    Icon(Icons.Default.Close, contentDescription = "Close practice", tint = MaterialTheme.colorScheme.onBackground)
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = topic.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = topic.urduName,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = IndigoPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = IndigoPrimary.copy(alpha = 0.15f),
                modifier = Modifier.padding(2.dp)
            ) {
                Text(
                    text = "${currentIndex + 1} / $totalCount",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = IndigoPrimary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }
        }

        // Progress Bar
        LinearProgressIndicator(
            progress = { (currentIndex + 1).toFloat() / totalCount.toFloat() },
            color = IndigoPrimary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(RoundedCornerShape(3.dp))
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Learning Flashcard
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // English Display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                        ) {
                            Text(
                                text = "English",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        IconButton(onClick = { onSpeakText(sentence.english, false) }) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Play English Audio", tint = IndigoPrimary)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = sentence.english,
                        style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }

                // Divider
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Roman Urdu Display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!hideRomanUrdu) {
                        Text(
                            text = sentence.romanUrdu,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                            color = SunsetAmber,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        Text(
                            text = "(Pronunciation Help)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    } else {
                        Text(
                            text = "Roman Urdu Hidden",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                // Divider
                HorizontalDivider(
                    color = MaterialTheme.colorScheme.outline.copy(alpha = 0.15f),
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                // Urdu Display
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer,
                        ) {
                            Text(
                                text = "Urdu",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }

                        IconButton(onClick = { onSpeakText(sentence.urdu, true) }) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Play Urdu Audio", tint = ElectricViolet)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    if (!hideTranslation) {
                        Text(
                            text = sentence.urdu,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                    } else {
                        Text(
                            text = "Urdu Translation Hidden",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                        )
                    }
                }

                // Toggle visibility controls inside card bottom
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    TextButton(
                        onClick = { hideTranslation = !hideTranslation }
                    ) {
                        Icon(
                            imageVector = if (hideTranslation) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hideTranslation) "Show Translation" else "Hide Translation",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    TextButton(
                        onClick = { hideRomanUrdu = !hideRomanUrdu }
                    ) {
                        Icon(
                            imageVector = if (hideRomanUrdu) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (hideRomanUrdu) "Show Pronunciation" else "Hide Pronunciation",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Speaking Practice Section
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🎤 Practice Your Pronunciation",
                    style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Record / Stop speaking practice
                    Button(
                        onClick = {
                            if (isRecording) stopListening() else startListening()
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isRecording) Color(0xFFDC2626) else IndigoPrimary
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Icon(
                            imageVector = if (isRecording) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Speak now"
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = if (isRecording) "Stop" else "Tap & Say it!")
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Demo voice simulation button to assist testing
                    FilledTonalButton(
                        onClick = {
                            spokenText = sentence.english
                            evaluationResult = "🎯 Brilliant! Perfect pronunciation match!"
                            isMatchResult = true
                        },
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("Demo Pronounce", style = MaterialTheme.typography.labelMedium)
                    }
                }

                if (spokenText.isNotEmpty() || evaluationResult != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        if (spokenText.isNotEmpty()) {
                            Text(
                                text = "You said: \"$spokenText\"",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                        if (evaluationResult != null) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = evaluationResult!!,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = if (isMatchResult) Color(0xFF16A34A) else Color(0xFFDC2626)
                                ),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Navigation Footer (Previous, Next)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            FilledTonalButton(
                onClick = onPrevClick,
                enabled = currentIndex > 0,
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.weight(1f).testTag("prev_button")
            ) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Previous")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = onNextClick,
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                modifier = Modifier.weight(1f).testTag("next_button")
            ) {
                Text(text = if (currentIndex < totalCount - 1) "Next" else "Finish")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(Icons.Default.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
            }
        }
    }
}

// Map the string name to the vector icon
private fun mapIconNameToVector(name: String): ImageVector {
    return when (name) {
        "chat" -> Icons.Default.Chat
        "people" -> Icons.Default.People
        "restaurant" -> Icons.Default.Restaurant
        "shopping_bag" -> Icons.Default.ShoppingBag
        "work" -> Icons.Default.Work
        "flight" -> Icons.Default.Flight
        "healing" -> Icons.Default.Healing
        "school" -> Icons.Default.School
        "wb_sunny" -> Icons.Default.WbSunny
        "sports_cricket" -> Icons.Default.Sports
        "phone" -> Icons.Default.Phone
        "schedule" -> Icons.Default.Schedule
        "payments" -> Icons.Default.Payments
        "home" -> Icons.Default.Home
        "landscape" -> Icons.Default.Landscape
        "today" -> Icons.Default.Today
        "psychology" -> Icons.Default.Psychology
        "warning" -> Icons.Default.Warning
        "favorite" -> Icons.Default.Favorite
        "info" -> Icons.Default.Info
        "emoji_emotions" -> Icons.Default.Face
        "explore" -> Icons.Default.Explore
        "checkroom" -> Icons.Default.Person
        "phone_android" -> Icons.Default.Phone
        "help" -> Icons.Default.Help
        "whatshot" -> Icons.Default.Whatshot
        else -> Icons.Default.AutoAwesome
    }
}

// Calculate similarity ratio between two strings
private fun calculateStringSimilarity(s1: String, s2: String): Double {
    val words1 = s1.split(" ").filter { it.isNotBlank() }.toSet()
    val words2 = s2.split(" ").filter { it.isNotBlank() }.toSet()
    if (words1.isEmpty() || words2.isEmpty()) return 0.0
    val intersection = words1.intersect(words2).size
    val union = words1.union(words2).size
    return intersection.toDouble() / union.toDouble()
}
