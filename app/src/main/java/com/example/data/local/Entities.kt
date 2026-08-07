package com.example.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "translation_history")
data class TranslationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val sourceLanguage: String,
    val targetLanguage: String,
    val sourceText: String,
    val translatedText: String,
    val grammarNotes: String? = null,
    val timestamp: Long = System.currentTimeMillis(),
    val isFavorite: Boolean = false
)

@Entity(tableName = "saved_dictionary")
data class DictionaryEntity(
    @PrimaryKey val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val exampleSentence: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tutor_chat_history")
data class TutorChatEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val scenarioId: String,
    val sender: String, // "user" or "ai"
    val messageText: String,
    val grammarCorrection: String? = null,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "tense_progress")
data class TenseProgressEntity(
    @PrimaryKey val tenseId: String,
    val isCompleted: Boolean = false,
    val scorePercentage: Int = 0,
    val lastPracticedTimestamp: Long = System.currentTimeMillis()
)
