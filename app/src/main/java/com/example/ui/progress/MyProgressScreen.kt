package com.example.ui.progress

import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyProgressScreen(
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val topicPrefs = remember { context.getSharedPreferences("topic_partner_prefs", Context.MODE_PRIVATE) }
    val scrollState = rememberScrollState()

    val topicIds = remember { listOf("routine", "interview", "shopping", "doctor") }

    // Read stored metrics
    var completedTopicsCount by remember { mutableStateOf(0) }
    var totalXp by remember { mutableStateOf(100) }
    var totalMinutes by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        var count = 0
        topicIds.forEach { id ->
            if (topicPrefs.getBoolean("topic_completed_$id", false)) {
                count++
            }
        }
        completedTopicsCount = count
        totalXp = topicPrefs.getInt("user_xp", 100)
        // Estimated practice time: 15 mins per completed topic + base baseline of 10 mins if 0
        totalMinutes = (count * 15) + (if (count == 0) 10 else 0)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "My Progress",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier.testTag("progress_back_button")
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        modifier = Modifier.testTag("my_progress_screen")
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
                .verticalScroll(scrollState)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Hero Overview Banner
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("progress_hero_card")
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    ElectricViolet.copy(alpha = 0.12f),
                                    IndigoPrimary.copy(alpha = 0.08f),
                                    MaterialTheme.colorScheme.surface
                                )
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("📈", fontSize = 28.sp)
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Overall Learning Progress",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "Your English AI Conversation Analytics",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = EmeraldGlow.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Active Learner",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = EmeraldGlow,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        // 3 Key Metrics Cards
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // 1. Topics Completed
                            MetricTile(
                                title = "Topics Completed",
                                value = "$completedTopicsCount / 4",
                                icon = Icons.Default.CheckCircle,
                                color = ElectricViolet,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("stat_topics_completed")
                            )

                            // 2. Total XP
                            MetricTile(
                                title = "Total XP",
                                value = "$totalXp XP",
                                icon = Icons.Default.Star,
                                color = SunsetAmber,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("stat_total_xp")
                            )

                            // 3. Total Minutes
                            MetricTile(
                                title = "Total Minutes",
                                value = "$totalMinutes mins",
                                icon = Icons.Default.Timer,
                                color = CyberCyan,
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("stat_total_minutes")
                            )
                        }
                    }
                }
            }

            // Strengths vs Weak Areas Chart Section
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("strengths_vs_weakness_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.PieChart,
                                contentDescription = null,
                                tint = ElectricViolet,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Strengths vs Weak Areas",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.testTag("chart_section_title")
                            )
                        }

                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = ElectricViolet.copy(alpha = 0.1f)
                        ) {
                            Text(
                                text = "AI Analysis",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = ElectricViolet,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    // Donut Chart Graphic
                    StrengthsVsWeaknessDonutChart(
                        strengthPercentage = 0.70f,
                        weakPercentage = 0.30f,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .testTag("strengths_donut_chart")
                    )

                    // Legend & Breakdown Items
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        // Strength Legend Item
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(EmeraldGlow.copy(alpha = 0.08f))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(EmeraldGlow, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Strengths (70%)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.testTag("strength_legend_text")
                                    )
                                    Text(
                                        text = "Vocabulary, Conversational Fluency & Confidence",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "70%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldGlow
                            )
                        }

                        // Weak Area Legend Item
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SunsetAmber.copy(alpha = 0.08f))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(12.dp)
                                        .background(SunsetAmber, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(
                                        text = "Weak Areas (30%)",
                                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface,
                                        modifier = Modifier.testTag("weakness_legend_text")
                                    )
                                    Text(
                                        text = "Past Tense Verbs & Preposition Usage",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                            Text(
                                text = "30%",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = SunsetAmber
                            )
                        }
                    }
                }
            }

            // Detailed Skill Mastery Progress Bars
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("skill_mastery_card")
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Detailed Grammar & Skill Breakdown",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    SkillBarItem(
                        skillName = "Vocabulary & Expression",
                        percentage = 0.85f,
                        color = EmeraldGlow,
                        testTagPrefix = "vocab_skill"
                    )

                    SkillBarItem(
                        skillName = "Pronunciation Accuracy",
                        percentage = 0.80f,
                        color = CyberCyan,
                        testTagPrefix = "pronunciation_skill"
                    )

                    SkillBarItem(
                        skillName = "Grammar & Sentence Structure",
                        percentage = 0.65f,
                        color = SunsetAmber,
                        testTagPrefix = "grammar_skill"
                    )

                    SkillBarItem(
                        skillName = "Past Tense Verbs",
                        percentage = 0.55f,
                        color = ElectricViolet,
                        testTagPrefix = "past_tense_skill"
                    )
                }
            }
        }
    }
}

@Composable
fun MetricTile(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = color.copy(alpha = 0.1f),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.25f)),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 10.sp
            )
        }
    }
}

@Composable
fun StrengthsVsWeaknessDonutChart(
    strengthPercentage: Float,
    weakPercentage: Float,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height
            val side = minOf(canvasWidth, canvasHeight)
            val strokeWidth = 36f

            val topLeft = Offset((canvasWidth - side) / 2 + strokeWidth, (canvasHeight - side) / 2 + strokeWidth)
            val arcSize = Size(side - strokeWidth * 2, side - strokeWidth * 2)

            val strengthAngle = strengthPercentage * 360f
            val weakAngle = weakPercentage * 360f

            // Strengths Arc (Green / EmeraldGlow)
            drawArc(
                color = Color(0xFF10B981),
                startAngle = -90f,
                sweepAngle = strengthAngle - 4f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )

            // Weak Areas Arc (Amber / SunsetAmber)
            drawArc(
                color = Color(0xFFF59E0B),
                startAngle = -90f + strengthAngle + 2f,
                sweepAngle = weakAngle - 4f,
                useCenter = false,
                topLeft = topLeft,
                size = arcSize,
                style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "70%",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = EmeraldGlow
            )
            Text(
                text = "Strengths Ratio",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun SkillBarItem(
    skillName: String,
    percentage: Float,
    color: Color,
    testTagPrefix: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = skillName,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.testTag("${testTagPrefix}_label")
            )
            Text(
                text = "${(percentage * 100).toInt()}%",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = color
            )
        }
        LinearProgressIndicator(
            progress = { percentage },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape)
                .testTag("${testTagPrefix}_progress"),
            color = color,
            trackColor = color.copy(alpha = 0.15f)
        )
    }
}
