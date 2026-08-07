package com.example.util

object SpeechLanguageUtils {
    /**
     * Maps a language code (e.g. "ur", "en", "es", "ar") to a precise BCP-47 language tag
     * required by Android SpeechRecognizer for high-accuracy speech-to-text.
     */
    fun getBcp47LanguageTag(langCode: String): String {
        return when (langCode.lowercase().trim()) {
            "ur" -> "ur-PK" // Urdu (Pakistan)
            "en" -> "en-US" // English (United States)
            "es" -> "es-ES" // Spanish (Spain)
            "fr" -> "fr-FR" // French (France)
            "de" -> "de-DE" // German (Germany)
            "ar" -> "ar-SA" // Arabic (Saudi Arabia)
            "hi" -> "hi-IN" // Hindi (India)
            "zh" -> "zh-CN" // Chinese Mandarin (China)
            "ja" -> "ja-JP" // Japanese (Japan)
            "ko" -> "ko-KR" // Korean (South Korea)
            "it" -> "it-IT" // Italian (Italy)
            "pt" -> "pt-BR" // Portuguese (Brazil)
            "ru" -> "ru-RU" // Russian (Russia)
            "tr" -> "tr-TR" // Turkish (Turkey)
            "id" -> "id-ID" // Indonesian (Indonesia)
            "vi" -> "vi-VN" // Vietnamese (Vietnam)
            "nl" -> "nl-NL" // Dutch (Netherlands)
            "fa" -> "fa-IR" // Persian (Iran)
            "ps" -> "ps-AF" // Pashto (Afghanistan)
            "pa" -> "pa-PK" // Punjabi (Pakistan)
            "bn" -> "bn-BD" // Bengali (Bangladesh)
            "ta" -> "ta-IN" // Tamil (India)
            "te" -> "te-IN" // Telugu (India)
            "mr" -> "mr-IN" // Marathi (India)
            "gu" -> "gu-IN" // Gujarati (India)
            "ml" -> "ml-IN" // Malayalam (India)
            "kn" -> "kn-IN" // Kannada (India)
            "sd" -> "sd-PK" // Sindhi (Pakistan)
            "ku" -> "ku-IQ" // Kurdish (Iraq)
            "el" -> "el-GR" // Greek (Greece)
            "he" -> "he-IL" // Hebrew (Israel)
            "sv" -> "sv-SE" // Swedish (Sweden)
            "no" -> "nb-NO" // Norwegian (Norway)
            "da" -> "da-DK" // Danish (Denmark)
            "fi" -> "fi-FI" // Finnish (Finland)
            "pl" -> "pl-PL" // Polish (Poland)
            "cs" -> "cs-CZ" // Czech (Czechia)
            "hu" -> "hu-HU" // Hungarian (Hungary)
            "ro" -> "ro-RO" // Romanian (Romania)
            "uk" -> "uk-UA" // Ukrainian (Ukraine)
            "th" -> "th-TH" // Thai (Thailand)
            "ms" -> "ms-MY" // Malay (Malaysia)
            "tl" -> "fil-PH" // Filipino / Tagalog
            "sw" -> "sw-KE" // Swahili (Kenya)
            "af" -> "af-ZA" // Afrikaans
            "am" -> "am-ET" // Amharic
            "hy" -> "hy-AM" // Armenian
            "az" -> "az-AZ" // Azerbaijani
            "bg" -> "bg-BG" // Bulgarian
            "hr" -> "hr-HR" // Croatian
            "et" -> "et-EE" // Estonian
            "ka" -> "ka-GE" // Georgian
            "is" -> "is-IS" // Icelandic
            "kk" -> "kk-KZ" // Kazakh
            "km" -> "km-KH" // Khmer
            "ky" -> "ky-KG" // Kyrgyz
            "lo" -> "lo-LA" // Lao
            "lv" -> "lv-LV" // Latvian
            "lt" -> "lt-LT" // Lithuanian
            "mk" -> "mk-MK" // Macedonian
            "mn" -> "mn-MN" // Mongolian
            "my" -> "my-MM" // Burmese
            "ne" -> "ne-NP" // Nepali
            "sr" -> "sr-RS" // Serbian
            "sk" -> "sk-SK" // Slovak
            "sl" -> "sl-SI" // Slovenian
            "so" -> "so-SO" // Somali
            "tg" -> "tg-TJ" // Tajik
            "uz" -> "uz-UZ" // Uzbek
            "cy" -> "cy-GB" // Welsh
            "si" -> "si-LK" // Sinhala
            else -> {
                if (langCode.contains("-")) langCode else "$langCode-${langCode.uppercase()}"
            }
        }
    }
}
