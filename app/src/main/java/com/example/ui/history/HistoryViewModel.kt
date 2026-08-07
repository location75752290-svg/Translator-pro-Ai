package com.example.ui.history

import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.speech.tts.TextToSpeech
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.TranslationEntity
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository
    private var tts: TextToSpeech? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(db.translationDao(), db.dictionaryDao(), db.tutorChatDao())
        
        tts = TextToSpeech(application) { status ->
            if (status != TextToSpeech.ERROR) {
                tts?.language = Locale.US
            }
        }
    }

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _filterFavoritesOnly = MutableStateFlow(false)
    val filterFavoritesOnly: StateFlow<Boolean> = _filterFavoritesOnly.asStateFlow()

    val historyList: StateFlow<List<TranslationEntity>> = combine(
        _searchQuery,
        _filterFavoritesOnly,
        repository.allHistory
    ) { query, favoritesOnly, allList ->
        var filtered = allList
        if (favoritesOnly) {
            filtered = filtered.filter { it.isFavorite }
        }
        if (query.isNotBlank()) {
            filtered = filtered.filter {
                it.sourceText.contains(query, ignoreCase = true) ||
                        it.translatedText.contains(query, ignoreCase = true) ||
                        it.sourceLanguage.contains(query, ignoreCase = true) ||
                        it.targetLanguage.contains(query, ignoreCase = true)
            }
        }
        filtered
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setFilterFavoritesOnly(favoritesOnly: Boolean) {
        _filterFavoritesOnly.value = favoritesOnly
    }

    fun toggleFavorite(item: TranslationEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, item.isFavorite)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repository.deleteHistoryItem(id)
        }
    }

    fun clearAllHistory() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun copyToClipboard(text: String) {
        val clipboard = getApplication<Application>().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("History Item", text)
        clipboard.setPrimaryClip(clip)
    }

    fun speakText(text: String, langCode: String) {
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
        tts?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "history_tts")
    }

    override fun onCleared() {
        super.onCleared()
        tts?.stop()
        tts?.shutdown()
    }
}
