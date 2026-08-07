package com.example.ui.tutor

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.tooling.preview.Preview
import com.example.R
import com.example.data.model.TutorScenario
import com.example.ui.theme.*

@Composable
fun AiTutorScreen(
    viewModel: AiTutorViewModel? = null
) {
    if (LocalInspectionMode.current || viewModel == null) {
        AiTutorScreenPreviewContent()
        return
    }

    val currentMode by viewModel.currentMode.collectAsState()
    val selectedTense by viewModel.selectedTense.collectAsState()
    val selectedCategoryFilter by viewModel.selectedCategoryFilter.collectAsState()
    val tenseProgressMap by viewModel.tenseProgressMap.collectAsState()

    val quizUserAnswers by viewModel.quizUserAnswers.collectAsState()
    val quizSubmitted by viewModel.quizSubmitted.collectAsState()
    val quizScore by viewModel.quizScore.collectAsState()

    val aiTenseQuery by viewModel.aiTenseQuery.collectAsState()
    val aiTenseResponse by viewModel.aiTenseResponse.collectAsState()
    val isAskingTenseAi by viewModel.isAskingTenseAi.collectAsState()

    val selectedScenario by viewModel.selectedScenario.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val messageInput by viewModel.messageInput.collectAsState()
    val isSending by viewModel.isSending.collectAsState()

    val listState = rememberLazyListState()

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("ai_tutor_screen")
    ) {
        // Mode Switcher Segment (Only show when not in a specific tense detail screen)
        if (selectedTense == null) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column {
                    Text(
                        text = "AI Learning Hub",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = "Master 12 Tenses with Urdu Explanations & AI Practice",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Primary Tab Switcher
            TabRow(
                selectedTabIndex = currentMode.ordinal,
                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                contentColor = ElectricViolet,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tutor_mode_tab_row")
            ) {
                TutorMode.values().forEach { mode ->
                    Tab(
                        selected = currentMode == mode,
                        onClick = { viewModel.setMode(mode) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = if (mode == TutorMode.TENSES) Icons.Default.MenuBook else Icons.Default.Psychology,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = mode.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (currentMode == mode) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            }
                        },
                        modifier = Modifier.testTag("tutor_mode_tab_${mode.name}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
        }

        // Screen Body according to active mode
        if (currentMode == TutorMode.TENSES) {
            if (selectedTense != null) {
                TenseDetailView(
                    tense = selectedTense!!,
                    quizUserAnswers = quizUserAnswers,
                    quizSubmitted = quizSubmitted,
                    quizScore = quizScore,
                    aiQuery = aiTenseQuery,
                    aiResponse = aiTenseResponse,
                    isAskingAi = isAskingTenseAi,
                    onBackClick = { viewModel.selectTense(null) },
                    onSpeakText = { viewModel.speakText(it) },
                    onSelectAnswer = { exId, ans -> viewModel.selectQuizAnswer(exId, ans) },
                    onSubmitQuiz = { viewModel.submitQuiz(selectedTense!!) },
                    onResetQuiz = { viewModel.resetQuiz() },
                    onAiQueryChange = { viewModel.setAiTenseQuery(it) },
                    onAskAi = { viewModel.askTenseAi(selectedTense!!.name) }
                )
            } else {
                TensesDashboard(
                    allTenses = viewModel.allTenses,
                    progressMap = tenseProgressMap,
                    selectedFilter = selectedCategoryFilter,
                    onFilterSelected = { viewModel.setCategoryFilter(it) },
                    onSelectTense = { viewModel.selectTense(it) }
                )
            }
        } else {
            // Scenario Conversation Roleplay View
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Select Practice Scenario:",
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    IconButton(
                        onClick = { viewModel.clearCurrentChat() },
                        modifier = Modifier.testTag("clear_tutor_chat_button")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Reset Chat", tint = ElectricViolet)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Scenario Chips Carousel
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(TutorScenario.scenarios) { scenario ->
                        val isSelected = scenario.id == selectedScenario.id
                        FilterChip(
                            selected = isSelected,
                            onClick = { viewModel.selectScenario(scenario) },
                            label = {
                                Text(
                                    text = scenario.title,
                                    style = MaterialTheme.typography.labelMedium.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                    )
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricViolet,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("scenario_chip_${scenario.id}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Scenario Description Card
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = ElectricViolet.copy(alpha = 0.12f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = null,
                            tint = ElectricViolet,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = selectedScenario.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Chat Messages Thread
                LazyColumn(
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(chatMessages, key = { it.id }) { msg ->
                        val isUser = msg.sender == "user"

                        Column(
                            horizontalAlignment = if (isUser) Alignment.End else Alignment.Start,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Surface(
                                shape = RoundedCornerShape(
                                    topStart = 18.dp,
                                    topEnd = 18.dp,
                                    bottomStart = if (isUser) 18.dp else 4.dp,
                                    bottomEnd = if (isUser) 4.dp else 18.dp
                                ),
                                color = if (isUser) IndigoPrimary else MaterialTheme.colorScheme.surface,
                                tonalElevation = 2.dp,
                                modifier = Modifier
                                    .widthIn(max = 280.dp)
                                    .testTag("chat_bubble_${msg.id}")
                            ) {
                                Column(modifier = Modifier.padding(14.dp)) {
                                    Row(
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Text(
                                            text = if (isUser) "You" else "AI Tutor",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = if (isUser) Color.White.copy(alpha = 0.8f) else ElectricViolet
                                        )

                                        if (!isUser) {
                                            IconButton(
                                                onClick = { viewModel.speakText(msg.messageText) },
                                                modifier = Modifier.size(24.dp)
                                            ) {
                                                Icon(
                                                    Icons.AutoMirrored.Filled.VolumeUp,
                                                    contentDescription = "Listen",
                                                    tint = ElectricViolet,
                                                    modifier = Modifier.size(16.dp)
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))

                                    Text(
                                        text = msg.messageText,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = if (isUser) Color.White else MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            if (!msg.grammarCorrection.isNullOrBlank()) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = SunsetAmber.copy(alpha = 0.15f),
                                    modifier = Modifier.widthIn(max = 280.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = SunsetAmber,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = msg.grammarCorrection ?: "",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = SunsetAmber
                                        )
                                    }
                                }
                            }
                        }
                    }

                    if (isSending) {
                        item {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                CircularProgressIndicator(
                                    color = ElectricViolet,
                                    strokeWidth = 2.dp,
                                    modifier = Modifier.size(20.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "AI Tutor is typing...",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Message Input Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = messageInput,
                        onValueChange = { viewModel.setMessageInput(it) },
                        placeholder = { Text("Reply to AI Tutor...") },
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricViolet,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("tutor_message_input")
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    FloatingActionButton(
                        onClick = { viewModel.sendMessage() },
                        shape = CircleShape,
                        containerColor = ElectricViolet,
                        contentColor = Color.White,
                        modifier = Modifier.testTag("send_tutor_message_button")
                    ) {
                        Icon(Icons.Default.Send, contentDescription = "Send")
                    }
                }
            }
        }
    }
}

@Composable
fun AiTutorScreenPreviewContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "AI Learning Hub",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Master 12 Tenses with Urdu Explanations & AI Practice",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = ElectricViolet.copy(alpha = 0.15f)),
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Present Simple Tense (فعل حال سادہ)",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Formula: Subject + Verb (s/es) + Object",
                    style = MaterialTheme.typography.bodySmall,
                    color = ElectricViolet
                )
            }
        }
    }
}

@Preview(showBackground = true, name = "AI Tutor - Light")
@Composable
fun AiTutorScreenLightPreview() {
    TranslatorProTheme(darkTheme = false) {
        AiTutorScreenPreviewContent()
    }
}

@Preview(showBackground = true, name = "AI Tutor - Dark")
@Composable
fun AiTutorScreenDarkPreview() {
    TranslatorProTheme(darkTheme = true) {
        AiTutorScreenPreviewContent()
    }
}

