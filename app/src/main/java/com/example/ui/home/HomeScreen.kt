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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.example.R
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
    val grammarResult by viewModel.grammarCheckResult.collectAsState()

    var showSourceLangSheet by remember { mutableStateOf(false) }
    var showTargetLangSheet by remember { mutableStateOf(false) }

    var swapRotationAngle by remember { mutableFloatStateOf(0f) }
    val animatedRotation by animateFloatAsState(
        targetValue = swapRotationAngle,
        animationSpec = tween(durationMillis = 300),
        label = "swap_rotation"
    )

    val scrollState = rememberScrollState()

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
                    // Streak Pill
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = SunsetAmber.copy(alpha = 0.15f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.3f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = "🔥", fontSize = 14.sp)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "5 Days",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = SunsetAmber
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    // Dark/Light Theme Toggle
                    IconButton(
                        onClick = onToggleTheme,
                        modifier = Modifier.testTag("theme_toggle_button")
                    ) {
                        Icon(
                            imageVector = if (isDarkTheme) Icons.Default.LightMode else Icons.Default.DarkMode,
                            contentDescription = "Toggle Theme",
                            tint = if (isDarkTheme) SunsetAmber else IndigoPrimary
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
                        }
                    }

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
                                    Row {
                                        IconButton(
                                            onClick = { viewModel.speakText(translatedText, targetLang.code) },
                                            modifier = Modifier.testTag("speak_output_button")
                                        ) {
                                            Icon(Icons.Default.VolumeUp, contentDescription = "Speak Output", tint = IndigoPrimary)
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
                        title = "Conversation Practice",
                        description = "Interactive roleplay with AI Language Tutor",
                        icon = Icons.Default.Psychology,
                        accentColor = NeonPink,
                        onClick = { onNavigateToTab(BottomTab.AiTutor) }
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
                    onScan = { viewModel.simulateCameraScan() },
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

