package com.example.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.repository.TranslatorRepository
import kotlinx.coroutines.flow.*

data class UserStats(
    val totalTranslationsCount: Int = 0,
    val totalFavoritesCount: Int = 0,
    val savedWordsCount: Int = 0,
    val dailyStreakDays: Int = 5
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: TranslatorRepository

    init {
        val db = AppDatabase.getDatabase(application)
        repository = TranslatorRepository(db.translationDao(), db.dictionaryDao(), db.tutorChatDao())
    }

    val userStats: StateFlow<UserStats> = combine(
        repository.allHistory,
        repository.favoriteHistory,
        repository.savedDictionaryWords
    ) { allHistory, favs, savedWords ->
        UserStats(
            totalTranslationsCount = allHistory.size,
            totalFavoritesCount = favs.size,
            savedWordsCount = savedWords.size,
            dailyStreakDays = 5
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserStats())
}
