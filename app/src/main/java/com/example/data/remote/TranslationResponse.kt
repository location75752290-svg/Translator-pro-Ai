package com.example.data.remote

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TranslationResponse(
    val translation: String,
    val grammarNotes: String? = null
)
