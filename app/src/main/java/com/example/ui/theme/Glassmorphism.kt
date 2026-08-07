package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Modifier.glassmorphism(
    cornerRadius: Dp = 20.dp,
    alpha: Float = 0.12f,
    borderAlpha: Float = 0.2f
): Modifier {
    val isDark = MaterialTheme.colorScheme.background == DarkBackground
    val surfaceColor = if (isDark) {
        Color.White.copy(alpha = alpha)
    } else {
        Color.White.copy(alpha = 0.85f)
    }

    val borderColor = if (isDark) {
        Color.White.copy(alpha = borderAlpha)
    } else {
        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
    }

    return this
        .clip(RoundedCornerShape(cornerRadius))
        .background(surfaceColor)
        .border(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(
                    borderColor,
                    borderColor.copy(alpha = 0.05f)
                )
            ),
            shape = RoundedCornerShape(cornerRadius)
        )
}
