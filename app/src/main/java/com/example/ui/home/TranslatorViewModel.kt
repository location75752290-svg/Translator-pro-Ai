package com.example.ui.home

import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.PartnerCheckAnswerResult
import com.example.data.model.PartnerFinalReportResult
import com.example.data.model.PartnerQuestion
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

data class ExamHistoryItem(
    val question: PartnerQuestion,
    val userAnswer: String,
    val checkResult: PartnerCheckAnswerResult
)

class TranslatorViewModel(application: Application) : AndroidViewModel(application) {

    val repository: TranslatorRepository
    private var tts: TextToSpeech? = null
    private var isTtsInitialized = false
    private var speechRecognizer: SpeechRecognizer? = null

    private val _partnerQuestions = MutableStateFlow<List<PartnerQuestion>>(emptyList())
    val partnerQuestions: StateFlow<List<PartnerQuestion>> = _partnerQuestions.asStateFlow()

    private val _currentQuestionIndex = MutableStateFlow(0)
    val currentQuestionIndex: StateFlow<Int> = _currentQuestionIndex.asStateFlow()

    private val _checkResult = MutableStateFlow<PartnerCheckAnswerResult?>(null)
    val checkResult: StateFlow<PartnerCheckAnswerResult?> = _checkResult.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private val _spokenText = MutableStateFlow("")
    val spokenText: StateFlow<String> = _spokenText.asStateFlow()

    private val _isLoadingQuestions = MutableStateFlow(false)
    val isLoadingQuestions: StateFlow<Boolean> = _isLoadingQuestions.asStateFlow()

    private val _isEvaluating = MutableStateFlow(false)
    val isEvaluating: StateFlow<Boolean> = _isEvaluating.asStateFlow()

    private val _isGeneratingReport = MutableStateFlow(false)
    val isGeneratingReport: StateFlow<Boolean> = _isGeneratingReport.asStateFlow()

    private val _finalReport = MutableStateFlow<PartnerFinalReportResult?>(null)
    val finalReport: StateFlow<PartnerFinalReportResult?> = _finalReport.asStateFlow()

    private val _history = MutableStateFlow<List<ExamHistoryItem>>(emptyList())
    val history: StateFlow<List<ExamHistoryItem>> = _history.asStateFlow()

    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()

    private val _currentTopic = MutableStateFlow("Daily Conversation")
    val currentTopic: StateFlow<String> = _currentTopic.asStateFlow()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(
            translationDao = database.translationDao(),
            dictionaryDao = database.dictionaryDao(),
            tutorChatDao = database.tutorChatDao(),
            tenseProgressDao = database.tenseProgressDao()
        )

        tts = TextToSpeech(application) { status ->
            if (status == TextToSpeech.SUCCESS) {
                tts?.language = Locale.US
                tts?.setSpeechRate(0.95f)
                isTtsInitialized = true
            }
        }
    }

    fun loadQuestions(topic: String, level: String = "Intermediate") {
        _currentTopic.value = topic
        _isLoadingQuestions.value = true
        _currentQuestionIndex.value = 0
        _checkResult.value = null
        _spokenText.value = ""
        _finalReport.value = null
        _history.value = emptyList()

        viewModelScope.launch {
            val result = repository.generatePartnerQuestions(topic, level)
            result.onSuccess { list ->
                _partnerQuestions.value = list
            }.onFailure {
                // Fallback handled in repository
            }
            _isLoadingQuestions.value = false
        }
    }

    fun setSpokenText(text: String) {
        _spokenText.value = text
    }

    fun startListening(context: Context) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            _isListening.value = false
            return
        }

        try {
            speechRecognizer?.destroy()
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isListening.value = true
                    }

                    override fun onBeginningOfSpeech() {}
                    override fun onRmsChanged(rmsdB: Float) {}
                    override fun onBufferReceived(buffer: ByteArray?) {}
                    override fun onEndOfSpeech() {
                        _isListening.value = false
                    }

                    override fun onError(error: Int) {
                        _isListening.value = false
                    }

                    override fun onResults(results: Bundle?) {
                        _isListening.value = false
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull().orEmpty()
                        if (text.isNotBlank()) {
                            _spokenText.value = text
                            evaluateAnswer(text)
                        }
                    }

                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        val text = matches?.firstOrNull().orEmpty()
                        if (text.isNotBlank()) {
                            _spokenText.value = text
                        }
                    }

                    override fun onEvent(eventType: Int, params: Bundle?) {}
                })
            }

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
            }
            speechRecognizer?.startListening(intent)
        } catch (e: Exception) {
            _isListening.value = false
            e.printStackTrace()
        }
    }

    fun stopListening() {
        try {
            speechRecognizer?.stopListening()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        _isListening.value = false
    }

    fun evaluateAnswer(userAnswer: String = _spokenText.value) {
        val questions = _partnerQuestions.value
        val currentIndex = _currentQuestionIndex.value
        val currentQ = questions.getOrNull(currentIndex) ?: return
        val answer = userAnswer.ifBlank { _spokenText.value }

        if (answer.isBlank()) return

        _isEvaluating.value = true
        viewModelScope.launch {
            val keywordsJoined = currentQ.expectedKeywords.joinToString(", ")
            val result = repository.checkPartnerAnswer(
                questionAsked = currentQ.questionEn,
                userSpokenAnswer = answer,
                expectedKeywords = keywordsJoined
            )

            result.onSuccess { checkRes ->
                _checkResult.value = checkRes
                // Record into history
                val updatedHistory = _history.value.toMutableList()
                val existingIndex = updatedHistory.indexOfFirst { it.question.qId == currentQ.qId }
                val historyItem = ExamHistoryItem(currentQ, answer, checkRes)
                if (existingIndex >= 0) {
                    updatedHistory[existingIndex] = historyItem
                } else {
                    updatedHistory.add(historyItem)
                }
                _history.value = updatedHistory
            }
            _isEvaluating.value = false
        }
    }

    fun retryCurrentQuestion() {
        _checkResult.value = null
        _spokenText.value = ""
    }

    fun nextQuestion() {
        val nextIdx = _currentQuestionIndex.value + 1
        if (nextIdx < _partnerQuestions.value.size) {
            _currentQuestionIndex.value = nextIdx
            _checkResult.value = null
            _spokenText.value = ""
        } else {
            // Completed all questions! Generate Final Report
            generateFinalReport()
        }
    }

    fun generateFinalReport() {
        _isGeneratingReport.value = true
        viewModelScope.launch {
            val historyItems = _history.value
            val historySummary = buildString {
                appendLine("Topic: ${_currentTopic.value}")
                historyItems.forEachIndexed { i, item ->
                    appendLine("Q${i + 1}: ${item.question.questionEn}")
                    appendLine("User Answer: ${item.userAnswer}")
                    appendLine("Score: ${item.checkResult.score}/100, Correct: ${item.checkResult.isCorrect}")
                    appendLine("Feedback: ${item.checkResult.feedbackEn}")
                    appendLine("---")
                }
            }

            val reportRes = repository.generatePartnerFinalReport(historySummary)
            reportRes.onSuccess { report ->
                _finalReport.value = report
            }
            _isGeneratingReport.value = false
        }
    }

    fun speakText(text: String, isUrdu: Boolean = false) {
        if (!isTtsInitialized || text.isBlank()) return
        try {
            tts?.language = if (isUrdu) Locale("ur", "PK") else Locale.US
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "LiveExamTTS")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun stopSpeaking() {
        try {
            tts?.stop()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        try {
            speechRecognizer?.destroy()
            tts?.stop()
            tts?.shutdown()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
