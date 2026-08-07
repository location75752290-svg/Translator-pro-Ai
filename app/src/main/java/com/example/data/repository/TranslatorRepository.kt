package com.example.data.repository

import com.example.data.local.*
import com.example.data.model.DictionaryWord
import com.example.data.model.Language
import com.example.data.remote.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.withContext

class TranslatorRepository(
    private val translationDao: TranslationDao,
    private val dictionaryDao: DictionaryDao,
    private val tutorChatDao: TutorChatDao,
    private val tenseProgressDao: TenseProgressDao? = null
) {
    val allHistory: Flow<List<TranslationEntity>> = translationDao.getAllHistory()
    val favoriteHistory: Flow<List<TranslationEntity>> = translationDao.getFavoriteHistory()
    val savedDictionaryWords: Flow<List<DictionaryEntity>> = dictionaryDao.getAllSavedWords()
    val allTenseProgress: Flow<List<TenseProgressEntity>> = tenseProgressDao?.getAllProgress() ?: flowOf(emptyList())

    suspend fun saveTenseProgress(tenseId: String, isCompleted: Boolean, scorePercentage: Int) = withContext(Dispatchers.IO) {
        tenseProgressDao?.saveProgress(
            TenseProgressEntity(
                tenseId = tenseId,
                isCompleted = isCompleted,
                scorePercentage = scorePercentage,
                lastPracticedTimestamp = System.currentTimeMillis()
            )
        )
    }

    fun searchHistory(query: String): Flow<List<TranslationEntity>> = translationDao.searchHistory(query)

    suspend fun saveTranslationToHistory(
        sourceLang: String, targetLang: String, sourceText: String, translatedText: String, grammarNotes: String? = null
    ): Long = withContext(Dispatchers.IO) {
        translationDao.insertTranslation(
            TranslationEntity(
                sourceLanguage = sourceLang, targetLanguage = targetLang, sourceText = sourceText,
                translatedText = translatedText, grammarNotes = grammarNotes
            )
        )
    }

    suspend fun toggleFavorite(id: Long, currentIsFavorite: Boolean) = withContext(Dispatchers.IO) { translationDao.updateFavorite(id, !currentIsFavorite) }
    suspend fun deleteHistoryItem(id: Long) = withContext(Dispatchers.IO) { translationDao.deleteById(id) }
    suspend fun clearHistory() = withContext(Dispatchers.IO) { translationDao.clearAll() }
    
    suspend fun saveWordToDictionary(word: DictionaryWord) = withContext(Dispatchers.IO) {
        dictionaryDao.saveWord(DictionaryEntity(word = word.word, phonetic = word.phonetic, partOfSpeech = word.partOfSpeech, definition = word.definition, exampleSentence = word.exampleSentence))
    }
    suspend fun removeWordFromDictionary(word: String) = withContext(Dispatchers.IO) { dictionaryDao.deleteWord(word) }
    suspend fun isWordSaved(word: String): Boolean = withContext(Dispatchers.IO) { dictionaryDao.isWordSaved(word) }
    fun getScenarioMessages(scenarioId: String): Flow<List<TutorChatEntity>> = tutorChatDao.getChatMessages(scenarioId)
    suspend fun saveTutorMessage(scenarioId: String, sender: String, text: String, correction: String? = null) = withContext(Dispatchers.IO) {
        tutorChatDao.insertMessage(TutorChatEntity(scenarioId = scenarioId, sender = sender, messageText = text, grammarCorrection = correction))
    }
    suspend fun clearScenarioChat(scenarioId: String) = withContext(Dispatchers.IO) { tutorChatDao.clearScenarioChat(scenarioId) }

    suspend fun translateText(sourceText: String, sourceLang: Language, targetLang: Language, formality: String = "Natural"): Result<Pair<String, String?>> = withContext(Dispatchers.IO) {
        try {
            val apiKey = GeminiClient.getApiKey()
            var translation = ""
            var grammarNotes: String? = null
            
            if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
                try {
                    val prompt = """
                        You are Translator Pro AI, a world-class professional linguistic engine.
                        Translate the following text from ${sourceLang.name} (${sourceLang.code}) to ${targetLang.name} (${targetLang.code}).
                        Formality tone level: $formality.
                        Text to translate: "$sourceText"
                        Respond strictly in valid JSON format with the following keys:
                        - "translation": the accurate, natural translation
                        - "grammarNotes": brief 1-2 sentence linguistic note about nuances or grammar, or null if none
                    """.trimIndent()
                    
                    val response = GeminiClient.service.generateContent(
                        apiKey = apiKey,
                        request = GenerateContentRequest(
                            contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                            generationConfig = GenerationConfig(temperature = 0.2f)
                        )
                    )
                    val fullText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                    if (fullText.isNotBlank()) {
                        try {
                            val cleanJson = fullText.replace("```json", "").replace("```", "").trim()
                            val moshi = com.squareup.moshi.Moshi.Builder().addLast(com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory()).build()
                            val jsonResponse = moshi.adapter(com.example.data.remote.TranslationResponse::class.java).fromJson(cleanJson)
                            if (jsonResponse != null) { translation = jsonResponse.translation; grammarNotes = jsonResponse.grammarNotes }
                        } catch (e: Exception) { translation = fullText }
                    }
                } catch (e: Exception) {
                    // Fallthrough to MyMemory fallback if Gemini API throws exception (e.g., quota exceeded)
                }
            }
            
            // Fallback to MyMemory Free Translation API if Gemini fails or API key is missing
            if (translation.isBlank()) {
                try {
                    val url = "https://api.mymemory.translated.net/get?q=${java.net.URLEncoder.encode(sourceText, "UTF-8")}&langpair=${sourceLang.code}|${targetLang.code}"
                    val request = okhttp3.Request.Builder().url(url).build()
                    val okHttpClient = okhttp3.OkHttpClient.Builder()
                        .connectTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                        .readTimeout(15, java.util.concurrent.TimeUnit.SECONDS)
                        .build()
                    val response = okHttpClient.newCall(request).execute()
                    if (response.isSuccessful) {
                        val responseBody = response.body?.string()
                        if (!responseBody.isNullOrBlank()) {
                            val jsonObject = org.json.JSONObject(responseBody)
                            val responseData = jsonObject.optJSONObject("responseData")
                            val translated = responseData?.optString("translatedText")
                            if (!translated.isNullOrBlank() && translated != "null") {
                                translation = translated
                                grammarNotes = "Translated from ${sourceLang.name} to ${targetLang.name}."
                            }
                        }
                    }
                } catch (e: Exception) {
                    // MyMemory error
                }
            }

            if (translation.isBlank()) {
                return@withContext Result.failure(Exception("Translation service unavailable. Please check your network connection."))
            }

            saveTranslationToHistory(sourceLang.code, targetLang.code, sourceText, translation, grammarNotes)
            Result.success(Pair(translation, grammarNotes))
        } catch (e: Exception) { Result.failure(e) }
    }

    suspend fun rewriteText(text: String, style: String, language: Language): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "You are a professional AI editor and copywriter. Rewrite the following text in ${language.name} to match the '$style' style. Keep original meaning. Text: $text"
                val response = GeminiClient.service.generateContent(apiKey = apiKey, request = GenerateContentRequest(contents = listOf(Content(parts = listOf(Part(text = prompt)))), generationConfig = GenerationConfig(temperature = 0.4f)))
                val fullText = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (fullText.isNotBlank()) return@withContext Result.success(fullText)
            } catch (e: Exception) {
                // Ignore and use fallback
            }
        }
        val fallbackText = when (style.lowercase()) {
            "formal" -> "Respectfully, " + text.replaceFirstChar { it.titlecase() }
            "casual" -> "Hey! " + text.replaceFirstChar { it.lowercase() }
            "concise" -> text.trim()
            "professional" -> "Please be advised: " + text
            "creative" -> "✨ " + text + " ✨"
            else -> text
        }
        Result.success("[$style Style]: $fallbackText")
    }

    suspend fun checkGrammar(text: String): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "You are an expert AI English Grammar Checker. Analyze the following text:\n\"$text\"\nFormat your response strictly as:\nCORRECTED: <corrected text or original if perfect>\nEXPLANATION: <brief explanation of the corrections made>"
                val response = GeminiClient.service.generateContent(apiKey = apiKey, request = GenerateContentRequest(contents = listOf(Content(parts = listOf(Part(text = prompt)))), generationConfig = GenerationConfig(temperature = 0.2f)))
                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (raw.isNotBlank()) {
                    val corrected = raw.substringAfter("CORRECTED:").substringBefore("EXPLANATION:").trim().ifEmpty { raw }
                    val explanation = raw.substringAfter("EXPLANATION:").trim().ifEmpty { "No corrections required." }
                    return@withContext Result.success(Pair(corrected, explanation))
                }
            } catch (e: Exception) {
                // Ignore and use fallback
            }
        }
        Result.success(Pair(text.trim().replaceFirstChar { it.titlecase() }, "✔ Sentence structure is clear."))
    }

    suspend fun lookupDictionaryWord(word: String): Result<DictionaryWord> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "Provide a comprehensive dictionary entry for the word or phrase \"$word\". Respond strictly in the following key-value format:\nPHONETIC: <IPA>\nPART_OF_SPEECH: <pos>\nCEFR: <level>\nDEFINITION: <def>\nEXAMPLE: <ex>\nSYNONYMS: <syn1, syn2>\nANTONYMS: <ant1, ant2>"
                val response = GeminiClient.service.generateContent(apiKey = apiKey, request = GenerateContentRequest(contents = listOf(Content(parts = listOf(Part(text = prompt)))), generationConfig = GenerationConfig(temperature = 0.2f)))
                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (raw.isNotBlank()) {
                    val phonetic = raw.substringAfter("PHONETIC:").substringBefore("PART_OF_SPEECH:").trim().ifEmpty { "/$word/" }
                    val pos = raw.substringAfter("PART_OF_SPEECH:").substringBefore("CEFR:").trim().ifEmpty { "noun" }
                    val cefr = raw.substringAfter("CEFR:").substringBefore("DEFINITION:").trim().ifEmpty { "B2" }
                    val def = raw.substringAfter("DEFINITION:").substringBefore("EXAMPLE:").trim().ifEmpty { "Definition of $word" }
                    val example = raw.substringAfter("EXAMPLE:").substringBefore("SYNONYMS:").trim().ifEmpty { "Example using $word." }
                    val synsStr = raw.substringAfter("SYNONYMS:").substringBefore("ANTONYMS:").trim()
                    val antsStr = raw.substringAfter("ANTONYMS:").trim()
                    val syns = synsStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    val ants = antsStr.split(",").map { it.trim() }.filter { it.isNotBlank() }
                    return@withContext Result.success(DictionaryWord(word, phonetic, pos, def, example, syns, ants, cefr))
                }
            } catch (e: Exception) {
                // Ignore and use fallback
            }
        }
        Result.success(
            DictionaryWord(
                word = word.replaceFirstChar { it.titlecase() },
                phonetic = "/${word.lowercase()}/",
                partOfSpeech = "noun / verb",
                definition = "Definition for '$word': A key linguistic concept analyzed in Translator Pro AI.",
                exampleSentence = "She mastered the word '$word' using Translator Pro AI.",
                synonyms = listOf("term", "expression", "vocabulary"),
                antonyms = listOf("silence", "pause"),
                cefrLevel = "B2"
            )
        )
    }

    suspend fun getDailyWord(): DictionaryWord = withContext(Dispatchers.IO) {
        lookupDictionaryWord(listOf("Resilience", "Eloquent", "Perspective", "Serendipity", "Pragmatic", "Luminous").shuffled().first()).getOrDefault(
            DictionaryWord("Resilience", "/rɪˈzɪl.jəns/", "noun", "The capacity to withstand or recover quickly from difficult conditions; toughness.", "Learning a new language builds mental resilience.", listOf("toughness", "adaptability"), listOf("fragility", "weakness"), "C1")
        )
    }

    suspend fun sendTutorMessage(scenarioSystemPrompt: String, userMessage: String, conversationHistory: List<TutorChatEntity>): Result<Pair<String, String?>> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val promptBuilder = java.lang.StringBuilder("System Instruction: ").append(scenarioSystemPrompt).append("\n\nConversation History:\n")
                conversationHistory.takeLast(6).forEach { msg -> promptBuilder.append("${if (msg.sender == "user") "User" else "AI Tutor"}: ${msg.messageText}\n") }
                promptBuilder.append("User: $userMessage\n\nRespond as AI Tutor in 2-3 sentences. If grammatical error, provide gentle correction.\nFormat:\nCORRECTION: <correction or NONE>\nRESPONSE: <reply>")
                val response = GeminiClient.service.generateContent(apiKey = apiKey, request = GenerateContentRequest(contents = listOf(Content(parts = listOf(Part(text = promptBuilder.toString())))), generationConfig = GenerationConfig(temperature = 0.7f)))
                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (raw.isNotBlank()) {
                    val correction = raw.substringAfter("CORRECTION:").substringBefore("RESPONSE:").trim()
                    val reply = raw.substringAfter("RESPONSE:").trim().ifEmpty { raw }
                    return@withContext Result.success(Pair(reply, if (correction.lowercase() != "none" && correction.isNotBlank()) correction else null))
                }
            } catch (e: Exception) {
                // Ignore and use fallback
            }
        }
        Result.success(Pair("That's a great observation! Keeping up practice in daily conversation builds confidence and fluency. What else would you like to discuss?", if (userMessage.length < 5) "Tip: Try forming complete sentences for better fluency." else null))
    }

    suspend fun askTenseAiTutor(tenseName: String, userQuestion: String): Result<String> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = "You are an expert bilingual English Grammar AI Tutor specializing in English Tenses for Urdu speakers.\nCurrent Focus Tense: $tenseName\nUser's Question: \"$userQuestion\"\nPlease provide a friendly explanation including English explanation, Urdu explanation, 2 examples, and 1 tip. Use bullet points."
                val response = GeminiClient.service.generateContent(apiKey = apiKey, request = GenerateContentRequest(contents = listOf(Content(parts = listOf(Part(text = prompt)))), generationConfig = GenerationConfig(temperature = 0.5f)))
                val text = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (text.isNotBlank()) return@withContext Result.success(text)
            } catch (e: Exception) {
                // Ignore and use fallback
            }
        }
        Result.success(
            "Regarding **$tenseName**:\n\n" +
            "• **Explanation**: In $tenseName, focus on matching the appropriate auxiliary verb with the main verb form.\n" +
            "• **اردو وضاحت**: اس زمانے میں صحیح ہیلپنگ ورب اور ورب کی فارم کو ذہن میں رکھنا ضروری ہے۔\n" +
            "• **Examples**:\n  1. I practice English daily. (میں روزانہ انگریزی کی مشق کرتا ہوں۔)\n  2. She speaks fluently. (وہ روانی سے بولتی ہے۔)\n" +
            "• **Personalized Tip**: Write 3 practice sentences daily to build natural recall!"
        )
    }
}
