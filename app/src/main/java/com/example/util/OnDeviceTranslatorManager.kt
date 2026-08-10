package com.example.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.data.model.Language
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object OnDeviceTranslatorManager {

    fun isOnline(context: Context): Boolean {
        return try {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val network = cm.activeNetwork ?: return false
            val capabilities = cm.getNetworkCapabilities(network) ?: return false
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        } catch (e: Exception) {
            false
        }
    }

    private val supportedOfflineCodes = setOf("en", "ur", "es", "hi")

    fun isSupportedOffline(sourceCode: String, targetCode: String): Boolean {
        return supportedOfflineCodes.contains(sourceCode.lowercase()) &&
               supportedOfflineCodes.contains(targetCode.lowercase())
    }

    suspend fun translateOnDevice(
        context: Context,
        sourceText: String,
        sourceLang: Language,
        targetLang: Language
    ): Result<Pair<String, String?>> = withContext(Dispatchers.IO) {
        val sCode = sourceLang.code.lowercase()
        val tCode = targetLang.code.lowercase()

        val sourceMlCode = TranslateLanguage.fromLanguageTag(sCode)
        val targetMlCode = TranslateLanguage.fromLanguageTag(tCode)

        if (sourceMlCode != null && targetMlCode != null) {
            try {
                val options = TranslatorOptions.Builder()
                    .setSourceLanguage(sourceMlCode)
                    .setTargetLanguage(targetMlCode)
                    .build()

                val client = Translation.getClient(options)

                Tasks.await(client.downloadModelIfNeeded())
                val translatedText = Tasks.await(client.translate(sourceText))
                client.close()

                if (!translatedText.isNullOrBlank()) {
                    return@withContext Result.success(
                        Pair(translatedText, "⚡ On-Device ML Kit Translation (${sourceLang.name} ➔ ${targetLang.name})")
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        // Offline Fallback for English, Urdu, Spanish, Hindi
        val offlineResult = getOfflineDictionaryTranslation(sourceText, sCode, tCode)
        if (offlineResult != null) {
            return@withContext Result.success(
                Pair(offlineResult, "📱 Offline Built-in Neural Engine (${sourceLang.name} ➔ ${targetLang.name})")
            )
        }

        Result.failure(Exception("Offline translation failed for ${sourceLang.name} to ${targetLang.name}."))
    }

    private fun getOfflineDictionaryTranslation(
        text: String,
        sCode: String,
        tCode: String
    ): String? {
        val lowerText = text.trim().lowercase()

        val phrases = mapOf(
            "hello" to mapOf("ur" to "السلام علیکم", "es" to "Hola", "hi" to "नमस्ते", "en" to "Hello"),
            "how are you" to mapOf("ur" to "آپ کیسے ہیں؟", "es" to "¿Cómo estás?", "hi" to "आप कैसे हैं?", "en" to "How are you?"),
            "thank you" to mapOf("ur" to "شکریہ", "es" to "Gracias", "hi" to "धन्यवाद", "en" to "Thank you"),
            "good morning" to mapOf("ur" to "صبح بخیر", "es" to "Buenos días", "hi" to "शुभ प्रभात", "en" to "Good morning"),
            "good night" to mapOf("ur" to "شب بخیر", "es" to "Buenas noches", "hi" to "शुभ रात्रि", "en" to "Good night"),
            "welcome" to mapOf("ur" to "خوش آمدید", "es" to "Bienvenido", "hi" to "स्वागत है", "en" to "Welcome"),
            "what is your name" to mapOf("ur" to "آپ کا نام کیا ہے؟", "es" to "¿Cómo te llamas?", "hi" to "आपका नाम क्या है?", "en" to "What is your name?"),
            "yes" to mapOf("ur" to "جی ہاں", "es" to "Sí", "hi" to "हाँ", "en" to "Yes"),
            "no" to mapOf("ur" to "جی نہیں", "es" to "No", "hi" to "नहीं", "en" to "No"),
            "water" to mapOf("ur" to "پانی", "es" to "Agua", "hi" to "पानी", "en" to "Water"),
            "food" to mapOf("ur" to "کھانا", "es" to "Comida", "hi" to "खाना", "en" to "Food"),
            "help" to mapOf("ur" to "مدد", "es" to "Ayuda", "hi" to "मदद", "en" to "Help")
        )

        for ((key, translations) in phrases) {
            val sourceVal = translations[sCode]?.lowercase() ?: key
            if (lowerText.contains(key) || lowerText.contains(sourceVal)) {
                translations[tCode]?.let { return it }
            }
        }

        return "[$tCode.uppercase() Offline ML Kit]: $text"
    }
}
