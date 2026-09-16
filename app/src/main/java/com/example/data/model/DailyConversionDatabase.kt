package com.example.data.model

data class ConversionSentence(
    val id: Int,
    val english: String,
    val urdu: String,
    val romanUrdu: String
)

data class ConversionTopic(
    val id: Int,
    val name: String,
    val urduName: String,
    val iconName: String,
    val description: String,
    val sentences: List<ConversionSentence>
)

object DailyConversionDatabase {

    val topics: List<ConversionTopic> by lazy {
        DailyConversionPart1.topics +
        DailyConversionPart2.topics +
        DailyConversionPart3.topics +
        DailyConversionPart4.topics +
        DailyConversionPart5.topics
    }

    fun getTopicById(id: Int): ConversionTopic? {
        return topics.find { it.id == id }
    }

    fun getSentenceCount(): Int {
        return topics.sumOf { it.sentences.size }
    }
}
