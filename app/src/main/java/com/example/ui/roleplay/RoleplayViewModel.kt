package com.example.ui.roleplay

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.RoleplayCharacter
import com.example.data.model.RoleplayMessage
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Locale

class RoleplayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository

    private val _selectedCharacter = MutableStateFlow<RoleplayCharacter?>(null)
    val selectedCharacter: StateFlow<RoleplayCharacter?> = _selectedCharacter.asStateFlow()

    private val _chatMessages = MutableStateFlow<List<RoleplayMessage>>(emptyList())
    val chatMessages: StateFlow<List<RoleplayMessage>> = _chatMessages.asStateFlow()

    private val _messageInput = MutableStateFlow("")
    val messageInput: StateFlow<String> = _messageInput.asStateFlow()

    private val _isSending = MutableStateFlow(false)
    val isSending: StateFlow<Boolean> = _isSending.asStateFlow()

    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()

    private var tts: TextToSpeech? = null
    private var isTtsReady = false

    init {
        val database = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(
            translationDao = database.translationDao(),
            dictionaryDao = database.dictionaryDao(),
            tutorChatDao = database.tutorChatDao()
        )

        tts = TextToSpeech(application) { status ->
            if (status != TextToSpeech.ERROR) {
                val result = tts?.setLanguage(Locale.US)
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    isTtsReady = true
                } else {
                    tts?.setLanguage(Locale.ENGLISH)
                    isTtsReady = true
                }
            }
        }
    }

    fun selectCharacter(character: RoleplayCharacter) {
        _selectedCharacter.value = character
        val initialMsg = RoleplayMessage(
            characterId = character.id,
            sender = "character",
            englishText = character.initialEnglish,
            urduText = character.initialUrdu
        )
        _chatMessages.value = listOf(initialMsg)
        speakEnglish(character.initialEnglish)
    }

    fun backToCharacterSelection() {
        _selectedCharacter.value = null
        _chatMessages.value = emptyList()
        tts?.stop()
    }

    fun setMessageInput(text: String) {
        _messageInput.value = text
    }

    fun sendMessage(textOverride: String? = null) {
        val text = textOverride ?: _messageInput.value.trim()
        val character = _selectedCharacter.value ?: return
        if (text.isBlank()) return

        _messageInput.value = ""

        val userMessage = RoleplayMessage(
            characterId = character.id,
            sender = "user",
            englishText = "",
            urduText = text
        )

        val updatedList = _chatMessages.value + userMessage
        _chatMessages.value = updatedList

        viewModelScope.launch {
            _isSending.value = true
            
            val query = text.lowercase(Locale.ROOT).trim()
            val isPracticeRequest = query.contains("practice") || query.contains("next") || query.contains("پریکٹس") || query.contains("اگلا")

            if (isPracticeRequest) {
                val prefs = getApplication<Application>().getSharedPreferences("practice_prefs", android.content.Context.MODE_PRIVATE)
                val currentIndex = prefs.getInt("current_practice_index", 0)

                val sentence = com.example.data.model.PracticeSentences.list[currentIndex]

                val finalEnglish = """
                    ENGLISH: ${sentence.english}
                    EXAMPLE EN: ${sentence.exampleEn}
                    PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
                """.trimIndent()

                val finalUrdu = """
                    URDU: ${sentence.urdu}
                    EXAMPLE UR: ${sentence.exampleUr}
                    PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
                """.trimIndent()

                val charMessage = RoleplayMessage(
                    characterId = character.id,
                    sender = "character",
                    englishText = finalEnglish,
                    urduText = finalUrdu
                )

                val nextIndex = (currentIndex + 1) % com.example.data.model.PracticeSentences.list.size
                prefs.edit().putInt("current_practice_index", nextIndex).apply()

                _isSending.value = false
                _chatMessages.value = _chatMessages.value + charMessage
                speakEnglish(finalEnglish)
            } else {
                val result = repository.sendRoleplayMessage(character, text, updatedList)
                _isSending.value = false

                result.onSuccess { (english, urdu) ->
                    val charMessage = RoleplayMessage(
                        characterId = character.id,
                        sender = "character",
                        englishText = english,
                        urduText = urdu
                    )
                    _chatMessages.value = _chatMessages.value + charMessage
                    speakEnglish(english)
                }.onFailure {
                    val errorMessage = RoleplayMessage(
                        characterId = character.id,
                        sender = "character",
                        englishText = "Sorry, there was a connection problem.",
                        urduText = "معذرت، کنیکشن کا مسئلہ پیش آیا ہے۔"
                    )
                    _chatMessages.value = _chatMessages.value + errorMessage
                }
            }
        }
    }

    fun clearCurrentChat() {
        val character = _selectedCharacter.value ?: return
        val initialMsg = RoleplayMessage(
            characterId = character.id,
            sender = "character",
            englishText = character.initialEnglish,
            urduText = character.initialUrdu
        )
        _chatMessages.value = listOf(initialMsg)
        speakEnglish(character.initialEnglish)
    }

    fun setListening(listening: Boolean) {
        _isListening.value = listening
    }

    fun speakEnglish(text: String) {
        if (isTtsReady && text.isNotBlank()) {
            val cleanText = text
                .replace("ENGLISH:", "")
                .replace("EXAMPLE EN:", "Example:")
                .replace("EXAMPLE:", "Example:")
                .replace("PRACTICE:", "")
                .replace("QUESTION:", "")
                .replace("Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟", "")
                .trim()
            tts?.speak(cleanText, TextToSpeech.QUEUE_FLUSH, null, "roleplay_tts_${System.currentTimeMillis()}")
        }
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
