package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Premium AI Theme Palette
val IndigoPrimary = Color(0xFF6366F1)
val ElectricViolet = Color(0xFF8B5CF6)
val CyberCyan = Color(0xFF06B6D4)
val EmeraldGlow = Color(0xFF10B981)
val NeonPink = Color(0xFFEC4899)
val SunsetAmber = Color(0xFFF59E0B)

// Dark Theme Colors
val DarkBackground = Color(0xFF0F172A)
val DarkSurface = Color(0xFF1E293B)
val DarkSurfaceVariant = Color(0xFF334155)
val DarkBorder = Color(0xFF475569)
val DarkTextPrimary = Color(0xFFF8FAFC)
val DarkTextSecondary = Color(0xFF94A3B8)

// Light Theme Colors
val LightBackground = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightBorder = Color(0xFFE2E8F0)
val LightTextPrimary = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF64748B)

// Gradient Sets
val GradientPrimary = listOf(IndigoPrimary, ElectricViolet, CyberCyan)
val GradientAccent = listOf(ElectricViolet, NeonPink)
val GradientEmerald = listOf(CyberCyan, EmeraldGlow)
val GradientDarkCard = listOf(Color(0xFF1E293B), Color(0xFF0F172A))
val GradientGlassBorder = listOf(
    Color.White.copy(alpha = 0.3f),
    Color.White.copy(alpha = 0.05f),
    IndigoPrimary.copy(alpha = 0.4f)
)
