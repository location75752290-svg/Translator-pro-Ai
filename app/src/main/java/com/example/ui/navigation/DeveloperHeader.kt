package com.example.ui.navigation

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.WorkspacePremium
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun DeveloperHeader(
    modifier: Modifier = Modifier
) {
    // Golden Metallic Gradient
    val goldGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFF099), // Light bright gold
            Color(0xFFFFD700), // Standard gold
            Color(0xFFFFA500), // Amber gold
            Color(0xFFFFD700), // Standard gold
            Color(0xFFFFF099)  // Light bright gold
        )
    )

    // Gold Glow Border Gradient
    val goldBorderGradient = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFFFFD700).copy(alpha = 0.6f),
            Color(0xFFFFA500).copy(alpha = 0.3f),
            Color(0xFFFFD700).copy(alpha = 0.6f)
        )
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .statusBarsPadding()
            .padding(top = 8.dp, bottom = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.90f),
            border = BorderStroke(1.dp, goldBorderGradient),
            shadowElevation = 8.dp,
            modifier = Modifier.wrapContentWidth()
        ) {
            Box(
                modifier = Modifier
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFFFFD700).copy(alpha = 0.15f),
                                Color.Transparent
                            )
                        )
                    )
                    .padding(horizontal = 18.dp, vertical = 7.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.WorkspacePremium,
                        contentDescription = "Developer Badge",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Text(
                        text = "Naeem App Developer",
                        style = TextStyle(
                            brush = goldGradient,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            letterSpacing = 1.1.sp,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Center,
                            shadow = Shadow(
                                color = Color(0xFFFFD700).copy(alpha = 0.5f),
                                blurRadius = 10f
                            )
                        )
                    )

                    Spacer(modifier = Modifier.width(6.dp))

                    Icon(
                        imageVector = Icons.Default.Stars,
                        contentDescription = "Verified Star",
                        tint = Color(0xFFFFD700),
                        modifier = Modifier
                            .size(16.dp)
                    )
                }
            }
        }
    }
}
