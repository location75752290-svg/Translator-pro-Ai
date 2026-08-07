package com.example.ui.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onSplashFinished: () -> Unit
) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val scale by transition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logo_scale"
    )

    LaunchedEffect(Unit) {
        delay(2200) // 2.2 seconds splash display
        onSplashFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        DarkBackground,
                        Color(0xFF1E1B4B),
                        DarkBackground
                    )
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            // Glowing AI Language Logo
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .scale(scale)
                    .size(130.dp)
                    .border(
                        width = 3.dp,
                        brush = Brush.sweepGradient(GradientPrimary),
                        shape = CircleShape
                    )
                    .padding(8.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_app_logo_asset),
                    contentDescription = "Translator Pro AI Logo",
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // App Title
            Text(
                text = "Translator Pro AI",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 32.sp
                ),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle
            Text(
                text = "Next-Gen AI Language Intelligence",
                style = MaterialTheme.typography.bodyMedium,
                color = CyberCyan
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Smooth Loading Indicator
            CircularProgressIndicator(
                color = ElectricViolet,
                strokeWidth = 3.dp,
                modifier = Modifier.size(36.dp)
            )
        }

        // Developer Branding at bottom
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 40.dp)
        ) {
            Text(
                text = "POWERED BY",
                style = MaterialTheme.typography.labelSmall.copy(letterSpacing = 2.sp),
                color = DarkTextSecondary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "NL Apps Studio",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
    }
}
