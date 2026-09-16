package com.example.ui.home.components

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Warning
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import com.example.data.model.Language
import com.example.ui.home.VoiceState
import com.example.ui.theme.*
import com.example.util.SpeechLanguageUtils

@Composable
fun VoiceTranslationModal(
    sourceLang: Language,
    targetLang: Language,
    voiceState: VoiceState,
    partialText: String = "",
    errorMessage: String? = null,
    onSpeechRecognized: (String) -> Unit,
    onVoiceStateChanged: (VoiceState) -> Unit,
    onPartialTextChanged: (String) -> Unit,
    onError: (String) -> Unit,
    onDismiss: () -> Unit,
    onDemoRecording: () -> Unit
) {
    val context = LocalContext.current

    // Remember speech recognizer instance for lifecycle management
    var speechRecognizer by remember { mutableStateOf<SpeechRecognizer?>(null) }
    var isListening by remember { mutableStateOf(false) }

    // Pulse animation for recording state
    val infiniteTransition = rememberInfiniteTransition(label = "wave")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.35f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // System Activity Speech Intent Fallback
    val speechIntentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        isListening = false
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val matches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            if (!matches.isNullOrEmpty()) {
                onSpeechRecognized(matches[0])
            } else {
                onError("No text recognized.")
            }
        } else {
            onError("Voice recognition cancelled.")
        }
    }

    // Helper to launch speech recognition intent or service
    val startListeningProcess = {
        val bcp47Tag = SpeechLanguageUtils.getBcp47LanguageTag(sourceLang.code)
        val speechIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, bcp47Tag)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, bcp47Tag)
            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, true)
            putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak in ${sourceLang.name} (${sourceLang.nativeName})...")
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3)
            putExtra("android.speech.extra.DICTATION_MODE", true)
        }

        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            try {
                if (speechRecognizer == null) {
                    speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
                }

                speechRecognizer?.setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        isListening = true
                        onVoiceStateChanged(VoiceState.RECORDING)
                        onPartialTextChanged("Listening... Speak in ${sourceLang.name} (${sourceLang.nativeName})")
                    }

                    override fun onBeginningOfSpeech() {
                        isListening = true
                        onVoiceStateChanged(VoiceState.RECORDING)
                    }

                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}

                    override fun onEndOfSpeech() {
                        isListening = false
                        onVoiceStateChanged(VoiceState.PROCESSING)
                    }

                    override fun onError(error: Int) {
                        isListening = false
                        // If speech recognizer failed with network or service error, fallback to system prompt
                        if (error == SpeechRecognizer.ERROR_NETWORK || error == SpeechRecognizer.ERROR_SERVER || error == SpeechRecognizer.ERROR_CLIENT) {
                            try {
                                speechIntentLauncher.launch(speechIntent)
                                return
                            } catch (_: Exception) {}
                        }

                        val errorMsg = when (error) {
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech detected in ${sourceLang.name}. Please speak clearly into the microphone."
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech input timed out. Tap microphone to speak again."
                            SpeechRecognizer.ERROR_AUDIO -> "Microphone error. Please check your mic hardware."
                            SpeechRecognizer.ERROR_NETWORK, SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network connection issue for speech service."
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Microphone permission is required."
                            else -> "Voice recognition notice ($error). Tap microphone to try speaking again."
                        }
                        onError(errorMsg)
                    }

                    override fun onResults(results: Bundle?) {
                        isListening = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val text = matches[0]
                            onSpeechRecognized(text)
                        } else {
                            onError("Could not capture spoken text clearly. Please try again.")
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            onPartialTextChanged(matches[0])
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })

                speechRecognizer?.startListening(speechIntent)
            } catch (e: Exception) {
                isListening = false
                try {
                    speechIntentLauncher.launch(speechIntent)
                } catch (fallbackEx: Exception) {
                    onError("Voice recognizer not available: ${e.localizedMessage}")
                }
            }
        } else {
            // SpeechRecognizer service unavailable, launch system intent fallback
            try {
                speechIntentLauncher.launch(speechIntent)
            } catch (e: Exception) {
                onDemoRecording()
            }
        }
    }

    // Runtime Permission Launcher for RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListeningProcess()
        } else {
            Toast.makeText(context, "Microphone permission is required for voice input", Toast.LENGTH_SHORT).show()
            onError("Microphone permission denied.")
        }
    }

    // Cleanup speech recognizer on dismiss/dispose
    DisposableEffect(Unit) {
        onDispose {
            try {
                speechRecognizer?.stopListening()
                speechRecognizer?.destroy()
            } catch (_: Exception) {}
            speechRecognizer = null
        }
    }

    val toggleRecording = {
        if (voiceState == VoiceState.RECORDING || isListening) {
            try {
                speechRecognizer?.stopListening()
            } catch (_: Exception) {}
            isListening = false
            onVoiceStateChanged(VoiceState.PROCESSING)
        } else {
            val hasPermission = ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED

            if (hasPermission) {
                startListeningProcess()
            } else {
                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }

    Dialog(onDismissRequest = {
        try {
            speechRecognizer?.stopListening()
            speechRecognizer?.destroy()
        } catch (_: Exception) {}
        speechRecognizer = null
        onDismiss()
    }) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .testTag("voice_modal")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Voice Translation",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                    IconButton(
                        onClick = {
                            try {
                                speechRecognizer?.stopListening()
                                speechRecognizer?.destroy()
                            } catch (_: Exception) {}
                            speechRecognizer = null
                            onDismiss()
                        },
                        modifier = Modifier.testTag("close_voice_modal")
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Language direction badge
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = IndigoPrimary.copy(alpha = 0.12f)
                ) {
                    Text(
                        text = "${sourceLang.flagEmoji} ${sourceLang.name} (${sourceLang.nativeName})  ➔  ${targetLang.flagEmoji} ${targetLang.name}",
                        style = MaterialTheme.typography.labelLarge,
                        color = IndigoPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Microphone Wave Circle
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.size(130.dp)
                ) {
                    if (voiceState == VoiceState.RECORDING || isListening) {
                        Box(
                            modifier = Modifier
                                .scale(pulseScale)
                                .size(115.dp)
                                .background(
                                    brush = Brush.radialGradient(
                                        colors = listOf(CyberCyan.copy(alpha = 0.45f), Color.Transparent)
                                    ),
                                    shape = CircleShape
                                )
                        )
                    }

                    FloatingActionButton(
                        onClick = { toggleRecording() },
                        shape = CircleShape,
                        containerColor = if (voiceState == VoiceState.RECORDING || isListening) ElectricViolet else IndigoPrimary,
                        contentColor = Color.White,
                        modifier = Modifier
                            .size(80.dp)
                            .testTag("mic_record_button")
                    ) {
                        Icon(
                            imageVector = if (voiceState == VoiceState.RECORDING || isListening) Icons.Default.Stop else Icons.Default.Mic,
                            contentDescription = "Microphone",
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Status message
                Text(
                    text = when {
                        voiceState == VoiceState.RECORDING || isListening -> "Listening... Speak in ${sourceLang.name}"
                        voiceState == VoiceState.PROCESSING -> "Processing Speech..."
                        else -> "Tap microphone to speak"
                    },
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (voiceState == VoiceState.RECORDING || isListening) CyberCyan else MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Live Partial Transcript / Captured Text Box
                if (partialText.isNotBlank()) {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.3f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        Text(
                            text = partialText,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }

                // Error message banner if any
                AnimatedVisibility(visible = !errorMessage.isNullOrBlank()) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.8f),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = "Error",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = errorMessage.orEmpty(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Demo speech option button for testing environment
                TextButton(
                    onClick = onDemoRecording,
                    modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                        text = "Test Sample Voice (${sourceLang.name})",
                        style = MaterialTheme.typography.labelMedium,
                        color = IndigoPrimary.copy(alpha = 0.8f)
                    )
                }
            }
        }
    }
}
