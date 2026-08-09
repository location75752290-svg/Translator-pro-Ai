package com.example.ui.home

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.DictionaryWord
import com.example.data.model.Language
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.*

enum class ActiveModal { NONE, VOICE, CAMERA, GRAMMAR_CHECKER, DAILY_WORD }
enum class VoiceState { IDLE, RECORDING, PROCESSING }

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository
    private var tts: TextToSpeech? = null

    private val _sourceLanguage = MutableStateFlow(Language.getByCode("en"))
    val sourceLanguage: StateFlow<Language> = _sourceLanguage.asStateFlow()

    private val _targetLanguage = MutableStateFlow(Language.getByCode("es"))
    val targetLanguage: StateFlow<Language> = _targetLanguage.asStateFlow()

    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText.asStateFlow()

    private val _grammarNotes = MutableStateFlow<String?>(null)
    val grammarNotes: StateFlow<String?> = _grammarNotes.asStateFlow()

    private val _formality = MutableStateFlow("Natural")
    val formality: StateFlow<String> = _formality.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _activeModal = MutableStateFlow(ActiveModal.NONE)
    val activeModal: StateFlow<ActiveModal> = _activeModal.asStateFlow()

    private val _voiceState = MutableStateFlow(VoiceState.IDLE)
    val voiceState: StateFlow<VoiceState> = _voiceState.asStateFlow()

    private val _voicePartialText = MutableStateFlow("")
    val voicePartialText: StateFlow<String> = _voicePartialText.asStateFlow()

    private val _voiceErrorMessage = MutableStateFlow<String?>(null)
    val voiceErrorMessage: StateFlow<String?> = _voiceErrorMessage.asStateFlow()

    private val _dailyWord = MutableStateFlow<DictionaryWord?>(null)
    val dailyWord: StateFlow<DictionaryWord?> = _dailyWord.asStateFlow()

    private val _grammarCheckResult = MutableStateFlow<Pair<String, String>?>(null)
    val grammarCheckResult: StateFlow<Pair<String, String>?> = _grammarCheckResult.asStateFlow()

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(db.translationDao(), db.dictionaryDao(), db.tutorChatDao())
        
        try {
            tts = TextToSpeech(application) { status ->
                if (status != TextToSpeech.ERROR) {
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
        
        loadDailyWord()
    }

    fun setSourceLanguage(language: Language) {
        _sourceLanguage.value = language
    }

    fun setTargetLanguage(language: Language) {
        _targetLanguage.value = language
    }

    fun swapLanguages() {
        val temp = _sourceLanguage.value
        _sourceLanguage.value = _targetLanguage.value
        _targetLanguage.value = temp
        if (_translatedText.value.isNotBlank()) {
            _inputText.value = _translatedText.value
            _translatedText.value = ""
            _grammarNotes.value = null
        }
    }

    fun setInputText(text: String) {
        _inputText.value = text
    }

    fun setFormality(formality: String) {
        _formality.value = formality
    }

    fun openModal(modal: ActiveModal) {
        _activeModal.value = modal
        if (modal == ActiveModal.DAILY_WORD && _dailyWord.value == null) {
            loadDailyWord()
        }
    }

    fun closeModal() {
        _activeModal.value = ActiveModal.NONE
        _voiceState.value = VoiceState.IDLE
    }

    fun translate() {
        val text = _inputText.value.trim()
        if (text.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _translatedText.value = ""
            _grammarNotes.value = null
            
            val result = repository.translateText(
                sourceText = text,
                sourceLang = _sourceLanguage.value,
                targetLang = _targetLanguage.value,
                formality = _formality.value
            )

            result.onSuccess { (trans, notes) ->
                _translatedText.value = trans
                _grammarNotes.value = notes
            }.onFailure { err ->
                _errorMessage.value = err.localizedMessage ?: "Translation failed"
            }

            _isLoading.value = false
        }
    }

    fun rewriteTranslatedText(style: String) {
        val currentTranslation = _translatedText.value
        if (currentTranslation.isBlank()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            _translatedText.value = ""
            _grammarNotes.value = null
            
            val result = repository.rewriteText(
                text = currentTranslation,
                style = style,
                language = _targetLanguage.value
            )

            result.onSuccess { rewritten ->
                _translatedText.value = rewritten
                _grammarNotes.value = "Rewritten in $style style."
            }.onFailure { err ->
                _errorMessage.value = err.localizedMessage ?: "Rewrite failed"
            }

            _isLoading.value = false
        }
    }

    fun saveCurrentTranslation() {
        viewModelScope.launch {
            repository.saveTranslationToHistory(
                sourceLang = _sourceLanguage.value.code,
                targetLang = _targetLanguage.value.code,
                sourceText = _inputText.value,
                translatedText = _translatedText.value,
                grammarNotes = _grammarNotes.value
            )
        }
    }

    fun runGrammarCheck() {
        val text = _inputText.value.ifBlank { "I goes to school yesterday and see my friend." }
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.checkGrammar(text)
            result.onSuccess { pair ->
                _grammarCheckResult.value = pair
                openModal(ActiveModal.GRAMMAR_CHECKER)
            }
            _isLoading.value = false
        }
    }

    fun setVoiceState(state: VoiceState) {
        _voiceState.value = state
        if (state == VoiceState.RECORDING) {
            _voiceErrorMessage.value = null
        }
    }

    fun setVoicePartialText(text: String) {
        _voicePartialText.value = text
    }

    fun onVoiceTextCaptured(capturedText: String) {
        if (capturedText.isBlank()) return
        _inputText.value = capturedText.trim()
        _voicePartialText.value = capturedText.trim()
        _voiceState.value = VoiceState.PROCESSING
        _voiceErrorMessage.value = null
        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            _voiceState.value = VoiceState.IDLE
            _voicePartialText.value = ""
            closeModal()
            translate()
        }
    }

    fun onVoiceRecognitionError(message: String) {
        _voiceState.value = VoiceState.IDLE
        _voiceErrorMessage.value = message
    }

    fun resetVoiceState() {
        _voiceState.value = VoiceState.IDLE
        _voicePartialText.value = ""
        _voiceErrorMessage.value = null
    }

    fun simulateVoiceRecording() {
        // Fallback demo recording if no audio hardware is present
        viewModelScope.launch {
            _voiceState.value = VoiceState.RECORDING
            _voiceErrorMessage.value = null
            kotlinx.coroutines.delay(1500)
            val demoText = when (_sourceLanguage.value.code.lowercase()) {
                "ur" -> "السلام علیکم! میں آج آپ کے AI مترجم کے ذریعے گفتگو کرنا چاہتا ہوں۔"
                "es" -> "Hola, me gustaría reservar una mesa para dos personas esta noche."
                "fr" -> "Bonjour, je voudrais réserver une table pour deux personnes ce soir."
                "de" -> "Hallo, ich möchte heute Abend einen Tisch für zwei Personen reservieren."
                "ar" -> "مرحبا، أود حجز طاولة لشخصين هذا المساء."
                "hi" -> "नमस्ते, मैं आज रात दो लोगों के लिए एक टेबल बुक करना चाहता हूं।"
                else -> "Hello, I would like to book a table for two people tonight."
            }
            onVoiceTextCaptured(demoText)
        }
    }

    fun simulateCameraScan(scannedText: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            kotlinx.coroutines.delay(1000)
            _inputText.value = scannedText ?: "Welcome to the AI Multi-Language Translation Assistant. Scan any sign, document, or menu instantly."
            _isLoading.value = false
            closeModal()
            translate()
        }
    }

    private fun loadDailyWord() {
        viewModelScope.launch {
            _dailyWord.value = repository.getDailyWord()
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("Translated Text", text)
        clipboard.setPrimaryClip(clip)
    }

    fun speakText(text: String, langCode: String = "en") {
        if (text.isBlank()) return
        val locale = when (langCode.lowercase()) {
            "ur" -> Locale("ur", "PK")
            "ar" -> Locale("ar", "SA")
            "hi" -> Locale("hi", "IN")
            "zh" -> Locale.CHINESE
            "ja" -> Locale.JAPANESE
            "ko" -> Locale.KOREAN
            "de" -> Locale.GERMAN
            "fr" -> Locale.FRENCH
            "es" -> Locale("es", "ES")
            "it" -> Locale.ITALIAN
            else -> Locale.forLanguageTag(langCode)
        }
        val result = tts?.setLanguage(locale)
        if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
            tts?.language = Locale.US
        }
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "tts_id")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
