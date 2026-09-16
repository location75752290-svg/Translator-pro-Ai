package com.example.ui.tutor

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.TenseProgressEntity
import com.example.data.local.TutorChatEntity
import com.example.data.model.*
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

enum class TutorMode(val title: String) {
    SCENARIOS("AI Conversation Roleplay"),
    TENSES("English Tenses Masterclass")
}

class AiTutorViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository
    private var tts: TextToSpeech? = null

    private var isTtsReady = false

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(
            translationDao = db.translationDao(),
            dictionaryDao = db.dictionaryDao(),
            tutorChatDao = db.tutorChatDao(),
            tenseProgressDao = db.tenseProgressDao()
        )

        try {
            tts = TextToSpeech(application) { status ->
                if (status != TextToSpeech.ERROR) {
                    isTtsReady = true
                    try {
                        tts?.language = Locale.US
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            tts = null
        }
    }

    // --- Mode Management ---
    private val _currentMode = MutableStateFlow(TutorMode.TENSES)
    val currentMode: StateFlow<TutorMode> = _currentMode.asStateFlow()

    fun setMode(mode: TutorMode) {
        _currentMode.value = mode
    }

    // --- Scenario Chat (Roleplay) ---
    private val _selectedScenario = MutableStateFlow(TutorScenario.scenarios.first())
    val selectedScenario: StateFlow<TutorScenario> = _selectedScenario.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    val chatMessages: StateFlow<List<TutorChatEntity>> = _selectedScenario
        .flatMapLatest { scenario ->
            repository.getScenarioMessages(scenario.id)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectScenario(scenario: TutorScenario) {
        _selectedScenario.value = scenario
        viewModelScope.launch {
            val existing = repository.getScenarioMessages(scenario.id).firstOrNull()
            if (existing.isNullOrEmpty()) {
                repository.saveTutorMessage(
                    scenarioId = scenario.id,
                    sender = "ai",
                    text = scenario.initialMessage
                )
            }
        }
    }

    fun setMessageInput(input: String) {
        _messageInput.value = input
    }

    fun sendMessage() {
        val text = _messageInput.value.trim()
        if (text.isBlank() || _isSending.value) return

        val scenario = _selectedScenario.value
        _messageInput.value = ""

        viewModelScope.launch {
            _isSending.value = true

            repository.saveTutorMessage(
                scenarioId = scenario.id,
                sender = "user",
                text = text
            )

            val currentHistory = chatMessages.value
            val result = repository.sendTutorMessage(
                scenarioSystemPrompt = scenario.systemPrompt,
                userMessage = text,
                conversationHistory = currentHistory
            )

            result.onSuccess { (reply, correction) ->
                repository.saveTutorMessage(
                    scenarioId = scenario.id,
                    sender = "ai",
                    text = reply,
                    correction = correction
                )
            }.onFailure {
                repository.saveTutorMessage(
                    scenarioId = scenario.id,
                    sender = "ai",
                    text = "Sorry, I had a brief issue connecting to the AI language engine. Let's try again!"
                )
            }

            _isSending.value = false
        }
    }

    fun clearCurrentChat() {
        viewModelScope.launch {
            repository.clearScenarioChat(_selectedScenario.value.id)
            repository.saveTutorMessage(
                scenarioId = _selectedScenario.value.id,
                sender = "ai",
                text = _selectedScenario.value.initialMessage
            )
        }
    }

    // --- TENSES MODULE STATE ---
    val allTenses: List<EnglishTense> = TenseDataset.tenses

    private val _selectedCategoryFilter = MutableStateFlow("ALL")
    val selectedCategoryFilter: StateFlow<String> = _selectedCategoryFilter.asStateFlow()

    private val _selectedTense = MutableStateFlow<EnglishTense?>(null)
    val selectedTense: StateFlow<EnglishTense?> = _selectedTense.asStateFlow()

    val tenseProgressMap: StateFlow<Map<String, TenseProgressEntity>> = repository.allTenseProgress
        .map { list -> list.associateBy { it.tenseId } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    // Quiz State
    private val _quizUserAnswers = MutableStateFlow<Map<String, String>>(emptyMap())
    val quizUserAnswers: StateFlow<Map<String, String>> = _quizUserAnswers.asStateFlow()

    private val _quizSubmitted = MutableStateFlow(false)
    val quizSubmitted: StateFlow<Boolean> = _quizSubmitted.asStateFlow()

    private val _quizScore = MutableStateFlow(0)
    val quizScore: StateFlow<Int> = _quizScore.asStateFlow()

    // AI Tense Tutor Q&A State
    private val _aiTenseQuery = MutableStateFlow("")
    val aiTenseQuery: StateFlow<String> = _aiTenseQuery.asStateFlow()

    private val _aiTenseResponse = MutableStateFlow<String?>(null)
    val aiTenseResponse: StateFlow<String?> = _aiTenseResponse.asStateFlow()

    private val _isAskingTenseAi = MutableStateFlow(false)
    val isAskingTenseAi: StateFlow<Boolean> = _isAskingTenseAi.asStateFlow()

    fun setCategoryFilter(filter: String) {
        _selectedCategoryFilter.value = filter
    }

    fun selectTense(tense: EnglishTense?) {
        _selectedTense.value = tense
        resetQuiz()
        _aiTenseResponse.value = null
        _aiTenseQuery.value = ""
    }

    fun selectQuizAnswer(exerciseId: String, answer: String) {
        if (_quizSubmitted.value) return
        val current = _quizUserAnswers.value.toMutableMap()
        current[exerciseId] = answer
        _quizUserAnswers.value = current
    }

    fun submitQuiz(tense: EnglishTense) {
        if (_quizSubmitted.value) return
        var correctCount = 0
        val total = tense.exercises.size
        if (total == 0) return

        tense.exercises.forEach { ex ->
            val userAns = _quizUserAnswers.value[ex.id]?.trim()?.lowercase().orEmpty()
            val correctAns = ex.correctAnswer.trim().lowercase()
            if (userAns == correctAns) {
                correctCount++
            }
        }

        val percentage = ((correctCount.toFloat() / total.toFloat()) * 100).toInt()
        _quizScore.value = percentage
        _quizSubmitted.value = true

        viewModelScope.launch {
            repository.saveTenseProgress(
                tenseId = tense.id,
                isCompleted = true,
                scorePercentage = percentage
            )
        }
    }

    fun resetQuiz() {
        _quizUserAnswers.value = emptyMap()
        _quizSubmitted.value = false
        _quizScore.value = 0
    }

    fun setAiTenseQuery(query: String) {
        _aiTenseQuery.value = query
    }

    fun askTenseAi(tenseName: String) {
        val q = _aiTenseQuery.value.trim()
        if (q.isBlank() || _isAskingTenseAi.value) return

        viewModelScope.launch {
            _isAskingTenseAi.value = true
            val result = repository.askTenseAiTutor(
                tenseName = tenseName,
                userQuestion = q
            )

            result.onSuccess { text ->
                _aiTenseResponse.value = text
            }.onFailure {
                _aiTenseResponse.value = "Sorry, unable to process grammar question right now. Please check your internet connection."
            }

            _isAskingTenseAi.value = false
        }
    }

    fun speakText(text: String) {
        if (text.isBlank()) return
        if (tts == null) {
            try {
                tts = TextToSpeech(getApplication()) { status ->
                    if (status != TextToSpeech.ERROR) {
                        isTtsReady = true
                        tts?.language = Locale.US
                        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tutor_tts_${System.currentTimeMillis()}")
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return
        }
        try {
            tts?.language = Locale.US
            tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tutor_tts_${System.currentTimeMillis()}")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
