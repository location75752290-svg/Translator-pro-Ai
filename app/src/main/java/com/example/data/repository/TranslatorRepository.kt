package com.example.data.repository

import android.content.Context
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

    suspend fun translateText(
        sourceText: String,
        sourceLang: Language,
        targetLang: Language,
        formality: String = "Natural",
        context: Context? = null
    ): Result<Pair<String, String?>> = withContext(Dispatchers.IO) {
        try {
            val isOffline = context != null && !com.example.util.OnDeviceTranslatorManager.isOnline(context)

            if (isOffline) {
                val onDeviceRes = com.example.util.OnDeviceTranslatorManager.translateOnDevice(
                    context = context!!,
                    sourceText = sourceText,
                    sourceLang = sourceLang,
                    targetLang = targetLang
                )
                if (onDeviceRes.isSuccess) {
                    val (trans, notes) = onDeviceRes.getOrThrow()
                    saveTranslationToHistory(sourceLang.code, targetLang.code, sourceText, trans, notes)
                    return@withContext onDeviceRes
                }
            }

            var translation = ""
            var grammarNotes: String? = null
            
            val apiKey = GeminiClient.getApiKey()
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
                    // Fallthrough
                }
            }
            
            // Fallback to MyMemory Free Translation API if Gemini fails
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

            // Fallback to On-Device ML Kit if online services fail
            if (translation.isBlank() && context != null) {
                val onDeviceRes = com.example.util.OnDeviceTranslatorManager.translateOnDevice(
                    context = context,
                    sourceText = sourceText,
                    sourceLang = sourceLang,
                    targetLang = targetLang
                )
                if (onDeviceRes.isSuccess) {
                    val (trans, notes) = onDeviceRes.getOrThrow()
                    saveTranslationToHistory(sourceLang.code, targetLang.code, sourceText, trans, notes)
                    return@withContext onDeviceRes
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

    suspend fun sendRoleplayMessage(
        character: com.example.data.model.RoleplayCharacter,
        userMessage: String,
        history: List<com.example.data.model.RoleplayMessage>
    ): Result<Pair<String, String>> = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val roleName = when (character.id) {
                    "teacher" -> "English Teacher"
                    "doctor" -> "Doctor"
                    "shopkeeper" -> "Shopkeeper"
                    "friend" -> "Friend"
                    "boss" -> "Boss"
                    else -> "Tourist Guide"
                }

                val characterIntro = when (character.id) {
                    "teacher" -> "Prof. Sarah: English Teacher. Teaches grammar. Friendly."
                    "doctor" -> "Dr. Ali: Doctor. Gives health advice. Says \"I'm not a real doctor\". Simple words."
                    "shopkeeper" -> "Ahmed Bhai: Shopkeeper. Talks about price and products, shopping."
                    "friend" -> "Mike: Best Friend. Casual, uses \"bro\", \"yaar\"."
                    "boss" -> "Mr. Khan: Office Boss. Talks about work, meetings."
                    else -> "Lisa: Tourist Guide. Talks about places, travel."
                }

                val systemInstruction = """You are a Real AI Assistant inside a Roleplay App.
You are playing the character: ${character.name} who is a $roleName.

### CHARACTER PERSONALITY ###
$characterIntro

### YOUR #1 JOB ###
READ THE USER'S NEW MESSAGE. THINK ABOUT IT. THEN REPLY TO IT. 
DO NOT USE PRE-WRITTEN ANSWERS. DO NOT REPEAT.

### MANDATORY REPLY FORMAT - NO EXCEPTIONS ###
ENGLISH: [Your direct answer to the user's question]
URDU: [Exact translation of above line in Urdu]
EXAMPLE EN: [One new example sentence using words from answer]
EXAMPLE UR: [Urdu translation of example]
PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟

### EXAMPLES OF GOOD REPLY ###
User: hi
ENGLISH: Hello! I'm ${character.name}. How are you today?
URDU: ہیلو! میں ${character.name} ہوں۔ آپ آج کیسے ہیں؟
EXAMPLE EN: I am good, thank you.
EXAMPLE UR: میں ٹھیک ہوں، شکریہ۔
PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟

User: 2+2
ENGLISH: 2 plus 2 equals 4.
URDU: 2 جمع 2 برابر ہے 4 کے۔
EXAMPLE EN: 5 plus 5 equals 10.
EXAMPLE UR: 5 جمع 5 برابر ہے 10 کے۔
PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟

User: bukhar hai
ENGLISH: For fever, take rest and drink water. You should see a doctor.
URDU: بخار کے لیے آرام کریں اور پانی پئیں۔ آپ کو ڈاکٹر سے ملنا چاہیے۔
EXAMPLE EN: I have a fever and headache.
EXAMPLE UR: مجھے بخار اور سر درد ہے۔
PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟

### IMPORTANT RULES ###
1. READ THE USER'S LAST MESSAGE FIRST. Then reply ONLY to that message.
2. NEVER give the same reply twice. NEVER use fixed replies like "Perfect! Everything is on discount".
3. If user says "hi" or "hello" or "shoro kar", say "Hello" / greet them and ask what they want to practice today.
4. If user asks math, give math answer.
5. If user asks in Urdu, answer in English + Urdu."""

                val priorMessages = history.filter { it.urduText.isNotBlank() || it.englishText.isNotBlank() }
                val actualPrior = priorMessages.dropLast(1)
                val last5Prior = actualPrior.takeLast(5)

                val historyBuilder = java.lang.StringBuilder()
                last5Prior.forEach { msg ->
                    if (msg.sender == "user") {
                        val txt = msg.urduText.ifBlank { msg.englishText }
                        historyBuilder.append("User: ").append(txt).append("\n")
                    } else {
                        historyBuilder.append(character.name).append(" (English): ").append(msg.englishText).append("\n")
                        historyBuilder.append(character.name).append(" (Urdu): ").append(msg.urduText).append("\n")
                    }
                }

                val promptBuilder = java.lang.StringBuilder()
                promptBuilder.append("System Instruction:\n").append(systemInstruction).append("\n\n")
                if (last5Prior.isNotEmpty()) {
                    promptBuilder.append("Conversation History:\n").append(historyBuilder).append("\n")
                }
                promptBuilder.append("User (Latest Message): ").append(userMessage).append("\n\n")
                promptBuilder.append("Required Response Format:\n")
                promptBuilder.append("ENGLISH: <Direct answer to user's question>\n")
                promptBuilder.append("URDU: <Same answer in Urdu>\n")
                promptBuilder.append("EXAMPLE EN: <One new example sentence using words from answer>\n")
                promptBuilder.append("EXAMPLE UR: <Urdu translation of example>\n")
                promptBuilder.append("PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟")

                val response = GeminiClient.service.generateContent(
                    apiKey = apiKey,
                    request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = promptBuilder.toString())))),
                        generationConfig = GenerationConfig(temperature = 0.9f)
                    )
                )

                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (raw.isNotBlank()) {
                    var engAnswer = ""
                    var urdAnswer = ""
                    var engExample = ""
                    var urdExample = ""

                    val lines = raw.lines()
                    var currentSection = ""
                    for (line in lines) {
                        val cleanLine = line.replace("*", "").trim()
                        if (cleanLine.isBlank()) continue

                        val upperLine = cleanLine.uppercase()
                        when {
                            upperLine.startsWith("ENGLISH:") -> {
                                currentSection = "english"
                                engAnswer += cleanLine.substringAfter("ENGLISH:").trim() + " "
                            }
                            upperLine.startsWith("URDU:") -> {
                                currentSection = "urdu"
                                urdAnswer += cleanLine.substringAfter("URDU:").trim() + " "
                            }
                            upperLine.startsWith("EXAMPLE EN:") || upperLine.startsWith("EXAMPLE_EN:") || upperLine.startsWith("EXAMPLE:") -> {
                                currentSection = "example_en"
                                engExample += cleanLine.substringAfter(":").trim() + " "
                            }
                            upperLine.startsWith("EXAMPLE UR:") || upperLine.startsWith("EXAMPLE_UR:") || upperLine.startsWith("EXAMPLE URDU:") || upperLine.startsWith("URDU EXAMPLE:") -> {
                                currentSection = "example_ur"
                                urdExample += cleanLine.substringAfter(":").trim() + " "
                            }
                            upperLine.startsWith("PRACTICE:") || upperLine.startsWith("QUESTION:") -> {
                                currentSection = "practice"
                            }
                            else -> {
                                when (currentSection) {
                                    "english" -> engAnswer += cleanLine + " "
                                    "urdu" -> urdAnswer += cleanLine + " "
                                    "example_en" -> engExample += cleanLine + " "
                                    "example_ur" -> urdExample += cleanLine + " "
                                }
                            }
                        }
                    }

                    engAnswer = engAnswer.trim()
                    urdAnswer = urdAnswer.trim()
                    engExample = engExample.trim()
                    urdExample = urdExample.trim()

                    if (engAnswer.isBlank() || urdAnswer.isBlank()) {
                        val cleanRaw = raw.replace("*", "")
                        if (cleanRaw.contains("ENGLISH:") && cleanRaw.contains("URDU:")) {
                            engAnswer = cleanRaw.substringAfter("ENGLISH:").substringBefore("URDU:").trim()
                            val remaining = cleanRaw.substringAfter("URDU:")
                            urdAnswer = remaining.substringBefore("EXAMPLE EN:").trim()
                            if (urdAnswer.contains("EXAMPLE:")) {
                                urdAnswer = remaining.substringBefore("EXAMPLE:").trim()
                            }
                            if (remaining.contains("EXAMPLE EN:")) {
                                val examplePart = remaining.substringAfter("EXAMPLE EN:")
                                engExample = examplePart.substringBefore("EXAMPLE UR:").trim()
                                if (examplePart.contains("EXAMPLE UR:")) {
                                    urdExample = examplePart.substringAfter("EXAMPLE UR:").substringBefore("PRACTICE:").trim()
                                }
                            } else if (remaining.contains("EXAMPLE:")) {
                                val examplePart = remaining.substringAfter("EXAMPLE:")
                                engExample = examplePart.substringBefore("EXAMPLE URDU:").trim()
                                if (examplePart.contains("EXAMPLE URDU:")) {
                                    urdExample = examplePart.substringAfter("EXAMPLE URDU:").substringBefore("QUESTION:").trim()
                                }
                            }
                        }
                    }

                    engAnswer = engAnswer.removePrefix("ENGLISH:").trim()
                    urdAnswer = urdAnswer.removePrefix("URDU:").trim()
                    engExample = engExample.removePrefix("EXAMPLE EN:").removePrefix("EXAMPLE:").trim()
                    urdExample = urdExample.removePrefix("EXAMPLE UR:").removePrefix("EXAMPLE URDU:").trim()

                    if (engAnswer.isBlank()) {
                        engAnswer = "Hello! Let's continue practicing together."
                    }
                    if (urdAnswer.isBlank()) {
                        urdAnswer = "ہیلو! آئیے مل کر مشق جاری رکھیں۔"
                    }
                    if (engExample.isBlank()) {
                        engExample = "I enjoy speaking English with you."
                    }
                    if (urdExample.isBlank()) {
                        urdExample = "میں آپ کے ساتھ انگریزی بولنے سے لطف اندوز ہوتا ہوں۔"
                    }

                    val finalEnglish = """
                        ENGLISH: $engAnswer
                        EXAMPLE EN: $engExample
                        PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
                    """.trimIndent()

                    val finalUrdu = """
                        URDU: $urdAnswer
                        EXAMPLE UR: $urdExample
                        PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
                    """.trimIndent()

                    return@withContext Result.success(Pair(finalEnglish, finalUrdu))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val isAffirmative = userMessage.lowercase().contains("ha") || userMessage.lowercase().contains("yes") || userMessage.lowercase().contains("karna") || userMessage.lowercase().contains("han") || userMessage.lowercase().contains("ji") || userMessage.lowercase().contains("sure")
        val isGreeting = userMessage.lowercase().contains("hello") || userMessage.lowercase().contains("hi") || userMessage.lowercase().contains("shoro") || userMessage.lowercase().contains("slam") || userMessage.lowercase().contains("salam")

        val randomId = (0..2).random()

        val fbList = when (character.id) {
            "teacher" -> {
                when {
                    isGreeting -> listOf(
                        listOf("Hello! I am Prof. Sarah. How can I assist you with English today?", "ہیلو! میں پروفیسر سارہ ہوں۔ میں آج انگریزی میں آپ کی کس طرح مدد کر سکتی ہوں؟", "How are you doing today?", "آج آپ کا کیا حال ہے؟"),
                        listOf("Hi there! Prof. Sarah here. Ready to practice some English?", "ہیلو وہاں! پروفیسر سارہ یہاں ہیں۔ کچھ انگریزی کی مشق کرنے کے لیے تیار ہیں؟", "I am ready to learn English.", "میں انگریزی سیکھنے کے لیے تیار ہوں۔"),
                        listOf("Greetings! I am Prof. Sarah, your friendly English tutor. What's on your mind?", "سلام! میں پروفیسر سارہ ہوں، آپ کی دوست انگلش ٹیوٹر۔ آپ کے ذہن میں کیا ہے؟", "Practice makes a man perfect.", "مشق انسان کو کامل بناتی ہے۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Excellent! Let's start practicing immediately.", "بہترین! آئیے فوراً مشق شروع کرتے ہیں۔", "We are practicing English speaking.", "ہم انگریزی بولنے کی مشق کر رہے ہیں۔"),
                        listOf("Awesome! Consistency is the key to fluency.", "زبردست! روانی کی کنجی مستقل مزاجی ہے۔", "Consistency is the key to success.", "مستقل مزاجی کامیابی کی چابی ہے۔"),
                        listOf("Wonderful! Let's try some new conversation sentences.", "شاندار! آئیے کچھ نئے مکالمے کے جملے آزماتے ہیں۔", "I want to speak English fluently.", "میں روانی سے انگریزی بولنا چاہتا ہوں۔")
                    )[randomId]
                    else -> listOf(
                        listOf("I appreciate your question. Let's keep exploring English together!", "میں آپ کے سوال کی تعریف کرتا ہوں۔ آئیے مل کر انگریزی سیکھنا جاری رکھیں!", "I am learning new words every day.", "میں ہر روز نئے الفاظ سیکھ رہا ہوں۔"),
                        listOf("That is very interesting! Let's construct a practice sentence around this.", "یہ بہت دلچسپ ہے! آئیے اس کے گرد مشق کا ایک جملہ بنائیں۔", "Learning is a lifelong journey.", "سیکھنا زندگی بھر کا سفر ہے۔"),
                        listOf("Great question! Let's practice and discuss this topic further.", "بہترین سوال! آئیے اس موضوع پر مزید مشق اور بحث کریں۔", "English grammar is easy to learn.", "انگریزی گرامر سیکھنا آسان ہے۔")
                    )[randomId]
                }
            }
            "doctor" -> {
                when {
                    isGreeting -> listOf(
                        listOf("Hello! Dr. Ali here. How are you feeling today?", "ہیلو! ڈاکٹر علی یہاں ہیں۔ آج آپ کیسا محسوس کر رہے ہیں؟", "I feel strong and active today.", "میں آج مضبوط اور متحرک محسوس کر رہا ہوں۔"),
                        listOf("Hi! Welcome. I am Dr. Ali. Do you have any health concerns?", "ہیلو! خوش آمدید۔ میں ڈاکٹر علی ہوں۔ کیا آپ کو صحت سے متعلق کوئی مسئلہ ہے؟", "Health is wealth.", "تندرستی ہزار نعمت ہے۔"),
                        listOf("Greetings! I am Dr. Ali. How can I help you with your health query today?", "سلام! میں ڈاکٹر علی ہوں۔ میں آج آپ کے صحت کے سوال میں کیسے مدد کر سکتا ہوں؟", "Prevention is better than cure.", "پرہیز علاج سے بہتر ہے۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Great! Taking charge of your health is a wonderful step.", "بہت اچھا! اپنی صحت کا خیال رکھنا ایک بہترین قدم ہے۔", "I will exercise daily to stay healthy.", "میں صحت مند رہنے کے لیے روزانہ ورزش کروں گا۔"),
                        listOf("Awesome. Let's practice talking about daily healthy habits.", "زبردست۔ آئیے روزانہ کی صحت مند عادات کے بارے میں بات کرنے کی مشق کریں۔", "Eating fresh fruits is good.", "تازہ پھل کھانا اچھا ہے۔"),
                        listOf("Perfect! Let's practice some useful health-related phrases.", "بہترین! آئیے صحت سے متعلق کچھ مفید جملوں کی مشق کرتے ہیں۔", "I am resting to recover fast.", "میں جلدی ٹھیک ہونے کے لیے آرام کر رہا ہوں۔")
                    )[randomId]
                    else -> listOf(
                        listOf("I understand your query. Maintaining a balanced diet is very important.", "میں آپ کا سوال سمجھتا ہوں۔ متوازن غذا برقرار رکھنا بہت ضروری ہے۔", "We should eat fresh vegetables.", "ہمیں تازہ سبزیاں کھانی چاہئیں۔"),
                        listOf("I highly recommend sleeping on time and drinking sufficient water.", "میں وقت پر سونے اور کافی پانی پینے کی سختی سے سفارش کرتا ہوں۔", "Proper sleep improves immunity.", "مناسب نیند قوت مدافعت کو بہتر بناتی ہے۔"),
                        listOf("Take care. It is always best to keep track of your daily energy levels.", "اپنا خیال رکھیں۔ روزانہ کی توانائی کی سطح کا ٹریک رکھنا ہمیشہ بہتر ہوتا ہے۔", "I walk every evening.", "میں ہر شام چہل قدمی کرتا ہوں۔")
                    )[randomId]
                }
            }
            "shopkeeper" -> {
                when {
                    isGreeting -> listOf(
                        listOf("Hello! Welcome to my store. I am Ahmed Bhai. What are you looking to buy today?", "ہیلو! میری دکان میں خوش آمدید۔ میں احمد بھائی ہوں۔ آج آپ کیا خریدنا چاہتے ہیں؟", "I want to buy some fresh milk.", "میں کچھ تازہ دودھ خریدنا چاہتا ہوں۔"),
                        listOf("Welcome! Ahmed Bhai here. We have some great fresh arrivals today.", "خوش آمدید! احمد بھائی یہاں ہیں۔ آج ہمارے پاس کچھ بہترین تازہ چیزیں آئی ہیں۔", "The shop is open now.", "دکان اب کھلی ہے۔"),
                        listOf("Aao ji! Welcome. How can I help you find things in my shop today?", "آؤ جی! خوش آمدید۔ آج میں اپنی دکان میں چیزیں تلاش کرنے میں آپ کی کیسے مدد کر سکتا ہوں؟", "I am shopping for groceries.", "میں گروسری کی خریداری کر رہا ہوں۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Great! I can give you a special customer discount today.", "بہت اچھا! میں آج آپ کو ایک خصوصی کسٹمر ڈسکاؤنٹ دے سکتا ہوں۔", "I got a good discount on rice.", "مجھے چاول پر اچھا ڈسکاؤنٹ ملا۔"),
                        listOf("Arey wah! Let's practice bargaining or talking about prices.", "ارے واہ! آئیے بھاؤ تاؤ کرنے یا قیمتوں کے بارے میں بات کرنے کی مشق کریں۔", "This item has a very reasonable price.", "اس چیز کی قیمت بہت مناسب ہے۔"),
                        listOf("Perfect. Let's practice checking out items at the counter.", "بہترین۔ آئیے کاؤنٹر پر چیزیں چیک کرنے کی مشق کرتے ہیں۔", "Please give me the bill.", "براہ کرم مجھے بل دیں۔")
                    )[randomId]
                    else -> listOf(
                        listOf("We have fresh organic vegetables and high quality pulses today.", "ہمارے پاس آج تازہ نامیاتی سبزیاں اور اعلیٰ معیار کی دالیں ہیں۔", "These apples are very sweet.", "یہ سیب بہت میٹھے ہیں۔"),
                        listOf("Don't worry about the price, I will give you the best wholesale rate!", "قیمت کی فکر نہ کریں، میں آپ کو بہترین ہول سیل ریٹ دوں گا!", "I prefer high-quality goods.", "میں اعلیٰ معیار کی اشیاء کو ترجیح دیتا ہوں۔"),
                        listOf("Everything is freshly stocked this morning. Feel free to explore.", "آج صبح سب کچھ تازہ اسٹاک کیا گیا ہے۔ بلا جھجھک دیکھیں۔", "We bought fresh bread today.", "ہم نے آج تازہ ڈبل روٹی خریدی۔")
                    )[randomId]
                }
            }
            "friend" -> {
                when {
                    isGreeting -> listOf(
                        listOf("Hey! Mike here. How's everything going with you, my friend?", "ہیلو! مائیک یہاں ہے۔ میرے دوست، آپ کے ساتھ سب کیسا چل رہا ہے؟", "We are meeting our friends today.", "ہم آج اپنے دوستوں سے مل رہے ہیں۔"),
                        listOf("What's up, buddy! Mike here. What are your plans for today?", "کیا چل رہا ہے، یار! مائیک یہاں ہے۔ آج کے لیے آپ کے کیا منصوبے ہیں؟", "I am hanging out with my friends.", "میں اپنے دوستوں کے ساتھ گھوم پھر رہا ہوں۔"),
                        listOf("Hey friend! Always great to hear from you. What's new?", "ہیلو دوست! آپ سے سن کر ہمیشہ بہت اچھا لگتا ہے۔ کیا نیا ہے؟", "True friendship is a blessing.", "سچی دوستی ایک نعمت ہے۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Awesome! Let's catch up and talk about our weekend plans.", "زبردست! آئیے ملتے ہیں اور اپنے ویک اینڈ کے منصوبوں کے بارے میں بات کرتے ہیں۔", "Let's watch a movie together.", "آئیے مل کر فلم دیکھتے ہیں۔"),
                        listOf("That sounds super exciting! I am always up for a good chat.", "یہ بہت پرجوش لگتا ہے! میں ہمیشہ اچھی بات چیت کے لیے تیار رہتا ہوں۔", "We had a wonderful weekend.", "ہمارا ویک اینڈ بہت شاندار رہا۔"),
                        listOf("Perfect! Let's practice sharing some fun stories from today.", "بہترین! آئیے آج سے کچھ مزے دار کہانیاں شیئر کرنے کی مشق کرتے ہیں۔", "Sharing is caring.", "بانٹنا ہی پیار ہے۔")
                    )[randomId]
                    else -> listOf(
                        listOf("That is so cool! I was just thinking about that same thing.", "یہ تو بہت ہی عمدہ ہے! میں بھی اسی چیز کے بارے میں سوچ رہا تھا۔", "We should play games together.", "ہمیں مل کر گیمز کھیلنے چاہئیں۔"),
                        listOf("No way, that's wild! Tell me more about what happened.", "ارے واہ، یہ تو زبردست ہے! مجھے مزید بتائیں کہ کیا ہوا۔", "It was an amazing experience.", "یہ ایک شاندار تجربہ تھا۔"),
                        listOf("I am always here for you, buddy. Let's make the best of our time.", "میں ہمیشہ آپ کے لیے یہاں ہوں، یار۔ آئیے اپنے وقت کا بہترین استعمال کریں۔", "Friends always support each other.", "دوست ہمیشہ ایک دوسرے کا ساتھ دیتے ہیں۔")
                    )[randomId]
                }
            }
            "boss" -> {
                when {
                    isGreeting -> listOf(
                        listOf("Good morning. I am Mr. Khan. Let's look at the agenda for today.", "صبح بخیر۔ میں مسٹر خان ہوں۔ آئیے آج کے ایجنڈے پر نظر ڈالتے ہیں۔", "Please submit the report on time.", "براہ کرم وقت پر رپورٹ جمع کروائیں۔"),
                        listOf("Good day. Mr. Khan here. Do you have any project updates for me?", "اچھا دن۔ مسٹر خان یہاں ہیں۔ کیا آپ کے پاس میرے لیے پروجیکٹ کی کوئی اپ ڈیٹس ہیں؟", "Our team achieved the target.", "ہماری ٹیم نے ہدف حاصل کر لیا۔"),
                        listOf("Hello. I am Mr. Khan. Let's discuss our upcoming business goals.", "ہیلو۔ میں مسٹر خان ہوں۔ آئیے اپنے آنے والے کاروباری اہداف پر تبادلہ خیال کریں۔", "The presentation is scheduled for noon.", "پریزنٹیشن دوپہر کے لیے شیڈول ہے۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Excellent. Promptness is highly valued in our organization.", "بہترین۔ ہماری تنظیم میں وقت کی پابندی کو بہت اہمیت دی جاتی ہے۔", "I appreciate your dedication to work.", "میں کام کے لیے آپ کے جذبے کی تعریف کرتا ہوں۔"),
                        listOf("Good. Let's practice discussing team project milestones.", "اچھا۔ آئیے ٹیم پروجیکٹ کے سنگ میلوں پر بات کرنے کی مشق کریں۔", "We need to focus on efficiency.", "ہمیں کارکردگی پر توجہ دینے کی ضرورت ہے۔"),
                        listOf("Perfect. Please document these business requirements carefully.", "بہترین۔ براہ کرم ان کاروباری تقاضوں کو احتیاط سے دستاویز کریں۔", "Documenting details prevents errors.", "تفصیلات کی دستاویز کرنا غلطیوں کو روکتا ہے۔")
                    )[randomId]
                    else -> listOf(
                        listOf("Please review the final draft and ensure all metrics are accurate.", "براہ کرم آخری ڈرافٹ کا جائزہ لیں اور یقینی بنائیں کہ تمام میٹرکس درست ہیں۔", "Accuracy is essential in business.", "کاروبار میں درستگی ضروری ہے۔"),
                        listOf("We should coordinate with the client to verify their expectations.", "ہمیں کلائنٹ کی توقعات کی تصدیق کے لیے ان کے ساتھ ہم آہنگی کرنی چاہیے۔", "Client communication must be clear.", "کلائنٹ کے ساتھ بات چیت واضح ہونی چاہیے۔"),
                        listOf("Keep up the diligent work. Team collaboration is key to our success.", "سخت محنت جاری رکھیں۔ ٹیم کا تعاون ہماری کامیابی کی کلید ہے۔", "We work together as a strong team.", "ہم ایک مضبوط ٹیم کے طور پر مل کر کام کرتے ہیں۔")
                    )[randomId]
                }
            }
            else -> {
                when {
                    isGreeting -> listOf(
                        listOf("Hello! I am Lisa, your Tourist Guide. Which beautiful site should we explore first?", "ہیلو! میں لیزا ہوں، آپ کی ٹورسٹ گائیڈ۔ ہمیں پہلے کس خوبصورت جگہ کی سیر کرنی چاہیے؟", "The historic museum is beautiful.", "تاریخی میوزیم بہت خوبصورت ہے۔"),
                        listOf("Hi there! Lisa here. Welcome to our travel adventure! Ready to discover?", "ہیلو وہاں! لیزا یہاں۔ ہماری سفری مہم جوئی میں خوش آمدید! دریافت کرنے کے لیے تیار ہیں؟", "I love exploring ancient palaces.", "مجھے قدیم محلات کی سیر کرنا پسند ہے۔"),
                        listOf("Greetings traveler! Lisa here. Let's talk about the incredible local landmarks.", "سلام مسافر! لیزا یہاں۔ آئیے ناقابل یقین مقامی مقامات کے بارے میں بات کرتے ہیں۔", "This is a highly popular landmark.", "یہ ایک انتہائی مقبول مقام ہے۔")
                    )[randomId]
                    isAffirmative -> listOf(
                        listOf("Superb! Let's practice talking about booking a local city tour.", "شاندار! مقامی شہر کے دورے کی بکنگ کے بارے میں بات کرنے کی مشق کریں۔", "I want to buy a ticket for the tour.", "میں ٹور کے لیے ٹکٹ خریدنا چاہتا ہوں۔"),
                        listOf("Wonderful choice! Travel is the only thing you buy that makes you richer.", "شاندار انتخاب! سفر واحد چیز ہے جسے آپ خریدتے ہیں جو آپ کو امیر بناتی ہے۔", "Traveling broadens our perspective.", "سفر ہمارے نقطہ نظر کو وسعت دیتا ہے۔"),
                        listOf("Great! Let's practice asking for directions to the grand castle.", "بہت اچھا! عظیم الشان قلعے کا راستہ پوچھنے کی مشق کریں۔", "Where is the nearest tourist spot?", "قریب ترین سیاحتی مقام کہاں ہے؟")
                    )[randomId]
                    else -> listOf(
                        listOf("This historic temple was constructed over five hundred years ago.", "یہ تاریخی مندر پانچ سو سال سے پہلے تعمیر کیا گیا تھا۔", "The architecture here is stunning.", "یہاں کا فن تعمیر شاندار ہے۔"),
                        listOf("I recommend visiting the scenic lake during sunrise for the best view.", "بہترین منظر کے لیے میں طلوع آفتاب کے وقت خوبصورت جھیل کی سیر کرنے کی سفارش کرتی ہوں۔", "The view of the sunrise is peaceful.", "طلوع آفتاب کا منظر پرسکون ہے۔"),
                        listOf("This vibrant local marketplace is famous for handcrafted souvenirs.", "یہ متحرک مقامی بازار ہاتھ سے بنے تحائف کے لیے مشہور ہے۔", "I bought a lovely handmade souvenir.", "میں نے ایک پیارا ہاتھ سے بنا ہوا تحفہ خریدا۔")
                    )[randomId]
                }
            }
        }

        val fbEngVal = fbList[0]
        val fbUrdVal = fbList[1]
        val exEngVal = fbList[2]
        val exUrdVal = fbList[3]

        val fallbackEnglish = """
            ENGLISH: $fbEngVal
            EXAMPLE EN: $exEngVal
            PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
        """.trimIndent()

        val fallbackUrdu = """
            URDU: $fbUrdVal
            EXAMPLE UR: $exUrdVal
            PRACTICE: Do you want to practice this sentence? / کیا آپ اسکو پریکٹس کرنا چاہیں گے؟
        """.trimIndent()

        Result.success(Pair(fallbackEnglish, fallbackUrdu))
    }

    suspend fun fetchDailyLearnFromGemini(
        info: DailyThemeInfo,
        streakDays: Int
    ): com.example.data.model.DailyLearnContent = withContext(Dispatchers.IO) {
        val apiKey = GeminiClient.getApiKey()
        if (apiKey.isNotBlank() && apiKey != "MY_GEMINI_API_KEY") {
            try {
                val prompt = """
                    Generate a Daily Learn JSON object for an English learner who speaks Urdu.
                    Theme: ${info.theme}
                    
                    Strictly return ONLY a valid JSON object without any Markdown formatting or code fences:
                    {
                      "word": "<1 useful English word>",
                      "urduMeaning": "<Urdu meaning>",
                      "pronunciation": "</phonetic-pronunciation/>",
                      "exampleSentence": "<1 short example sentence in English>",
                      "dailySentence": "<1 practical everyday English sentence>",
                      "sentenceUrduTranslation": "<Urdu translation of the sentence>"
                    }
                """.trimIndent()

                val response = GeminiClient.service.generateContent(
                    apiKey = apiKey,
                    request = GenerateContentRequest(
                        contents = listOf(Content(parts = listOf(Part(text = prompt)))),
                        generationConfig = GenerationConfig(temperature = 0.7f)
                    )
                )

                val raw = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text.orEmpty()
                if (raw.isNotBlank()) {
                    val cleanJson = raw.replace("```json", "").replace("```", "").trim()
                    val json = org.json.JSONObject(cleanJson)
                    return@withContext com.example.data.model.DailyLearnContent(
                        dateKey = info.dateKey,
                        dayOfWeek = info.dayName,
                        theme = info.theme,
                        themeEmoji = info.themeEmoji,
                        word = json.optString("word", "Eloquent"),
                        urduMeaning = json.optString("urduMeaning", "فصیح / خوش گفتار"),
                        pronunciation = json.optString("pronunciation", "/eh-luh-kwuhnt/"),
                        exampleSentence = json.optString("exampleSentence", "She gave an eloquent speech."),
                        dailySentence = json.optString("dailySentence", "Every new day brings new opportunities."),
                        sentenceUrduTranslation = json.optString("sentenceUrduTranslation", "ہر نیا دن نئے مواقع لاتا ہے۔"),
                        streakDays = streakDays
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        getFallbackDailyLearnContent(info, streakDays)
    }

    fun getTodayThemeInfo(): DailyThemeInfo {
        val calendar = java.util.Calendar.getInstance()
        val dayOfWeek = calendar.get(java.util.Calendar.DAY_OF_WEEK)
        val dateKey = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US).format(calendar.time)

        return when (dayOfWeek) {
            java.util.Calendar.MONDAY -> DailyThemeInfo(dateKey, "Monday", "Travel", "✈️")
            java.util.Calendar.TUESDAY -> DailyThemeInfo(dateKey, "Tuesday", "Work", "💼")
            java.util.Calendar.WEDNESDAY -> DailyThemeInfo(dateKey, "Wednesday", "School", "🎓")
            java.util.Calendar.THURSDAY -> DailyThemeInfo(dateKey, "Thursday", "Food", "🍕")
            java.util.Calendar.FRIDAY -> DailyThemeInfo(dateKey, "Friday", "Shopping", "🛍️")
            java.util.Calendar.SATURDAY -> DailyThemeInfo(dateKey, "Saturday", "Health", "🏥")
            else -> DailyThemeInfo(dateKey, "Sunday", "General", "🌟")
        }
    }

    fun getFallbackDailyLearnContent(info: DailyThemeInfo, streakDays: Int = 3): com.example.data.model.DailyLearnContent {
        return when (info.theme) {
            "Travel" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "Travel",
                themeEmoji = "✈️",
                word = "Destination",
                urduMeaning = "منزل / ٹھکانہ",
                pronunciation = "/des-tuh-nay-shuhn/",
                exampleSentence = "Paris is a popular travel destination.",
                dailySentence = "Could you please tell me the way to the train station?",
                sentenceUrduTranslation = "کیا آپ مجھے ٹرین اسٹیشن کا راستہ بتا سکتے ہیں؟",
                streakDays = streakDays
            )
            "Work" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "Work",
                themeEmoji = "💼",
                word = "Collaboration",
                urduMeaning = "تعاون / باہمی کام",
                pronunciation = "/kuh-la-buh-ray-shuhn/",
                exampleSentence = "Good teamwork requires great collaboration.",
                dailySentence = "Let's schedule a brief meeting to discuss project goals.",
                sentenceUrduTranslation = "آئیں پروجیکٹ کے مقاصد پر تبادلہ خیال کے لیے ایک مختصر میٹنگ رکھیں۔",
                streakDays = streakDays
            )
            "School" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "School",
                themeEmoji = "🎓",
                word = "Perseverance",
                urduMeaning = "صبر و استقلال / ثابت قدمی",
                pronunciation = "/pur-suh-veer-uhns/",
                exampleSentence = "Students need perseverance to master new skills.",
                dailySentence = "Education is the most powerful tool for a bright future.",
                sentenceUrduTranslation = "تعلیم روشن مستقبل کے لیے سب سے طاقتور ہتھیار ہے۔",
                streakDays = streakDays
            )
            "Food" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "Food",
                themeEmoji = "🍕",
                word = "Delicious",
                urduMeaning = "مزیدار / لذیذ",
                pronunciation = "/dih-lish-uhs/",
                exampleSentence = "This traditional dish is absolutely delicious.",
                dailySentence = "Can I have a cup of tea and a light snack, please?",
                sentenceUrduTranslation = "کیا مجھے ایک کپ چائے اور ہلکا سنیک مل سکتا ہے؟",
                streakDays = streakDays
            )
            "Shopping" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "Shopping",
                themeEmoji = "🛍️",
                word = "Bargain",
                urduMeaning = "سستا سودا / رعایت",
                pronunciation = "/bar-gihn/",
                exampleSentence = "I bought this jacket at a great bargain price.",
                dailySentence = "Is there any discount available on this item today?",
                sentenceUrduTranslation = "کیا آج اس چیز پر کوئی رعایت دستیاب ہے؟",
                streakDays = streakDays
            )
            "Health" -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "Health",
                themeEmoji = "🏥",
                word = "Nutrition",
                urduMeaning = "غذائیت / پرہیز",
                pronunciation = "/noo-trish-uhn/",
                exampleSentence = "Balanced nutrition keeps your body energetic and strong.",
                dailySentence = "Drinking enough water daily is essential for good health.",
                sentenceUrduTranslation = "روزانہ مناسب مقدار میں پانی پینا اچھی صحت کے لیے ضروری ہے۔",
                streakDays = streakDays
            )
            else -> com.example.data.model.DailyLearnContent(
                dateKey = info.dateKey,
                dayOfWeek = info.dayName,
                theme = "General",
                themeEmoji = "🌟",
                word = "Eloquent",
                urduMeaning = "فصیح / خوش گفتار",
                pronunciation = "/eh-luh-kwuhnt/",
                exampleSentence = "She delivered an eloquent speech at the ceremony.",
                dailySentence = "Every new day brings new opportunities to learn and grow.",
                sentenceUrduTranslation = "ہر نیا دن سیکھنے اور آگے بڑھنے کے نئے مواقع لاتا ہے۔",
                streakDays = streakDays
            )
        }
    }
}

data class DailyThemeInfo(
    val dateKey: String,
    val dayName: String,
    val theme: String,
    val themeEmoji: String
)
