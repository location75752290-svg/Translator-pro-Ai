package com.example.data.model

data class DictionaryWord(
    val word: String,
    val phonetic: String,
    val partOfSpeech: String,
    val definition: String,
    val exampleSentence: String,
    val synonyms: List<String> = emptyList(),
    val antonyms: List<String> = emptyList(),
    val cefrLevel: String = "B2"
)

data class TutorScenario(
    val id: String,
    val title: String,
    val description: String,
    val iconName: String,
    val systemPrompt: String,
    val initialMessage: String
) {
    companion object {
        val scenarios = listOf(
            TutorScenario(
                id = "travel",
                title = "Travel & Navigation",
                description = "Practice asking for directions, hotel check-in, and airport chats",
                iconName = "Flight",
                systemPrompt = "You are an encouraging language tutor helping the user practice travel conversations. Respond in simple, clear language, correct any subtle grammar mistakes, and ask follow-up questions.",
                initialMessage = "Hello! I'm your travel assistant. Where are you planning to travel next?"
            ),
            TutorScenario(
                id = "interview",
                title = "Job Interview Prep",
                description = "Master professional English Q&A, elevator pitches, and business terminology",
                iconName = "Work",
                systemPrompt = "You are a professional hiring manager conducting a friendly job interview practice. Give constructive feedback on vocabulary, tone, and grammar.",
                initialMessage = "Welcome to our practice interview! Tell me a little bit about yourself and your background."
            ),
            TutorScenario(
                id = "cafe",
                title = "Ordering at a Cafe",
                description = "Order coffee, ask about allergies, and request the bill politely",
                iconName = "LocalCoffee",
                systemPrompt = "You are a friendly barista at a bustling coffee shop. Take the user's order and help them practice polite cafe phrases.",
                initialMessage = "Hi there! Welcome to AI Cafe. What can I get started for you today?"
            ),
            TutorScenario(
                id = "casual",
                title = "Casual Chit-Chat",
                description = "Discuss hobbies, movies, weather, and weekend plans naturally",
                iconName = "Chat",
                systemPrompt = "You are a friendly language partner chatting casually about everyday topics. Encourage fluent conversation and point out natural idioms.",
                initialMessage = "Hey! How is your day going so far? Do you have any fun plans for the weekend?"
            )
        )
    }
}
