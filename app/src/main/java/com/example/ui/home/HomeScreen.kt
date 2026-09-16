package com.example.ui.home

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import android.content.Context
import java.util.Calendar
import java.util.Date
import java.util.Locale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.example.R
import com.example.ui.components.AdBanner
import com.example.data.model.Language
import com.example.ui.home.components.*
import com.example.ui.navigation.BottomTab
import com.example.ui.theme.*

data class HomeFeatureCard(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val accentColor: Color,
    val onClick: () -> Unit
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel? = null,
    onNavigateToTab: (BottomTab) -> Unit = {},
    onNavigateToDailyConversion: () -> Unit = {},
    onNavigateToLearningTools: (Int?) -> Unit = {},
    onNavigateToAiConversationPartner: () -> Unit = {},
    onNavigateToMyProgress: () -> Unit = {},
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {}
) {
    if (LocalInspectionMode.current || viewModel == null) {
        HomeScreenPreviewContent(
            isDarkTheme = isDarkTheme,
            onToggleTheme = onToggleTheme,
            onNavigateToTab = onNavigateToTab
        )
        return
    }

    val sourceLang by viewModel.sourceLanguage.collectAsState()
    val targetLang by viewModel.targetLanguage.collectAsState()
    val inputText by viewModel.inputText.collectAsState()
    val translatedText by viewModel.translatedText.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val grammarNotes by viewModel.grammarNotes.collectAsState()
    val formality by viewModel.formality.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val activeModal by viewModel.activeModal.collectAsState()
    val voiceState by viewModel.voiceState.collectAsState()
    val voicePartialText by viewModel.voicePartialText.collectAsState()
    val voiceErrorMessage by viewModel.voiceErrorMessage.collectAsState()
    val dailyWord by viewModel.dailyWord.collectAsState()
    val dailyLearnContent by viewModel.dailyLearnContent.collectAsState()
    val practiceFeedback by viewModel.practiceFeedback.collectAsState()
    val grammarResult by viewModel.grammarCheckResult.collectAsState()
    val isOffline by viewModel.isOffline.collectAsState()
    val speechSpeed by viewModel.speechSpeed.collectAsState()
    var showProInfoDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val dailyChallengePrefs = remember { context.getSharedPreferences("daily_challenge_prefs", Context.MODE_PRIVATE) }
    var dailyChallengeStreak by remember {
        mutableStateOf(dailyChallengePrefs.getInt("challenge_streak", 0))
    }
    val topicPrefs = remember { context.getSharedPreferences("topic_partner_prefs", Context.MODE_PRIVATE) }
    val todayKey = remember {
        val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US)
        sdf.format(Date())
    }
    val yesterdayKey = remember {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -1)
        java.text.SimpleDateFormat("yyyy-MM-dd", Locale.US).format(cal.time)
    }
    var isChallengeCompleted by remember {
        mutableStateOf(dailyChallengePrefs.getBoolean("challenge_completed_$todayKey", false))
    }
    var topicStreak by remember { mutableStateOf(topicPrefs.getInt("topic_streak_count", 0)) }
    var isTopicGoalCompleted by remember {
        mutableStateOf(
            topicPrefs.getBoolean("daily_goal_topic_completed_$todayKey", false) ||
            topicPrefs.getString("last_completed_topic_date", "") == todayKey
        )
    }

    val challengeRule = remember {
        val dayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)
        val index = dayOfYear % com.example.data.model.LearningToolsDatabase.rules.size
        com.example.data.model.LearningToolsDatabase.rules[index]
    }

    // Dynamic sync when entering the home screen
    LaunchedEffect(Unit) {
        dailyChallengeStreak = dailyChallengePrefs.getInt("challenge_streak", 0)
        isChallengeCompleted = dailyChallengePrefs.getBoolean("challenge_completed_$todayKey", false)

        val storedStreak = topicPrefs.getInt("topic_streak_count", 0)
        val storedLastDate = topicPrefs.getString("topic_last_streak_date", "")

        val effectiveStreak = if (storedLastDate == todayKey || storedLastDate == yesterdayKey || storedStreak == 0) {
            storedStreak
        } else {
            0
        }
        if (effectiveStreak != storedStreak) {
            topicPrefs.edit().putInt("topic_streak_count", effectiveStreak).apply()
        }
        topicStreak = effectiveStreak
        isTopicGoalCompleted = topicPrefs.getBoolean("daily_goal_topic_completed_$todayKey", false) ||
                topicPrefs.getString("last_completed_topic_date", "") == todayKey
    }

    var showSourceLangSheet by remember { mutableStateOf(false) }
    var showTargetLangSheet by remember { mutableStateOf(false) }

    var swapRotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = swapRotationAngle,
        animationSpec = tween(durationMillis = 300),
        label = "swap_rotation"
    )

    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .testTag("home_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            // Header Top Bar
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Translator Pro",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                            color = MaterialTheme.colorScheme.onBackground
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = IndigoPrimary,
                            modifier = Modifier.padding(2.dp)
                        ) {
                            Text(
                                text = "AI",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                    Text(
                        text = "Next-Gen AI Language Studio",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    // VIP Pro Badge
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color(0xFFFFD700).copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.5f)),
                        modifier = Modifier
                            .testTag("vip_pro_badge_button")
                            .clickable { showProInfoDialog = true }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "👑 PRO",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.ExtraBold),
                                color = Color(0xFFFFD700)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SunsetAmber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .testTag("home_streak_pill")
                            .clickable { onNavigateToMyProgress() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "🔥 $topicStreak",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SunsetAmber,
                                modifier = Modifier.testTag("home_streak_text")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(6.dp))

                    // Dark/Light Theme Toggle
                    IconButton(
                        onClick = onToggleTheme,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDarkTheme) SunsetAmber else IndigoPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Main AI Translator Card
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("ai_translator_main_card")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {

                    // Language Selector Bar
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        // Source Language Button
                        Surface(
                            onClick = { showSourceLangSheet = true },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("source_lang_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(text = sourceLang.flagEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = sourceLang.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        // Swap Button
                        IconButton(
                            onClick = {
                                swapRotationAngle += 180f
                                viewModel.swapLanguages()
                            },
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .rotate(animatedRotation)
                                .testTag("swap_languages_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Swap Languages",
                                tint = IndigoPrimary
                            )
                        }

                        // Target Language Button
                        Surface(
                            onClick = { showTargetLangSheet = true },
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("target_lang_button")
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp)
                            ) {
                                Text(text = targetLang.flagEmoji, fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = targetLang.name,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    maxLines = 1,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Text Input Area
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.background,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .border(
                                width = 1.dp,
                                color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(14.dp)
                    ) {
                        Column {
                            OutlinedTextField(
                                value = inputText,
                                onValueChange = { viewModel.setInputText(it) },
                                placeholder = {
                                    Text(
                                        text = "Enter text, paste, or speak...",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                },
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color.Transparent,
                                    unfocusedBorderColor = Color.Transparent
                                ),
                                textStyle = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .heightIn(min = 100.dp, max = 160.dp)
                                    .testTag("translation_input_text")
                            )

                            // Quick Input Actions Row
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(
                                        onClick = { viewModel.openModal(ActiveModal.VOICE) },
                                        modifier = Modifier.testTag("mic_input_button")
                                    ) {
                                        Icon(Icons.Default.Mic, contentDescription = "Voice Input", tint = CyberCyan)
                                    }
                                    IconButton(
                                        onClick = { viewModel.openModal(ActiveModal.CAMERA) },
                                        modifier = Modifier.testTag("camera_input_button")
                                    ) {
                                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera Scan", tint = ElectricViolet)
                                    }
                                    IconButton(
                                        onClick = { viewModel.runGrammarCheck() },
                                        modifier = Modifier.testTag("quick_grammar_button")
                                    ) {
                                        Icon(Icons.Default.AutoAwesome, contentDescription = "Grammar Check", tint = EmeraldGlow)
                                    }
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (inputText.isNotEmpty()) {
                                        IconButton(
                                            onClick = { viewModel.setInputText("") },
                                            modifier = Modifier.testTag("clear_input_button")
                                        ) {
                                            Icon(Icons.Default.Clear, contentDescription = "Clear Input", tint = MaterialTheme.colorScheme.onSurfaceVariant)
                                        }
                                    }
                                    Text(
                                        text = "${inputText.length}/1000",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Formality Selector Chips
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Tone:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        listOf("Natural", "Professional", "Casual", "Academic").forEach { option ->
                            val isSelected = formality == option
                            FilterChip(
                                selected = isSelected,
                                onClick = { viewModel.setFormality(option) },
                                label = { Text(option, style = MaterialTheme.typography.labelSmall) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = IndigoPrimary,
                                    selectedLabelColor = Color.White
                                ),
                                modifier = Modifier.testTag("tone_chip_$option")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Translate Button
                    Button(
                        onClick = { viewModel.translate() },
                        enabled = inputText.isNotBlank() && !isLoading,
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = IndigoPrimary,
                            disabledContainerColor = IndigoPrimary.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp)
                            .testTag("translate_submit_button")
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                color = Color.White,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Translate with AI Pro",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            if (isOffline) {
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    color = Color(0xFFDC2626),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                            .testTag("offline_chip")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.WifiOff,
                                            contentDescription = "Offline Mode",
                                            tint = Color.White,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Offline",
                                            style = MaterialTheme.typography.labelSmall.copy(
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 10.sp
                                            ),
                                            color = Color.White
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Banner Ad directly below Translate Button
                    Spacer(modifier = Modifier.height(12.dp))
                    AdBanner(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("translate_button_ad_banner")
                    )

                    if (!errorMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    // Translated Output Box
                    if (translatedText.isNotBlank()) {
                        Spacer(modifier = Modifier.height(20.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            IndigoPrimary.copy(alpha = 0.08f),
                                            ElectricViolet.copy(alpha = 0.04f)
                                        )
                                    ),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .border(
                                    width = 1.dp,
                                    color = IndigoPrimary.copy(alpha = 0.3f),
                                    shape = RoundedCornerShape(16.dp)
                                )
                                .padding(16.dp)
                        ) {
                            Column {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${targetLang.flagEmoji} ${targetLang.name} Translation",
                                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                        color = IndigoPrimary
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        // Speed Toggle
                                        listOf(0.75f, 1.0f, 1.25f).forEach { speedVal ->
                                            val isSelected = speechSpeed == speedVal
                                            val speedLabel = if (speedVal == 1.0f) "1x" else "${speedVal}x"
                                            Surface(
                                                shape = RoundedCornerShape(8.dp),
                                                color = if (isSelected) IndigoPrimary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                                modifier = Modifier
                                                    .padding(horizontal = 2.dp)
                                                    .clickable { viewModel.setSpeechSpeed(speedVal) }
                                            ) {
                                                Text(
                                                    text = speedLabel,
                                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, fontSize = 10.sp),
                                                    color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.width(4.dp))

                                        IconButton(
                                            onClick = { viewModel.speakText(translatedText, targetLang.code) },
                                            modifier = Modifier.size(36.dp).testTag("speak_output_button")
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Speak Output", tint = IndigoPrimary, modifier = Modifier.size(20.dp))
                                        }
                                    }
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = translatedText,
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.testTag("translated_output_text")
                                )

                                if (!grammarNotes.isNullOrBlank()) {
                                    Spacer(modifier = Modifier.height(12.dp))
                                    HorizontalDivider(color = IndigoPrimary.copy(alpha = 0.2f))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Row(verticalAlignment = Alignment.Top) {
                                        Icon(
                                            Icons.Default.Lightbulb,
                                            contentDescription = null,
                                            tint = SunsetAmber,
                                            modifier = Modifier
                                                .size(16.dp)
                                                .padding(top = 2.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = grammarNotes ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "AI Rewrite",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                val rewriteStyles = listOf("Natural", "Formal", "Casual", "Professional", "Simple", "Shorter", "Detailed")
                                androidx.compose.foundation.lazy.LazyRow(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    items(rewriteStyles.size) { index ->
                                        val style = rewriteStyles[index]
                                        androidx.compose.material3.FilterChip(
                                            selected = false,
                                            onClick = { viewModel.rewriteTranslatedText(style) },
                                            label = { Text(style) },
                                            colors = androidx.compose.material3.FilterChipDefaults.filterChipColors(
                                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                                labelColor = IndigoPrimary
                                            )
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceAround
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(onClick = { viewModel.copyToClipboard(translatedText) }) {
                                            Icon(Icons.Default.ContentCopy, contentDescription = "Copy", tint = IndigoPrimary)
                                        }
                                        Text("Copy", style = MaterialTheme.typography.labelSmall)
                                    }
                                    
                                    val context = androidx.compose.ui.platform.LocalContext.current
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(
                                            onClick = {
                                                val shareIntent = android.content.Intent().apply {
                                                    action = android.content.Intent.ACTION_SEND
                                                    putExtra(android.content.Intent.EXTRA_TEXT, translatedText)
                                                    type = "text/plain"
                                                }
                                                context.startActivity(android.content.Intent.createChooser(shareIntent, "Share Translation"))
                                            }
                                        ) {
                                            Icon(Icons.Default.Share, contentDescription = "Share", tint = IndigoPrimary)
                                        }
                                        Text("Share", style = MaterialTheme.typography.labelSmall)
                                    }

                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(onClick = { viewModel.speakText(translatedText, targetLang.code) }) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Listen", tint = IndigoPrimary)
                                        }
                                        Text("Listen", style = MaterialTheme.typography.labelSmall)
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(
                                            onClick = {
                                                viewModel.saveCurrentTranslation()
                                                android.widget.Toast.makeText(context, "Saved to History", android.widget.Toast.LENGTH_SHORT).show()
                                            }
                                        ) {
                                            Icon(Icons.Default.Save, contentDescription = "Save", tint = IndigoPrimary)
                                        }
                                        Text("Save", style = MaterialTheme.typography.labelSmall)
                                    }
                                    
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        IconButton(onClick = { viewModel.translate() }) {
                                            Icon(Icons.Default.Refresh, contentDescription = "Regenerate", tint = IndigoPrimary)
                                        }
                                        Text("Regenerate", style = MaterialTheme.typography.labelSmall)
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        TranslationResultActionsRow(
                            translatedText = translatedText,
                            originalText = inputText,
                            sourceLangName = sourceLang.name,
                            targetLangName = targetLang.name,
                            snackbarHostState = snackbarHostState
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Daily Learn Card (Word + Sentence of the Day)
            DailyLearnCard(
                content = dailyLearnContent,
                onSpeakWord = { word -> viewModel.speakText(word, "en") },
                onEvaluatePronunciation = { spoken -> viewModel.evaluateSentencePronunciation(spoken) },
                practiceFeedback = practiceFeedback,
                onClearFeedback = { viewModel.clearPracticeFeedback() }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // DAILY GOAL & TOPIC STREAK CARD
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_goal_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    ElectricViolet.copy(alpha = 0.08f),
                                    SunsetAmber.copy(alpha = 0.05f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🎯", fontSize = 22.sp)
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Daily Topic Goal",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Complete 1 topic daily to build your streak!",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = SunsetAmber.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f)),
                            modifier = Modifier.clickable { onNavigateToMyProgress() }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "🔥 $topicStreak Day Streak",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = SunsetAmber,
                                    modifier = Modifier.testTag("daily_goal_streak_text")
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(14.dp))

                    // Checkbox & Label Row
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isTopicGoalCompleted) Color(0xFF10B981).copy(alpha = 0.1f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            )
                            .clickable {
                                val newCompleted = !isTopicGoalCompleted
                                isTopicGoalCompleted = newCompleted
                                val editor = topicPrefs.edit()
                                editor.putBoolean("daily_goal_topic_completed_$todayKey", newCompleted)
                                if (newCompleted) {
                                    editor.putString("last_completed_topic_date", todayKey)
                                    val storedLastDate = topicPrefs.getString("topic_last_streak_date", "")
                                    if (storedLastDate != todayKey) {
                                        val newStreak = if (storedLastDate == yesterdayKey) topicStreak + 1 else 1
                                        editor.putInt("topic_streak_count", newStreak)
                                        editor.putString("topic_last_streak_date", todayKey)
                                        topicStreak = newStreak
                                    }
                                } else {
                                    editor.remove("last_completed_topic_date")
                                }
                                editor.apply()
                            }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Checkbox(
                            checked = isTopicGoalCompleted,
                            onCheckedChange = { checked ->
                                isTopicGoalCompleted = checked
                                val editor = topicPrefs.edit()
                                editor.putBoolean("daily_goal_topic_completed_$todayKey", checked)
                                if (checked) {
                                    editor.putString("last_completed_topic_date", todayKey)
                                    val storedLastDate = topicPrefs.getString("topic_last_streak_date", "")
                                    if (storedLastDate != todayKey) {
                                        val newStreak = if (storedLastDate == yesterdayKey) topicStreak + 1 else 1
                                        editor.putInt("topic_streak_count", newStreak)
                                        editor.putString("topic_last_streak_date", todayKey)
                                        topicStreak = newStreak
                                    }
                                } else {
                                    editor.remove("last_completed_topic_date")
                                }
                                editor.apply()
                            },
                            colors = CheckboxDefaults.colors(
                                checkedColor = Color(0xFF10B981),
                                uncheckedColor = MaterialTheme.colorScheme.outline
                            ),
                            modifier = Modifier.testTag("daily_goal_checkbox")
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Daily Goal: Complete 1 Topic",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isTopicGoalCompleted) Color(0xFF10B981) else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("daily_goal_title_text")
                            )
                            Text(
                                text = if (isTopicGoalCompleted) "Goal Completed for Today! 🎉 (+1 Streak)" else "Complete 1 topic in AI Conversation or check here",
                                style = MaterialTheme.typography.bodySmall,
                                color = if (isTopicGoalCompleted) Color(0xFF059669) else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Button(
                        onClick = onNavigateToAiConversationPartner,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("start_topic_practice_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricViolet
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Mic, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isTopicGoalCompleted) "Practice Another Topic" else "Start AI Conversation Topic",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // DAILY ENGLISH LEARNING CHALLENGE CARD
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("daily_learning_challenge_card")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    SunsetAmber.copy(alpha = 0.08f),
                                    ElectricViolet.copy(alpha = 0.04f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(18.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "🔥", fontSize = 20.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "Daily Grammar Challenge",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Task of the Day",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SunsetAmber
                                )
                            }
                        }

                        // Complete Status Badge
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = if (isChallengeCompleted) Color(0xFF10B981).copy(alpha = 0.15f) else SunsetAmber.copy(alpha = 0.15f),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                if (isChallengeCompleted) Color(0xFF10B981).copy(alpha = 0.4f) else SunsetAmber.copy(alpha = 0.4f)
                            )
                        ) {
                            Text(
                                text = if (isChallengeCompleted) "Completed" else "Pending",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isChallengeCompleted) Color(0xFF10B981) else SunsetAmber,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = challengeRule.title,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = challengeRule.explanation,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Target: Study this topic and score 8/10 or more in the Practice Quiz today to increase your streak!",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    if (isChallengeCompleted) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF10B981).copy(alpha = 0.08f),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.Center
                            ) {
                                Icon(Icons.Default.CheckCircle, contentDescription = "Completed", tint = Color(0xFF10B981))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Awesome work! Streak: $dailyChallengeStreak Days 🔥",
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFF10B981)
                                )
                            }
                        }
                    } else {
                        Button(
                            onClick = { onNavigateToLearningTools(challengeRule.id) },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = SunsetAmber),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = "Start")
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Start Challenge Now", fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Home Features Cards Header
            Text(
                text = "AI Suite Features",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Feature Cards Grid (2 columns)
            val features = remember {
                listOf(
                    HomeFeatureCard(
                        title = "Text Translation",
                        description = "Instant neural translation for 100+ languages",
                        icon = Icons.Default.Translate,
                        accentColor = IndigoPrimary,
                        onClick = { /* Focus translation box */ }
                    ),
                    HomeFeatureCard(
                        title = "Voice Translation",
                        description = "Real-time speech to speech interpretation",
                        icon = Icons.Default.Mic,
                        accentColor = CyberCyan,
                        onClick = { viewModel.openModal(ActiveModal.VOICE) }
                    ),
                    HomeFeatureCard(
                        title = "Camera Translation",
                        description = "OCR visual text scan from photos & signs",
                        icon = Icons.Default.CameraAlt,
                        accentColor = ElectricViolet,
                        onClick = { viewModel.openModal(ActiveModal.CAMERA) }
                    ),
                    HomeFeatureCard(
                        title = "AI Grammar Checker",
                        description = "Deep structural correction and analysis",
                        icon = Icons.Default.AutoAwesome,
                        accentColor = EmeraldGlow,
                        onClick = { viewModel.runGrammarCheck() }
                    ),
                    HomeFeatureCard(
                        title = "Daily English Word",
                        description = "Expand vocabulary with CEFR word cards",
                        icon = Icons.Default.MenuBook,
                        accentColor = SunsetAmber,
                        onClick = { viewModel.openModal(ActiveModal.DAILY_WORD) }
                    ),
                    HomeFeatureCard(
                        title = "Daily Conversion",
                        description = "Learn 500 English to Urdu sentences",
                        icon = Icons.Default.SwapHoriz,
                        accentColor = ElectricViolet,
                        onClick = onNavigateToDailyConversion
                    ),
                    HomeFeatureCard(
                        title = "AI Conversation Partner",
                        description = "Speak to Sarah, correct mistakes & learn Urdu/English",
                        icon = Icons.Default.Mic,
                        accentColor = NeonPink,
                        onClick = onNavigateToAiConversationPartner
                    ),
                    HomeFeatureCard(
                        title = "English Learning Tools",
                        description = "Master Verbs, Tenses & Grammar with 20 rules",
                        icon = Icons.Default.School,
                        accentColor = SunsetAmber,
                        onClick = { onNavigateToLearningTools(null) }
                    ),
                    HomeFeatureCard(
                        title = "Conversation Practice",
                        description = "Interactive roleplay with AI Language Tutor",
                        icon = Icons.Default.Psychology,
                        accentColor = NeonPink,
                        onClick = { onNavigateToTab(BottomTab.AiTutor) }
                    ),
                    HomeFeatureCard(
                        title = "English Roleplay",
                        description = "Chat in Urdu with 6 AI English Characters",
                        icon = Icons.Default.TheaterComedy,
                        accentColor = ElectricViolet,
                        onClick = { onNavigateToTab(BottomTab.Roleplay) }
                    )
                )
            }

            // Grid Layout
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                features.chunked(2).forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        row.forEach { feature ->
                            Card(
                                onClick = feature.onClick,
                                shape = RoundedCornerShape(20.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                                elevation = CardDefaults.cardElevation(2.dp),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("feature_card_${feature.title.lowercase().replace(" ", "_")}")
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(44.dp)
                                            .background(
                                                color = feature.accentColor.copy(alpha = 0.15f),
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                    ) {
                                        Icon(
                                            imageVector = feature.icon,
                                            contentDescription = feature.title,
                                            tint = feature.accentColor,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }

                                    Spacer(modifier = Modifier.height(12.dp))

                                    Text(
                                        text = feature.title,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = feature.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Modals / Dialogs
        if (showSourceLangSheet) {
            LanguageSelectorSheet(
                title = "Select Source Language",
                selectedLanguage = sourceLang,
                onLanguageSelected = { viewModel.setSourceLanguage(it) },
                onDismiss = { showSourceLangSheet = false }
            )
        }

        if (showTargetLangSheet) {
            LanguageSelectorSheet(
                title = "Select Target Language",
                selectedLanguage = targetLang,
                onLanguageSelected = { viewModel.setTargetLanguage(it) },
                onDismiss = { showTargetLangSheet = false }
            )
        }

        when (activeModal) {
            ActiveModal.VOICE -> {
                VoiceTranslationModal(
                    sourceLang = sourceLang,
                    targetLang = targetLang,
                    voiceState = voiceState,
                    partialText = voicePartialText,
                    errorMessage = voiceErrorMessage,
                    onSpeechRecognized = { capturedText -> viewModel.onVoiceTextCaptured(capturedText) },
                    onVoiceStateChanged = { state -> viewModel.setVoiceState(state) },
                    onPartialTextChanged = { text -> viewModel.setVoicePartialText(text) },
                    onError = { error -> viewModel.onVoiceRecognitionError(error) },
                    onDismiss = {
                        viewModel.resetVoiceState()
                        viewModel.closeModal()
                    },
                    onDemoRecording = { viewModel.simulateVoiceRecording() }
                )
            }
            ActiveModal.CAMERA -> {
                CameraScanModal(
                    onScan = { text -> viewModel.simulateCameraScan(text) },
                    onDismiss = { viewModel.closeModal() }
                )
            }
            ActiveModal.GRAMMAR_CHECKER -> {
                grammarResult?.let { (corrected, explanation) ->
                    GrammarCheckerModal(
                        correctedText = corrected,
                        explanation = explanation,
                        onApplyCorrection = { viewModel.setInputText(corrected) },
                        onDismiss = { viewModel.closeModal() }
                    )
                }
            }
            ActiveModal.DAILY_WORD -> {
                DailyWordModal(
                    word = dailyWord,
                    onSpeak = { viewModel.speakText(it) },
                    onDismiss = { viewModel.closeModal() }
                )
            }
            ActiveModal.NONE -> {}
        }

        if (showProInfoDialog) {
            AlertDialog(
                onDismissRequest = { showProInfoDialog = false },
                icon = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(56.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(Color(0xFFFFD700).copy(alpha = 0.3f), Color.Transparent)
                                ),
                                shape = CircleShape
                            )
                    ) {
                        Text(text = "👑", fontSize = 28.sp)
                    }
                },
                title = {
                    Text(
                        text = "AI Studio PRO VIP",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.ExtraBold),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
                text = {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "You have unlocked the full Pro AI Language Studio suite:",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        val proBenefits = listOf(
                            "⚡ Ultra-Fast Gemini AI Neural Translation",
                            "🎙️ HD Voice Recognition & Pronunciation Wave",
                            "📖 500+ Daily Conversation Sentences",
                            "🤖 Sarah AI Speaking Coach (Urdu + English)",
                            "🎓 20 Master English Grammar Rules & Quizzes",
                            "🎚️ Variable Speed Audio Playback (0.75x, 1x, 1.25x)",
                            "🔥 Daily XP Streaks, Badges & Fluency Progress"
                        )

                        proBenefits.forEach { benefit ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                                        RoundedCornerShape(8.dp)
                                    )
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = benefit,
                                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showProInfoDialog = false },
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Awesome! Continue", fontWeight = FontWeight.Bold)
                    }
                },
                shape = RoundedCornerShape(24.dp),
                containerColor = MaterialTheme.colorScheme.surface
            )
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 16.dp)
                .testTag("home_snackbar_host")
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun HomeScreenPreviewContent(
    isDarkTheme: Boolean = false,
    onToggleTheme: () -> Unit = {},
    onNavigateToTab: (BottomTab) -> Unit = {}
) {
    var sourceLang by remember { mutableStateOf(Language.defaultList[0]) }
    var targetLang by remember { mutableStateOf(Language.defaultList[1]) }
    var inputText by remember { mutableStateOf("Hello, how are you?") }
    var translatedText by remember { mutableStateOf("Hola, ¿cómo estás?") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        // Top App Bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = IndigoPrimary.copy(alpha = 0.12f),
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Translate,
                            contentDescription = "Logo",
                            tint = IndigoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "Translator Pro AI",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Gemini 3.5 Neural Engine",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            IconButton(onClick = onToggleTheme) {
                Icon(
                    imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                    contentDescription = "Toggle Theme",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Language Selector Row
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${sourceLang.flagEmoji} ${sourceLang.name}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                IconButton(
                    onClick = {
                        val temp = sourceLang
                        sourceLang = targetLang
                        targetLang = temp
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.SwapHoriz,
                        contentDescription = "Swap Languages",
                        tint = IndigoPrimary
                    )
                }

                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "${targetLang.flagEmoji} ${targetLang.name}",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Source Text Input Field
        OutlinedTextField(
            value = inputText,
            onValueChange = { inputText = it },
            placeholder = { Text("Type text to translate...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp),
            shape = RoundedCornerShape(20.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndigoPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Translation Result Box
        if (translatedText.isNotBlank()) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Translation",
                        style = MaterialTheme.typography.labelMedium,
                        color = IndigoPrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = translatedText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            val previewSnackbarHostState = remember { SnackbarHostState() }
            TranslationResultActionsRow(
                translatedText = translatedText,
                originalText = inputText,
                sourceLangName = sourceLang.name,
                targetLangName = targetLang.name,
                snackbarHostState = previewSnackbarHostState
            )
        }
    }
}

@Preview(showBackground = true, name = "HomeScreen - Light")
@Composable
fun HomeScreenLightPreview() {
    TranslatorProTheme(darkTheme = false) {
        HomeScreenPreviewContent(isDarkTheme = false)
    }
}

@Preview(showBackground = true, name = "HomeScreen - Dark")
@Composable
fun HomeScreenDarkPreview() {
    TranslatorProTheme(darkTheme = true) {
        HomeScreenPreviewContent(isDarkTheme = true)
    }
}

