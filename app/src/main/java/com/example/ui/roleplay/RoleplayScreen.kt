package com.example.ui.roleplay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.data.model.RoleplayCharacter
import com.example.ui.theme.ElectricViolet
import com.example.ui.theme.IndigoPrimary
import kotlinx.coroutines.launch

@Composable
fun RoleplayScreen(
    viewModel: RoleplayViewModel
) {
    val selectedCharacter by viewModel.selectedCharacter.collectAsState()
    val chatMessages by viewModel.chatMessages.collectAsState()
    val messageInput by viewModel.messageInput.collectAsState()
    val isSending by viewModel.isSending.collectAsState()

    AnimatedContent(
        targetState = selectedCharacter,
        transitionSpec = {
            if (targetState != null) {
                slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
            } else {
                slideInHorizontally { -it } + fadeIn() togetherWith slideOutHorizontally { it } + fadeOut()
            }
        },
        label = "roleplay_screen_transition"
    ) { character ->
        if (character == null) {
            RoleplayCharacterSelectionScreen(
                onSelectCharacter = { viewModel.selectCharacter(it) }
            )
        } else {
            RoleplayChatScreen(
                character = character,
                chatMessages = chatMessages,
                messageInput = messageInput,
                isSending = isSending,
                onBackClick = { viewModel.backToCharacterSelection() },
                onSendMessage = { viewModel.sendMessage(it) },
                onInputChange = { viewModel.setMessageInput(it) },
                onSpeakText = { viewModel.speakEnglish(it) },
                onResetChat = { viewModel.clearCurrentChat() }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleplayCharacterSelectionScreen(
    onSelectCharacter: (RoleplayCharacter) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("roleplay_selection_screen")
    ) {
        // Header
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = ElectricViolet.copy(alpha = 0.12f),
            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = "🎭",
                    fontSize = 32.sp
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = "English AI Character Roleplay",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "اردو میں بات کریں اور انگریزی میں جواب پائیں",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = ElectricViolet
                    )
                    Text(
                        text = "Speak Urdu to practice real-life English scenarios with AI characters.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Select a Character to Chat:",
            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Grid of 6 character cards
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(RoleplayCharacter.characters, key = { it.id }) { character ->
                CharacterCard(
                    character = character,
                    onClick = { onSelectCharacter(character) }
                )
            }
        }
    }
}

@Composable
fun CharacterCard(
    character: RoleplayCharacter,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.2f)),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("character_card_${character.id}")
    ) {
        Column(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .background(ElectricViolet.copy(alpha = 0.15f), CircleShape)
            ) {
                Text(
                    text = character.avatarEmoji,
                    fontSize = 28.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = character.name,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = character.title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = ElectricViolet
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "(${character.urduTitle})",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = character.description,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 3,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoleplayChatScreen(
    character: RoleplayCharacter,
    chatMessages: List<com.example.data.model.RoleplayMessage>,
    messageInput: String,
    isSending: Boolean,
    onBackClick: () -> Unit,
    onSendMessage: (String?) -> Unit,
    onInputChange: (String) -> Unit,
    onSpeakText: (String) -> Unit,
    onResetChat: () -> Unit
) {
    val context = LocalContext.current
    val listState = rememberLazyListState()

    // Speech Recognizer Launcher for Urdu Voice Input
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val spokenText = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)?.firstOrNull()
            if (!spokenText.isNullOrBlank()) {
                onInputChange(spokenText)
            }
        }
    }

    val recordAudioPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur-PK")
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ur-PK")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in Urdu...")
            }
            try {
                speechLauncher.launch(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) {
            listState.animateScrollToItem(chatMessages.size - 1)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(12.dp)
            .testTag("roleplay_chat_screen")
    ) {
        // Character Top Bar Header
        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                IconButton(
                    onClick = onBackClick,
                    modifier = Modifier.testTag("back_to_roleplay_list_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back to Characters"
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(44.dp)
                        .background(ElectricViolet.copy(alpha = 0.15f), CircleShape)
                ) {
                    Text(text = character.avatarEmoji, fontSize = 22.sp)
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${character.name} (${character.title})",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "Urdu ➔ English AI Character • ${character.urduTitle}",
                        style = MaterialTheme.typography.labelSmall,
                        color = ElectricViolet
                    )
                }

                IconButton(
                    onClick = onResetChat,
                    modifier = Modifier.testTag("reset_roleplay_chat_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Chat",
                        tint = ElectricViolet
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Chat Message List
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
                    if (isUser) {
                        // User message (Urdu)
                        Surface(
                            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 18.dp, bottomEnd = 4.dp),
                            color = IndigoPrimary,
                            modifier = Modifier
                                .widthIn(max = 290.dp)
                                .testTag("roleplay_user_bubble_${msg.id}")
                        ) {
                            Column(modifier = Modifier.padding(12.dp)) {
                                Text(
                                    text = "You (اردو)",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = Color.White.copy(alpha = 0.8f)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = msg.urduText,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.White
                                )
                            }
                        }
                    } else {
                        // Character message (English + Urdu Translation)
                        Surface(
                            shape = RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp, bottomStart = 4.dp, bottomEnd = 18.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 2.dp,
                            border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.25f)),
                            modifier = Modifier
                                .widthIn(max = 300.dp)
                                .testTag("roleplay_char_bubble_${msg.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "${character.avatarEmoji} ${character.name}",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = ElectricViolet
                                    )

                                    IconButton(
                                        onClick = { onSpeakText(msg.englishText) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                            contentDescription = "Listen English Pronunciation",
                                            tint = ElectricViolet,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(4.dp))

                                // English Response
                                Text(
                                    text = msg.englishText,
                                    style = MaterialTheme.typography.bodyLarge.copy(
                                        fontWeight = FontWeight.SemiBold,
                                        fontSize = 17.sp
                                    ),
                                    color = MaterialTheme.colorScheme.onSurface
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                // Urdu Translation Box
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = ElectricViolet.copy(alpha = 0.08f),
                                    border = androidx.compose.foundation.BorderStroke(0.5.dp, ElectricViolet.copy(alpha = 0.2f)),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(8.dp)) {
                                        Text(
                                            text = "اردو ترجمہ (Urdu Translation):",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                            color = ElectricViolet
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = msg.urduText,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
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
                            text = "${character.name} is responding in English...",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Message Input Row with Voice Input (STT)
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Voice Input Button
            IconButton(
                onClick = {
                    if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ur-PK")
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, "ur-PK")
                            putExtra(RecognizerIntent.EXTRA_PROMPT, "Urdu me bolein...")
                        }
                        try {
                            speechLauncher.launch(intent)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    } else {
                        recordAudioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = ElectricViolet.copy(alpha = 0.12f),
                    contentColor = ElectricViolet
                ),
                modifier = Modifier
                    .size(48.dp)
                    .testTag("roleplay_mic_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Mic,
                    contentDescription = "Urdu Voice Input"
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            OutlinedTextField(
                value = messageInput,
                onValueChange = onInputChange,
                placeholder = { Text("اردو، رومن یا انگلش میں لکھیں...") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ElectricViolet,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outline
                ),
                modifier = Modifier
                    .weight(1f)
                    .testTag("roleplay_message_input")
            )

            Spacer(modifier = Modifier.width(8.dp))

            FloatingActionButton(
                onClick = { onSendMessage(null) },
                shape = CircleShape,
                containerColor = ElectricViolet,
                contentColor = Color.White,
                modifier = Modifier.testTag("roleplay_send_button")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send Message")
            }
        }
    }
}
