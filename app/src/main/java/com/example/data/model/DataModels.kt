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

data class PartnerQuestion(
    val qId: Int,
    val questionEn: String,
    val questionUr: String,
    val expectedKeywords: List<String> = emptyList(),
    val difficulty: String = "easy"
) {
    val q_id: Int get() = qId
    val question_en: String get() = questionEn
    val question_ur: String get() = questionUr
    val expected_keywords: List<String> get() = expectedKeywords
}

data class PartnerCheckAnswerResult(
    val isCorrect: Boolean,
    val score: Int,
    val userAnswerCorrectedEn: String,
    val userAnswerUr: String,
    val feedbackEn: String,
    val feedbackUr: String,
    val correctionEn: String = "",
    val correctionUr: String = "",
    val betterVersionEn: String = ""
) {
    val is_correct: Boolean get() = isCorrect
    val user_answer_corrected_en: String get() = userAnswerCorrectedEn
    val user_answer_ur: String get() = userAnswerUr
    val feedback_en: String get() = feedbackEn
    val feedback_ur: String get() = feedbackUr
    val correction_en: String get() = correctionEn
    val correction_ur: String get() = correctionUr
    val better_version_en: String get() = betterVersionEn
}

data class PartnerFinalReportResult(
    val totalScore: Int,
    val totalQuestions: Int,
    val correctAnswers: Int,
    val fluency: String,
    val grammar: String,
    val confidence: String,
    val strengths: List<String>,
    val weaknesses: List<String>,
    val finalAdviceEn: String,
    val finalAdviceUr: String,
    val nextTopicSuggestion: String
) {
    val total_score: Int get() = totalScore
    val total_questions: Int get() = totalQuestions
    val correct_answers: Int get() = correctAnswers
    val final_advice_en: String get() = finalAdviceEn
    val final_advice_ur: String get() = finalAdviceUr
    val next_topic_suggestion: String get() = nextTopicSuggestion
}

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
                title = "✈️ Travel & Navigation",
                description = "Directions, airport, hotel check-in",
                iconName = "Flight",
                systemPrompt = "You are an encouraging language tutor helping the user practice travel conversations. Respond in simple, clear language, correct any subtle grammar mistakes, and ask follow-up questions.",
                initialMessage = "Hello! I'm your AI Travel Guide. Where are you planning to travel next?"
            ),
            TutorScenario(
                id = "interview",
                title = "💼 Job Interview Prep",
                description = "Professional Q&A, elevator pitch",
                iconName = "Work",
                systemPrompt = "You are a professional hiring manager conducting a friendly job interview practice. Give constructive feedback on vocabulary, tone, and grammar.",
                initialMessage = "Welcome to our practice interview! Tell me a little bit about yourself and your professional background."
            ),
            TutorScenario(
                id = "cafe",
                title = "☕ Cafe & Restaurant Ordering",
                description = "Food orders, bill requests",
                iconName = "LocalCoffee",
                systemPrompt = "You are a friendly barista at a bustling coffee shop. Take the user's order and help them practice polite dining phrases.",
                initialMessage = "Hi there! Welcome to AI Cafe. What hot or cold beverage can I get started for you today?"
            ),
            TutorScenario(
                id = "casual",
                title = "💬 Casual Chit-Chat & Hobbies",
                description = "Daily routines, weather",
                iconName = "Chat",
                systemPrompt = "You are a friendly language partner chatting casually about everyday topics. Encourage fluent conversation and explain natural idioms.",
                initialMessage = "Hey! How is your day going so far? Do you have any fun plans or hobbies for the weekend?"
            ),
            TutorScenario(
                id = "doctor",
                title = "🩺 Doctor & Health Visit",
                description = "Symptoms, prescriptions",
                iconName = "MedicalServices",
                systemPrompt = "You are a patient and caring medical doctor listening to symptoms. Help the user describe health issues clearly in English.",
                initialMessage = "Good day! I'm Dr. Alex. What symptoms or health concerns bring you into the clinic today?"
            ),
            TutorScenario(
                id = "shopping",
                title = "🛍️ Shopping & Mall Spree",
                description = "Sizes, discounts, returns",
                iconName = "ShoppingBag",
                systemPrompt = "You are a helpful retail assistant at a clothing store. Help the user find sizes, discuss discounts, and make purchases.",
                initialMessage = "Welcome to Fashion Hub! Are you looking for anything specific today, like jackets, jeans, or shoes?"
            ),
            TutorScenario(
                id = "hotel",
                title = "🏨 Hotel Check-In & Services",
                description = "Room service, amenities",
                iconName = "Hotel",
                systemPrompt = "You are a courteous front desk receptionist at a 5-star hotel. Assist guests with check-in and hotel service requests.",
                initialMessage = "Good evening! Welcome to Grand Palace Hotel. Do you have a reservation under your name tonight?"
            ),
            TutorScenario(
                id = "networking",
                title = "🤝 Making Friends & Networking",
                description = "Introductions, contacts",
                iconName = "People",
                systemPrompt = "You are an outgoing attendee at an international conference. Practice warm introductions and networking conversations.",
                initialMessage = "Hi! Mind if I join you at this table? I'm Alex from the technology team. What brought you to today's event?"
            ),
            TutorScenario(
                id = "directions",
                title = "🚨 Directions & Emergency Help",
                description = "Lost items, urgent help",
                iconName = "SupportAgent",
                systemPrompt = "You are a friendly local police officer or transit assistant helping someone who needs directions or support.",
                initialMessage = "Excuse me! You look a bit lost. How can I help you find your destination or landmark today?"
            ),
            TutorScenario(
                id = "office",
                title = "💻 Office & Project Meetings",
                description = "Status updates, feedback",
                iconName = "Computer",
                systemPrompt = "You are a senior team lead conducting a weekly project sync. Encourage professional workplace communication and team updates.",
                initialMessage = "Welcome to our team sync! Could you give us a quick status update on the project tasks you're currently working on?"
            )
        )
    }
}
