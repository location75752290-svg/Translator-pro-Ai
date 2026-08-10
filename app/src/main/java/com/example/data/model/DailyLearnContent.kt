package com.example.data.model

data class DailyLearnContent(
    val dateKey: String,
    val dayOfWeek: String,
    val theme: String,
    val themeEmoji: String,
    val word: String,
    val urduMeaning: String,
    val pronunciation: String,
    val exampleSentence: String,
    val dailySentence: String,
    val sentenceUrduTranslation: String,
    val streakDays: Int = 3
)
