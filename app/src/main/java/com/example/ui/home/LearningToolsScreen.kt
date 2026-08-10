package com.example.ui.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.GrammarRule
import com.example.data.model.LearningToolsDatabase
import com.example.ui.theme.*
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearningToolsScreen(
    initialRuleId: Int? = null,
    onBackClick: () -> Unit = {}
) {
    val context = LocalContext.current
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }
    
    // Initialize TTS safely
    DisposableEffect(context) {
        val ttsInstance = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.ENGLISH
            }
        }
        tts = ttsInstance
        onDispose {
            ttsInstance.stop()
            ttsInstance.shutdown()
        }
    }

    fun speakText(text: String) {
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
    }

    // Tab Selection state (0 = Study Hub, 1 = Revision & Stats)
    var selectedTab by remember { mutableIntStateOf(0) }

    // Bookmarks state (persisted via SharedPreferences)
    val sharedPrefs = remember { context.getSharedPreferences("english_learning_prefs", Context.MODE_PRIVATE) }
    var bookmarkedIds by remember {
        mutableStateOf(
            sharedPrefs.getStringSet("bookmarks", emptySet())?.mapNotNull { it.toIntOrNull() }?.toSet() ?: emptySet()
        )
    }

    // Load quiz scores out of 10 for each topic
    var quizScores by remember {
        mutableStateOf(
            LearningToolsDatabase.rules.associate { rule ->
                rule.id to sharedPrefs.getInt("quiz_score_${rule.id}", -1)
            }
        )
    }

    // Load recorded weak areas: "ruleId||topicTitle||questionText||explanation"
    var weakAreas by remember {
        mutableStateOf(
            sharedPrefs.getStringSet("weak_areas", emptySet()) ?: emptySet()
        )
    }

    fun toggleBookmark(id: Int) {
        val newSet = if (bookmarkedIds.contains(id)) bookmarkedIds - id else bookmarkedIds + id
        bookmarkedIds = newSet
        sharedPrefs.edit().putStringSet("bookmarks", newSet.map { it.toString() }.toSet()).apply()
    }

    // State Variables
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    var hideUrduTranslation by remember { mutableStateOf(false) }
    
    // Dialog details
    var activeDetailRule by remember { mutableStateOf<GrammarRule?>(null) }
    var activeQuizRule by remember { mutableStateOf<GrammarRule?>(null) }
    var activePronunciationText by remember { mutableStateOf<String?>(null) }

    // Auto-open requested rule on launch (Daily Challenge)
    LaunchedEffect(initialRuleId) {
        initialRuleId?.let { id ->
            val rule = LearningToolsDatabase.rules.find { it.id == id }
            if (rule != null) {
                activeDetailRule = rule
            }
        }
    }

    // Filter and Search Logic
    val filteredRules = remember(searchQuery, selectedCategory) {
        LearningToolsDatabase.rules.filter { rule ->
            val matchesCategory = selectedCategory == "All" || rule.category.equals(selectedCategory, ignoreCase = true)
            val matchesSearch = searchQuery.isBlank() || 
                rule.title.contains(searchQuery, ignoreCase = true) ||
                rule.titleUrdu.contains(searchQuery, ignoreCase = true) ||
                rule.explanation.contains(searchQuery, ignoreCase = true) ||
                rule.explanationUrdu.contains(searchQuery, ignoreCase = true) ||
                rule.examples.any { ex ->
                    ex.english.contains(searchQuery, ignoreCase = true) ||
                    ex.urdu.contains(searchQuery, ignoreCase = true)
                } ||
                rule.quiz.any { q ->
                    q.question.contains(searchQuery, ignoreCase = true) ||
                    q.options.any { opt -> opt.contains(searchQuery, ignoreCase = true) }
                }
            
            matchesCategory && matchesSearch
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "English Learning Tools",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("learning_tools_back_btn")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            // Top Navigation Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = SunsetAmber,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = SunsetAmber
                    )
                },
                modifier = Modifier.padding(bottom = 12.dp)
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("Study Hub", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.School, contentDescription = "Study Hub") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Revision & Stats ⭐", fontWeight = FontWeight.Bold) },
                    icon = { Icon(Icons.Default.Star, contentDescription = "Revision") }
                )
            }

            if (selectedTab == 0) {
                // TAB 0: STUDY HUB
                // Welcome Header Card
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.25f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(SunsetAmber, ElectricViolet)),
                                    shape = RoundedCornerShape(14.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.School,
                                contentDescription = "School Icon",
                                tint = Color.White,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(14.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "Grammar & Verbs Hub",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                // Speaker icon on title
                                IconButton(
                                    onClick = { speakText("Welcome to Grammar and Verbs Hub. Learn and practice 20 crucial rules offline.") },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Speak intro",
                                        tint = SunsetAmber,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Text(
                                text = "20 Topics featuring rules, 5 examples each, and practice quizzes with 10 interactive MCQs.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search any word, rule or example...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Default.Clear, contentDescription = "Clear search")
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                        .testTag("rule_search_input"),
                    shape = RoundedCornerShape(16.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SunsetAmber,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                    ),
                    singleLine = true
                )

                // Category Chips & Assessment Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        listOf("All", "Modals", "Tenses", "Grammar").forEach { category ->
                            val isSelected = selectedCategory == category
                            Surface(
                                modifier = Modifier
                                    .clickable { selectedCategory = category }
                                    .testTag("category_chip_$category"),
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) SunsetAmber else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                            ) {
                                Text(
                                    text = category,
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Self Assessment toggle
                    Surface(
                        modifier = Modifier
                            .clickable { hideUrduTranslation = !hideUrduTranslation }
                            .testTag("hide_urdu_assessment_btn"),
                        shape = RoundedCornerShape(10.dp),
                        color = if (hideUrduTranslation) ElectricViolet.copy(alpha = 0.12f) else Color.Transparent,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (hideUrduTranslation) ElectricViolet else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (hideUrduTranslation) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle Urdu",
                                tint = if (hideUrduTranslation) ElectricViolet else MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Hide Urdu",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (hideUrduTranslation) ElectricViolet else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // 20 Rules List in 2-Column Grid
                if (filteredRules.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.SearchOff,
                                contentDescription = "No rules found",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.size(44.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "No learning topics found.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("rules_grid_list")
                    ) {
                        items(filteredRules) { rule ->
                            val isBookmarked = bookmarkedIds.contains(rule.id)
                            CompactTopicGridCard(
                                rule = rule,
                                isBookmarked = isBookmarked,
                                hideUrdu = hideUrduTranslation,
                                onCardClick = { activeDetailRule = rule },
                                onBookmarkToggle = { toggleBookmark(rule.id) },
                                onQuizClick = { activeQuizRule = rule },
                                onSpeak = { speakText(it) }
                            )
                        }
                    }
                }
            } else {
                // TAB 1: REVISION & PERFORMANCE STATS
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Bookmarked / Saved Revision list section
                    Text(
                        text = "⭐ Bookmarked Topics for Revision",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SunsetAmber,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                    
                    val bookmarkedRules = LearningToolsDatabase.rules.filter { bookmarkedIds.contains(it.id) }
                    
                    if (bookmarkedRules.isEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f))
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Icon(
                                    imageVector = Icons.Default.StarOutline,
                                    contentDescription = "No bookmarks",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                                    modifier = Modifier.size(36.dp)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your Revision list is empty!",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    textAlign = TextAlign.Center
                                )
                                Text(
                                    text = "Tap the ⭐ icon on any Grammar topic card in the Study Hub to save it here for fast revision and active practice.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                                )
                            }
                        }
                    } else {
                        // Display Saved Bookmarks
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier
                                .heightIn(max = 380.dp)
                                .padding(bottom = 16.dp)
                        ) {
                            items(bookmarkedRules) { rule ->
                                CompactTopicGridCard(
                                    rule = rule,
                                    isBookmarked = true,
                                    hideUrdu = hideUrduTranslation,
                                    onCardClick = { activeDetailRule = rule },
                                    onBookmarkToggle = { toggleBookmark(rule.id) },
                                    onQuizClick = { activeQuizRule = rule },
                                    onSpeak = { speakText(it) }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Quiz Performance / Progress Report Card
                    Text(
                        text = "📈 Quiz Practice Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = ElectricViolet,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            val attemptedQuizzes = quizScores.filter { it.value >= 0 }
                            val totalScore = attemptedQuizzes.values.sum()
                            val avgScore = if (attemptedQuizzes.isNotEmpty()) totalScore.toFloat() / attemptedQuizzes.size else 0f
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "Topics Cleared: ${attemptedQuizzes.size}/20",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Average score: ${String.format("%.1f", avgScore)}/10",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .background(ElectricViolet.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.TrendingUp, contentDescription = "Stats", tint = ElectricViolet)
                                }
                            }
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            // Visual linear bar
                            LinearProgressIndicator(
                                progress = { attemptedQuizzes.size / 20f },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(8.dp)
                                    .clip(RoundedCornerShape(4.dp)),
                                color = ElectricViolet,
                                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
                            )
                        }
                    }

                    // Weak Areas / Error Log Section
                    Text(
                        text = "🧠 Recorded Weak Areas (Incorrect Answers)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(0xFFEF4444),
                        modifier = Modifier.padding(bottom = 8.dp)
                    )

                    if (weakAreas.isEmpty()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 24.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.08f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF10B981).copy(alpha = 0.2f))
                        ) {
                            Row(
                                modifier = Modifier.padding(14.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Clean errors", tint = Color(0xFF10B981))
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "No weak areas recorded! 🎉",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFF10B981)
                                    )
                                    Text(
                                        text = "Any incorrect answers in practice quizzes will be saved here automatically for active review and pronunciation.",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    } else {
                        // Clear all button
                        TextButton(
                            onClick = {
                                weakAreas = emptySet()
                                sharedPrefs.edit().remove("weak_areas").apply()
                            },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text("Clear All Reviews", color = Color(0xFFEF4444), style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                        }

                        // List of weak areas
                        weakAreas.forEach { record ->
                            val parts = record.split("||")
                            if (parts.size >= 4) {
                                val ruleId = parts[0].toIntOrNull() ?: 1
                                val topicTitle = parts[1]
                                val questionText = parts[2]
                                val explanationText = parts[3]

                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEF4444).copy(alpha = 0.15f)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(vertical = 4.dp)
                                ) {
                                    Column(modifier = Modifier.padding(12.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Surface(
                                                color = ElectricViolet.copy(alpha = 0.12f),
                                                shape = RoundedCornerShape(4.dp)
                                            ) {
                                                Text(
                                                    text = topicTitle,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                                    color = ElectricViolet,
                                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                                )
                                            }

                                            // Dismiss single review
                                            IconButton(
                                                onClick = {
                                                    val newSet = weakAreas - record
                                                    weakAreas = newSet
                                                    sharedPrefs.edit().putStringSet("weak_areas", newSet).apply()
                                                },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.Close, contentDescription = "Dismiss", tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(14.dp))
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(6.dp))

                                        Text(
                                            text = "Q: $questionText",
                                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                                            color = MaterialTheme.colorScheme.onSurface
                                        )

                                        Spacer(modifier = Modifier.height(4.dp))

                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Icon(Icons.Default.Info, contentDescription = "Concept", tint = Color(0xFFEF4444), modifier = Modifier.size(12.dp))
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = explanationText,
                                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                modifier = Modifier.weight(1f)
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(8.dp))

                                        // Action buttons on weak area card
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            // Speak question audio
                                            OutlinedButton(
                                                onClick = { speakText(questionText) },
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp),
                                                colors = ButtonDefaults.outlinedButtonColors(contentColor = SunsetAmber),
                                                border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.5f))
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.VolumeUp, contentDescription = "Speak Q", modifier = Modifier.size(12.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Speak Q", style = MaterialTheme.typography.labelSmall)
                                                }
                                            }

                                            // Practice correct sentence speaking with Mic Pronunciation % score
                                            Button(
                                                onClick = { activePronunciationText = questionText },
                                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                                                modifier = Modifier.height(28.dp),
                                                colors = ButtonDefaults.buttonColors(containerColor = ElectricViolet, contentColor = Color.White)
                                            ) {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Mic, contentDescription = "Speak Answer", modifier = Modifier.size(12.dp))
                                                    Spacer(modifier = Modifier.width(4.dp))
                                                    Text("Practice speaking", style = MaterialTheme.typography.labelSmall)
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Detail Dialog / BottomSheet
    activeDetailRule?.let { rule ->
        GrammarRuleDetailDialog(
            rule = rule,
            hideUrdu = hideUrduTranslation,
            onDismiss = { activeDetailRule = null },
            onSpeak = { speakText(it) },
            onMicClick = { activePronunciationText = it },
            onStartQuiz = {
                activeQuizRule = rule
                activeDetailRule = null
            }
        )
    }

    // Quiz Dialog
    activeQuizRule?.let { rule ->
        GrammarQuizDialog(
            rule = rule,
            onDismiss = { activeQuizRule = null },
            onSpeak = { speakText(it) },
            onQuizFinished = { finalScore ->
                val newScores = quizScores.toMutableMap()
                newScores[rule.id] = finalScore
                quizScores = newScores
                sharedPrefs.edit().putInt("quiz_score_${rule.id}", finalScore).apply()

                // Trigger Daily Challenge validation if score is high
                val todayKey = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
                val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
                val challengeRuleId = LearningToolsDatabase.rules[dayOfYear % LearningToolsDatabase.rules.size].id
                
                if (rule.id == challengeRuleId && finalScore >= 8) {
                    val challengePrefs = context.getSharedPreferences("daily_challenge_prefs", Context.MODE_PRIVATE)
                    if (!challengePrefs.getBoolean("challenge_completed_$todayKey", false)) {
                        val currentStreak = challengePrefs.getInt("challenge_streak", 0)
                        challengePrefs.edit()
                            .putBoolean("challenge_completed_$todayKey", true)
                            .putInt("challenge_streak", currentStreak + 1)
                            .apply()
                    }
                }
            },
            onIncorrectAnswer = { question, explanation ->
                val cleanQuestion = question.replace("||", " ")
                val cleanExpl = explanation.replace("||", " ")
                val record = "${rule.id}||${rule.title}||$cleanQuestion||$cleanExpl"
                val newSet = weakAreas + record
                weakAreas = newSet
                sharedPrefs.edit().putStringSet("weak_areas", newSet).apply()
            }
        )
    }

    // Pronunciation Coach Mic Check Dialog
    activePronunciationText?.let { targetText ->
        PronunciationCoachDialog(
            targetText = targetText,
            onDismiss = { activePronunciationText = null },
            onSpeak = { speakText(it) }
        )
    }
}

@Composable
fun CompactTopicGridCard(
    rule: GrammarRule,
    isBookmarked: Boolean,
    hideUrdu: Boolean,
    onCardClick: () -> Unit,
    onBookmarkToggle: () -> Unit,
    onQuizClick: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val accentColor = when (rule.category) {
        "Modals" -> ElectricViolet
        "Tenses" -> SunsetAmber
        else -> IndigoPrimary
    }

    Card(
        onClick = onCardClick,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        border = androidx.compose.foundation.BorderStroke(
            width = 1.dp,
            color = MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
        ),
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .testTag("topic_card_${rule.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Upper row: Icon and Bookmark and Speaker
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                // Topic Icon
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(
                            color = accentColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconByString(rule.iconName),
                        contentDescription = "Topic Icon",
                        tint = accentColor,
                        modifier = Modifier.size(18.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Speaker Button
                    IconButton(
                        onClick = { onSpeak("${rule.title}. ${rule.explanation}") },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak Topic",
                            tint = SunsetAmber,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.width(4.dp))

                    // Bookmark Icon Button
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .background(
                                color = if (isBookmarked) SunsetAmber.copy(alpha = 0.1f) else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { onBookmarkToggle() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = "Bookmark",
                            tint = if (isBookmarked) SunsetAmber else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            // Center: Title & Rule
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = rule.title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold, fontSize = 13.sp),
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                if (!hideUrdu) {
                    Text(
                        text = rule.titleUrdu,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = rule.explanation,
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Bottom Row: Quiz Button
            Button(
                onClick = onQuizClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .testTag("quiz_btn_${rule.id}"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = accentColor.copy(alpha = 0.12f),
                    contentColor = accentColor
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(0.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Quiz Icon",
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Practice Quiz",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GrammarRuleDetailDialog(
    rule: GrammarRule,
    hideUrdu: Boolean,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    onMicClick: (String) -> Unit,
    onStartQuiz: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Header with Close
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = SunsetAmber.copy(alpha = 0.12f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = rule.id.toString(),
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = SunsetAmber
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = rule.title,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f, fill = false)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                // Speak title button
                                IconButton(
                                    onClick = { onSpeak(rule.title) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Speak Title", tint = SunsetAmber, modifier = Modifier.size(15.dp))
                                }
                            }
                            if (!hideUrdu) {
                                Text(
                                    text = rule.titleUrdu,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.testTag("detail_close_btn")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close detail")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(androidx.compose.foundation.rememberScrollState())
                ) {
                    // English Explanation Section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Rule & Explanation",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = SunsetAmber
                        )
                        IconButton(
                            onClick = { onSpeak(rule.explanation) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Speak Explanation", tint = SunsetAmber, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = rule.explanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // Urdu Explanation
                    if (!hideUrdu) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = rule.explanationUrdu,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                textAlign = TextAlign.Right
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Formula Bar Section
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Sentence Pattern / Formula",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = ElectricViolet
                        )
                        IconButton(
                            onClick = { onSpeak(rule.formula) },
                            modifier = Modifier.size(28.dp)
                        ) {
                            Icon(Icons.Default.VolumeUp, contentDescription = "Speak Formula", tint = ElectricViolet, modifier = Modifier.size(16.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = ElectricViolet.copy(alpha = 0.08f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.2f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = rule.formula,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = ElectricViolet,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Examples Section (5 examples)
                    Text(
                        text = "5 Study Examples & Pronunciation Check",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    rule.examples.forEachIndexed { index, ex ->
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${index + 1}. ${ex.english}",
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    if (!hideUrdu) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = ex.urdu,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            textAlign = TextAlign.Right,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    // Speaker Button
                                    IconButton(
                                        onClick = { onSpeak(ex.english) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                color = SunsetAmber.copy(alpha = 0.1f),
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.VolumeUp,
                                            contentDescription = "Speak Example",
                                            tint = SunsetAmber,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    // Microphone Pronunciation Practice Button
                                    IconButton(
                                        onClick = { onMicClick(ex.english) },
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(
                                                color = ElectricViolet.copy(alpha = 0.1f),
                                                shape = CircleShape
                                            )
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = "Practice speaking",
                                            tint = ElectricViolet,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Practice Quiz Button at bottom of details
                Button(
                    onClick = onStartQuiz,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("detail_dialog_quiz_btn"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SunsetAmber
                    ),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Psychology,
                        contentDescription = "Quiz"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Take 10-MCQ Practice Quiz",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                }
            }
        }
    }
}

@Composable
fun GrammarQuizDialog(
    rule: GrammarRule,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit,
    onQuizFinished: (Int) -> Unit,
    onIncorrectAnswer: (question: String, explanation: String) -> Unit
) {
    val quizList = rule.quiz
    var currentQuestionIndex by remember { mutableIntStateOf(0) }
    var selectedOptionIndex by remember { mutableStateOf<Int?>(null) }
    var hasAnswered by remember { mutableStateOf(false) }
    var correctAnswersCount by remember { mutableIntStateOf(0) }
    var showScoreCard by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Header with current progress
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${rule.title} - Quiz",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        if (!showScoreCard) {
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of 10",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Quiz")
                    }
                }

                if (showScoreCard) {
                    // Final Score UI
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .background(
                                    brush = Brush.linearGradient(listOf(SunsetAmber, ElectricViolet)),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "$correctAnswersCount/10",
                                    style = MaterialTheme.typography.headlineLarge.copy(
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = "Score",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        val percentage = (correctAnswersCount * 10)
                        val motivationMsg = when {
                            percentage >= 90 -> "Superb mastery! You're an expert!"
                            percentage >= 70 -> "Great job! Very strong skills!"
                            percentage >= 50 -> "Good work, keep on practicing!"
                            else -> "Don't give up! Study the examples and try again!"
                        }

                        Text(
                            text = if (percentage >= 50) "Congratulations!" else "Keep Learning!",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = if (percentage >= 50) SunsetAmber else MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = motivationMsg,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 24.dp)
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Quiz Summary stats
                        Row(
                            modifier = Modifier.fillMaxWidth(0.8f),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Correct", tint = Color(0xFF4CAF50))
                                Text(text = "$correctAnswersCount Correct", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Icon(Icons.Default.Cancel, contentDescription = "Incorrect", tint = Color(0xFFE57373))
                                Text(text = "${10 - correctAnswersCount} Incorrect", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }

                    // Score Card Action Buttons
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                // Restart Quiz
                                currentQuestionIndex = 0
                                selectedOptionIndex = null
                                hasAnswered = false
                                correctAnswersCount = 0
                                showScoreCard = false
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = SunsetAmber)
                        ) {
                            Text("Retry Quiz", fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                onQuizFinished(correctAnswersCount)
                                onDismiss()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SunsetAmber)
                        ) {
                            Text("Finish", fontWeight = FontWeight.Bold)
                        }
                    }
                } else {
                    // Active MCQ UI
                    val currentQuestion = quizList[currentQuestionIndex]

                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(vertical = 12.dp)
                    ) {
                        // Linear Progress Indicator
                        LinearProgressIndicator(
                            progress = { (currentQuestionIndex + 1) / 10f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = SunsetAmber,
                            trackColor = MaterialTheme.colorScheme.surfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // Question card
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            ),
                            shape = RoundedCornerShape(16.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.1f))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = currentQuestion.question,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.weight(1f)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                // Speaker icon on Question text
                                IconButton(
                                    onClick = { onSpeak(currentQuestion.question) },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Speak Question", tint = SunsetAmber)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Option Items
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            currentQuestion.options.forEachIndexed { optIdx, optionText ->
                                val isSelected = selectedOptionIndex == optIdx
                                val isCorrect = currentQuestion.correctIndex == optIdx

                                val backgroundColor = when {
                                    hasAnswered && isCorrect -> Color(0xFFE8F5E9) // soft green
                                    hasAnswered && isSelected && !isCorrect -> Color(0xFFFFEBEE) // soft red
                                    isSelected -> SunsetAmber.copy(alpha = 0.15f)
                                    else -> MaterialTheme.colorScheme.surface
                                }

                                val borderColor = when {
                                    hasAnswered && isCorrect -> Color(0xFF4CAF50)
                                    hasAnswered && isSelected && !isCorrect -> Color(0xFFF44336)
                                    isSelected -> SunsetAmber
                                    else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                                }

                                val textColor = when {
                                    hasAnswered && isCorrect -> Color(0xFF2E7D32)
                                    hasAnswered && isSelected && !isCorrect -> Color(0xFFC62828)
                                    isSelected -> SunsetAmber
                                    else -> MaterialTheme.colorScheme.onSurface
                                }

                                Surface(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable(enabled = !hasAnswered) {
                                            selectedOptionIndex = optIdx
                                        }
                                        .testTag("option_$optIdx"),
                                    shape = RoundedCornerShape(12.dp),
                                    color = backgroundColor,
                                    border = androidx.compose.foundation.BorderStroke(1.dp, borderColor)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(14.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(
                                                text = optionText,
                                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                                color = textColor,
                                                modifier = Modifier.weight(1f, fill = false)
                                            )
                                            Spacer(modifier = Modifier.width(6.dp))
                                            // Option speaker icon
                                            IconButton(
                                                onClick = { onSpeak(optionText) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(Icons.Default.VolumeUp, contentDescription = "Speak option", tint = SunsetAmber, modifier = Modifier.size(14.dp))
                                            }
                                        }

                                        if (hasAnswered) {
                                            if (isCorrect) {
                                                Icon(
                                                    imageVector = Icons.Default.CheckCircle,
                                                    contentDescription = "Correct",
                                                    tint = Color(0xFF4CAF50)
                                                )
                                            } else if (isSelected) {
                                                Icon(
                                                    imageVector = Icons.Default.Cancel,
                                                    contentDescription = "Incorrect",
                                                    tint = Color(0xFFF44336)
                                                )
                                            }
                                        } else {
                                            Box(
                                                modifier = Modifier
                                                    .size(20.dp)
                                                    .border(
                                                        width = 1.5.dp,
                                                        color = if (isSelected) SunsetAmber else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f),
                                                        shape = CircleShape
                                                    )
                                                    .background(
                                                        color = if (isSelected) SunsetAmber else Color.Transparent,
                                                        shape = CircleShape
                                                    )
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Answer Explanation Overlay
                        AnimatedVisibility(
                            visible = hasAnswered,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            Surface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                shape = RoundedCornerShape(12.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.Top
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Explanation",
                                        tint = ElectricViolet,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Explanation",
                                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                            color = ElectricViolet
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = currentQuestion.explanation,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Button controls at bottom
                    if (!hasAnswered) {
                        Button(
                            onClick = {
                                if (selectedOptionIndex != null) {
                                    hasAnswered = true
                                    val isCorrect = selectedOptionIndex == currentQuestion.correctIndex
                                    if (isCorrect) {
                                        correctAnswersCount++
                                    } else {
                                        // Record incorrect answer for revision / weak areas
                                        onIncorrectAnswer(currentQuestion.question, currentQuestion.explanation)
                                    }
                                }
                            },
                            enabled = selectedOptionIndex != null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("check_answer_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SunsetAmber,
                                disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        ) {
                            Text("Check Answer", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    } else {
                        Button(
                            onClick = {
                                if (currentQuestionIndex < 9) {
                                    currentQuestionIndex++
                                    selectedOptionIndex = null
                                    hasAnswered = false
                                } else {
                                    showScoreCard = true
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("quiz_next_btn"),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SunsetAmber
                            )
                        ) {
                            Text(
                                text = if (currentQuestionIndex < 9) "Next Question" else "See Results",
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PronunciationCoachDialog(
    targetText: String,
    onDismiss: () -> Unit,
    onSpeak: (String) -> Unit
) {
    val context = LocalContext.current
    var isListening by remember { mutableStateOf(false) }
    var spokenText by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(-1) }
    
    // Check permission
    var hasPermission by remember {
        mutableStateOf(
            androidx.core.content.ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.RECORD_AUDIO
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        )
    }
    
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasPermission = isGranted
    }
    
    // Speech recognition setup
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    val speechIntent = remember {
        Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
        }
    }
    
    val listener = remember {
        object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isListening = false
            }
            override fun onError(error: Int) {
                isListening = false
            }
            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val text = matches[0]
                    spokenText = text
                    score = calculatePronunciationScore(targetText, text)
                }
            }
            override fun onPartialResults(partialResults: Bundle?) {}
            override fun onEvent(eventType: Int, params: Bundle?) {}
        }
    }
    
    DisposableEffect(Unit) {
        onDispose {
            speechRecognizer?.destroy()
        }
    }
    
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Pronunciation Coach 🎤",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = SunsetAmber
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close Coach")
                    }
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    text = "Target Sentence:",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                
                if (score >= 0) {
                    val annotatedTargetText = buildAnnotatedString {
                        val targetWordsList = targetText.split(" ")
                        val cleanSpoken = spokenText.lowercase().replace(Regex("[^a-zA-Z0-9\\s]"), " ").split("\\s+".toRegex()).toSet()
                        
                        targetWordsList.forEachIndexed { idx, word ->
                            val cleanWord = word.lowercase().replace(Regex("[^a-zA-Z0-9]"), "")
                            val isCorrect = cleanSpoken.contains(cleanWord)
                            
                            withStyle(
                                style = SpanStyle(
                                    color = if (isCorrect) Color(0xFF10B981) else Color(0xFFEF4444),
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                            ) {
                                append(word)
                            }
                            if (idx < targetWordsList.size - 1) {
                                append(" ")
                            }
                        }
                    }
                    Text(
                        text = annotatedTargetText,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                } else {
                    Text(
                        text = targetText,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                // Play Target Speaker Icon
                IconButton(
                    onClick = { onSpeak(targetText) },
                    modifier = Modifier.background(SunsetAmber.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(Icons.Default.VolumeUp, contentDescription = "Hear Model", tint = SunsetAmber)
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                
                Spacer(modifier = Modifier.height(16.dp))
                
                if (score >= 0) {
                    Text(
                        text = "You said:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "\"$spokenText\"",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Text(
                        text = "Match Score: $score%",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = when {
                                score >= 85 -> Color(0xFF10B981)
                                score >= 60 -> SunsetAmber
                                else -> Color(0xFFEF4444)
                            }
                        )
                    )
                    
                    Text(
                        text = when {
                            score >= 85 -> "Excellent! Native pronunciation! 🌟"
                            score >= 60 -> "Good effort! Try pronouncing the red words again. 👍"
                            else -> "Keep practicing! Listen to the model above. 💪"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
                
                // Mic Button
                Box(contentAlignment = Alignment.Center) {
                    Button(
                        onClick = {
                            if (!hasPermission) {
                                permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                            } else {
                                if (isListening) {
                                    isListening = false
                                    speechRecognizer?.stopListening()
                                } else {
                                    spokenText = ""
                                    score = -1
                                    isListening = true
                                    try {
                                        if (speechRecognizer == null) {
                                            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                                                setRecognitionListener(listener)
                                            }
                                        }
                                        speechRecognizer?.startListening(speechIntent)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                        isListening = false
                                    }
                                }
                            }
                        },
                        shape = CircleShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isListening) Color(0xFFEF4444) else ElectricViolet
                        ),
                        modifier = Modifier.size(64.dp),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Icon(
                            imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = if (isListening) "Stop listening" else "Speak now",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    text = if (isListening) "Listening... Tap stop when done." else "Tap Mic to record, or use the simulator below!",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Spacer(modifier = Modifier.height(16.dp))
                
                // Auto-Evaluator Simulator
                Text(
                    text = "🤖 Tryout Simulator",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(top = 6.dp)
                ) {
                    listOf("Perfect (100%)", "Good (80%)", "Needs Practice (40%)").forEach { choice ->
                        Surface(
                            onClick = {
                                when (choice) {
                                    "Perfect (100%)" -> {
                                        spokenText = targetText
                                        score = 100
                                    }
                                    "Good (80%)" -> {
                                        val words = targetText.split(" ")
                                        spokenText = if (words.size > 1) {
                                            words.take(words.size - 1).joinToString(" ") + " incorrect"
                                        } else {
                                            "imperfect try"
                                        }
                                        score = 80
                                    }
                                    else -> {
                                        spokenText = "something completely different"
                                        score = 40
                                    }
                                }
                            },
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f))
                        ) {
                            Text(
                                text = choice.substringBefore(" "),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

fun calculatePronunciationScore(target: String, spoken: String): Int {
    if (target.isBlank() || spoken.isBlank()) return 0
    val targetWords = target.lowercase().replace(Regex("[^a-zA-Z0-9\\s]"), "").split("\\s+".toRegex()).filter { it.isNotBlank() }
    val spokenWords = spoken.lowercase().replace(Regex("[^a-zA-Z0-9\\s]"), "").split("\\s+".toRegex()).filter { it.isNotBlank() }
    if (targetWords.isEmpty()) return 0
    
    var matchCount = 0
    for (tWord in targetWords) {
        if (spokenWords.contains(tWord)) {
            matchCount++
        }
    }
    
    val ratio = matchCount.toFloat() / targetWords.size.toFloat()
    return (ratio * 100).toInt().coerceIn(0, 100)
}

// Icon mapper helper
fun getIconByString(iconName: String): androidx.compose.ui.graphics.vector.ImageVector {
    return when (iconName) {
        "whatshot" -> Icons.Default.Whatshot
        "checkroom" -> Icons.Default.Checkroom
        "explore" -> Icons.Default.Explore
        "phone_android" -> Icons.Default.PhoneAndroid
        "favorite" -> Icons.Default.Favorite
        "info" -> Icons.Default.Info
        "star" -> Icons.Default.Star
        "warning" -> Icons.Default.Warning
        "school" -> Icons.Default.School
        "emoji_emotions" -> Icons.Default.EmojiEmotions
        else -> Icons.Default.Book
    }
}
