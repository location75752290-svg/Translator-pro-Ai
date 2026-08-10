package com.example.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.data.model.DailyLearnContent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "daily_learn_prefs")

class DailyLearnDataStore(private val context: Context) {

    companion object {
        val KEY_DATE = stringPreferencesKey("date_key")
        val KEY_DAY_OF_WEEK = stringPreferencesKey("day_of_week")
        val KEY_THEME = stringPreferencesKey("theme")
        val KEY_THEME_EMOJI = stringPreferencesKey("theme_emoji")
        val KEY_WORD = stringPreferencesKey("word")
        val KEY_URDU_MEANING = stringPreferencesKey("urdu_meaning")
        val KEY_PRONUNCIATION = stringPreferencesKey("pronunciation")
        val KEY_EXAMPLE_SENTENCE = stringPreferencesKey("example_sentence")
        val KEY_DAILY_SENTENCE = stringPreferencesKey("daily_sentence")
        val KEY_SENTENCE_URDU_TRANSLATION = stringPreferencesKey("sentence_urdu_translation")
        val KEY_STREAK_DAYS = intPreferencesKey("streak_days")
        val KEY_LAST_PRACTICED_DATE = stringPreferencesKey("last_practiced_date")
    }

    val dailyLearnFlow: Flow<DailyLearnContent?> = context.dataStore.data.map { prefs ->
        val dateKey = prefs[KEY_DATE] ?: return@map null
        DailyLearnContent(
            dateKey = dateKey,
            dayOfWeek = prefs[KEY_DAY_OF_WEEK] ?: "Today",
            theme = prefs[KEY_THEME] ?: "General",
            themeEmoji = prefs[KEY_THEME_EMOJI] ?: "🌟",
            word = prefs[KEY_WORD] ?: "",
            urduMeaning = prefs[KEY_URDU_MEANING] ?: "",
            pronunciation = prefs[KEY_PRONUNCIATION] ?: "",
            exampleSentence = prefs[KEY_EXAMPLE_SENTENCE] ?: "",
            dailySentence = prefs[KEY_DAILY_SENTENCE] ?: "",
            sentenceUrduTranslation = prefs[KEY_SENTENCE_URDU_TRANSLATION] ?: "",
            streakDays = prefs[KEY_STREAK_DAYS] ?: 3
        )
    }

    suspend fun saveDailyLearnContent(content: DailyLearnContent) {
        context.dataStore.edit { prefs ->
            prefs[KEY_DATE] = content.dateKey
            prefs[KEY_DAY_OF_WEEK] = content.dayOfWeek
            prefs[KEY_THEME] = content.theme
            prefs[KEY_THEME_EMOJI] = content.themeEmoji
            prefs[KEY_WORD] = content.word
            prefs[KEY_URDU_MEANING] = content.urduMeaning
            prefs[KEY_PRONUNCIATION] = content.pronunciation
            prefs[KEY_EXAMPLE_SENTENCE] = content.exampleSentence
            prefs[KEY_DAILY_SENTENCE] = content.dailySentence
            prefs[KEY_SENTENCE_URDU_TRANSLATION] = content.sentenceUrduTranslation
            prefs[KEY_STREAK_DAYS] = content.streakDays
        }
    }

    suspend fun incrementStreak(todayKey: String) {
        context.dataStore.edit { prefs ->
            val lastDate = prefs[KEY_LAST_PRACTICED_DATE]
            if (lastDate != todayKey) {
                val currentStreak = prefs[KEY_STREAK_DAYS] ?: 3
                prefs[KEY_STREAK_DAYS] = currentStreak + 1
                prefs[KEY_LAST_PRACTICED_DATE] = todayKey
            }
        }
    }
}
