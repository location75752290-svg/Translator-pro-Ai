package com.example.ui.dictionary

import android.app.Application
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.DictionaryEntity
import com.example.data.model.DictionaryWord
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository
    private var tts: TextToSpeech? = null

    private val _searchQuery = MutableStateFlow("Resilience")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _currentWord = MutableStateFlow<DictionaryWord?>(null)
    val currentWord: StateFlow<DictionaryWord?> = _currentWord.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _isSaved = MutableStateFlow(false)
    val isSaved: StateFlow<Boolean> = _isSaved.asStateFlow()

    private val _showSavedTab = MutableStateFlow(false)
    val showSavedTab: StateFlow<Boolean> = _showSavedTab.asStateFlow()

    val savedWords: StateFlow<List<DictionaryEntity>> by lazy {
        repository.savedDictionaryWords
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

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
        
        lookupWord("Resilience")
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setShowSavedTab(showSaved: Boolean) {
        _showSavedTab.value = showSaved
    }

    fun lookupWord(word: String) {
        val target = word.trim()
        if (target.isBlank()) return

        _searchQuery.value = target
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.lookupDictionaryWord(target)
            result.onSuccess { dictWord ->
                _currentWord.value = dictWord
                _isSaved.value = repository.isWordSaved(dictWord.word)
            }
            _isLoading.value = false
        }
    }

    fun toggleSaveCurrentWord() {
        val word = _currentWord.value ?: return
        viewModelScope.launch {
            if (_isSaved.value) {
                repository.removeWordFromDictionary(word.word)
                _isSaved.value = false
            } else {
                repository.saveWordToDictionary(word)
                _isSaved.value = true
            }
        }
    }

    fun speakWord(word: String) {
        tts?.language = Locale.US
        tts?.speak(word, TextToSpeech.QUEUE_FLUSH, null, "dict_tts")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
