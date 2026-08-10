package com.example.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color as AndroidColor
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Shader
import android.os.Build
import android.os.Environment
import android.provider.MediaStore

object TranslationCardImageGenerator {

    fun generateCardBitmap(
        context: Context,
        appName: String = "Translator Pro AI",
        sourceLang: String,
        targetLang: String,
        originalText: String,
        translatedText: String
    ): Bitmap {
        val width = 1080
        val height = 1350
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)

        // Gradient Background
        val bgShader = LinearGradient(
            0f, 0f, width.toFloat(), height.toFloat(),
            intArrayOf(
                AndroidColor.parseColor("#0F172A"), // Dark Slate
                AndroidColor.parseColor("#1E1B4B"), // Indigo Navy
                AndroidColor.parseColor("#312E81")  // Deep Violet
            ),
            null,
            Shader.TileMode.CLAMP
        )
        val bgPaint = Paint().apply { shader = bgShader }
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), bgPaint)

        // Card Container
        val cardPaint = Paint().apply {
            color = AndroidColor.parseColor("#1E293B")
            style = Paint.Style.FILL
            isAntiAlias = true
        }
        val cardRect = RectF(60f, 80f, (width - 60).toFloat(), (height - 80).toFloat())
        canvas.drawRoundRect(cardRect, 44f, 44f, cardPaint)

        // Gradient Border
        val borderShader = LinearGradient(
            60f, 80f, (width - 60).toFloat(), (height - 80).toFloat(),
            intArrayOf(
                AndroidColor.parseColor("#6366F1"), // Indigo
                AndroidColor.parseColor("#818CF8"),
                AndroidColor.parseColor("#38BDF8")  // Cyan
            ),
            null,
            Shader.TileMode.CLAMP
        )
        val borderPaint = Paint().apply {
            shader = borderShader
            style = Paint.Style.STROKE
            strokeWidth = 6f
            isAntiAlias = true
        }
        canvas.drawRoundRect(cardRect, 44f, 44f, borderPaint)

        // App Logo Header
        val appTitlePaint = Paint().apply {
            color = AndroidColor.WHITE
            textSize = 52f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("✨ $appName", 110f, 180f, appTitlePaint)

        val appSubPaint = Paint().apply {
            color = AndroidColor.parseColor("#38BDF8")
            textSize = 30f
            isAntiAlias = true
        }
        canvas.drawText("AI Language Neural Engine", 110f, 226f, appSubPaint)

        // Top Divider
        val dividerPaint = Paint().apply {
            color = AndroidColor.parseColor("#334155")
            strokeWidth = 3f
            isAntiAlias = true
        }
        canvas.drawLine(110f, 266f, (width - 110).toFloat(), 266f, dividerPaint)

        // Language Pill Headers
        val langHeaderPaint = Paint().apply {
            color = AndroidColor.parseColor("#A5B4FC")
            textSize = 36f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("From: $sourceLang", 110f, 336f, langHeaderPaint)
        canvas.drawText("To: $targetLang", 620f, 336f, langHeaderPaint)

        // Original Text
        val labelPaint = Paint().apply {
            color = AndroidColor.parseColor("#94A3B8")
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("ORIGINAL TEXT", 110f, 420f, labelPaint)

        val bodyPaint = Paint().apply {
            color = AndroidColor.WHITE
            textSize = 38f
            isAntiAlias = true
        }
        drawWrappedText(canvas, originalText.ifBlank { "N/A" }, 110f, 476f, width - 220, bodyPaint, maxLines = 4)

        // Center Divider
        canvas.drawLine(110f, 710f, (width - 110).toFloat(), 710f, dividerPaint)

        // Translation Result Header
        val transLabelPaint = Paint().apply {
            color = AndroidColor.parseColor("#38BDF8")
            textSize = 28f
            isFakeBoldText = true
            isAntiAlias = true
        }
        canvas.drawText("TRANSLATED RESULT", 110f, 770f, transLabelPaint)

        val transBodyPaint = Paint().apply {
            color = AndroidColor.parseColor("#38BDF8")
            textSize = 42f
            isFakeBoldText = true
            isAntiAlias = true
        }
        drawWrappedText(canvas, translatedText, 110f, 836f, width - 220, transBodyPaint, maxLines = 6)

        // Footer Branding Watermark
        val footerPaint = Paint().apply {
            color = AndroidColor.parseColor("#64748B")
            textSize = 26f
            isAntiAlias = true
        }
        canvas.drawText("Generated by Translator Pro AI • $appName", 110f, (height - 120).toFloat(), footerPaint)

        return bitmap
    }

    private fun drawWrappedText(
        canvas: Canvas,
        text: String,
        x: Float,
        startY: Float,
        maxWidth: Int,
        paint: Paint,
        maxLines: Int
    ) {
        val words = text.split(" ")
        var currentLine = ""
        var y = startY
        var linesDrawn = 0

        for (word in words) {
            val testLine = if (currentLine.isEmpty()) word else "$currentLine $word"
            val measure = paint.measureText(testLine)
            if (measure <= maxWidth) {
                currentLine = testLine
            } else {
                canvas.drawText(currentLine, x, y, paint)
                y += paint.textSize * 1.35f
                linesDrawn++
                if (linesDrawn >= maxLines - 1) {
                    currentLine = "$word..."
                    break
                }
                currentLine = word
            }
        }
        if (currentLine.isNotEmpty() && linesDrawn < maxLines) {
            canvas.drawText(currentLine, x, y, paint)
        }
    }

    fun saveImageToGallery(context: Context, bitmap: Bitmap): Boolean {
        return try {
            val filename = "Translation_${System.currentTimeMillis()}.png"
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, filename)
                put(MediaStore.Images.Media.MIME_TYPE, "image/png")
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/TranslatorPro")
                    put(MediaStore.Images.Media.IS_PENDING, 1)
                }
            }
            val resolver = context.contentResolver
            val imageUri = resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            if (imageUri != null) {
                resolver.openOutputStream(imageUri)?.use { stream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
                }
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    contentValues.clear()
                    contentValues.put(MediaStore.Images.Media.IS_PENDING, 0)
                    resolver.update(imageUri, contentValues, null, null)
                }
                true
            } else false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}
