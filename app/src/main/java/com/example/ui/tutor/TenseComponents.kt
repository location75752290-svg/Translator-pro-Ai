package com.example.ui.tutor

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.TenseProgressEntity
import com.example.data.model.*
import com.example.ui.theme.*

@Composable
fun TensesDashboard(
    allTenses: List<EnglishTense>,
    progressMap: Map<String, TenseProgressEntity>,
    selectedFilter: String,
    onFilterSelected: (String) -> Unit,
    onSelectTense: (EnglishTense) -> Unit,
    modifier: Modifier = Modifier
) {
    val completedCount = progressMap.values.count { it.isCompleted }
    val totalTenses = allTenses.size
    val completionPercentage = if (totalTenses > 0) ((completedCount.toFloat() / totalTenses.toFloat()) * 100).toInt() else 0

    val filteredTenses = when (selectedFilter) {
        "PRESENT" -> allTenses.filter { it.category == TenseCategory.PRESENT }
        "PAST" -> allTenses.filter { it.category == TenseCategory.PAST }
        "FUTURE" -> allTenses.filter { it.category == TenseCategory.FUTURE }
        "COMPLETED" -> allTenses.filter { progressMap[it.id]?.isCompleted == true }
        else -> allTenses
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxSize()
    ) {
        // Overall Progress Banner
        item {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricViolet.copy(alpha = 0.3f)),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("tense_overall_progress_card")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column {
                            Text(
                                text = "Tenses Mastery Progress",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "$completedCount of $totalTenses Lessons Completed ($completionPercentage%)",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = ElectricViolet,
                            contentColor = Color.White
                        ) {
                            Text(
                                text = "$completionPercentage%",
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    LinearProgressIndicator(
                        progress = { completedCount.toFloat() / totalTenses.toFloat() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = ElectricViolet,
                        trackColor = ElectricViolet.copy(alpha = 0.15f)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Daily Reminder Chip
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SunsetAmber.copy(alpha = 0.12f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, SunsetAmber.copy(alpha = 0.25f))
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = SunsetAmber,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Daily Goal: Complete 1 tense lesson & quiz to master English & Urdu grammar!",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }

        // Category Filter Chips
        item {
            val filters = listOf(
                "ALL" to "All Tenses (12)",
                "PRESENT" to "Present (4)",
                "PAST" to "Past (4)",
                "FUTURE" to "Future (4)",
                "COMPLETED" to "Completed ($completedCount)"
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filters) { (key, label) ->
                    val isSelected = selectedFilter == key
                    FilterChip(
                        selected = isSelected,
                        onClick = { onFilterSelected(key) },
                        label = {
                            Text(
                                text = label,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricViolet,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("filter_chip_$key")
                    )
                }
            }
        }

        // List of Tense Cards
        items(filteredTenses, key = { it.id }) { tense ->
            val progress = progressMap[tense.id]
            val isCompleted = progress?.isCompleted == true
            val score = progress?.scorePercentage ?: 0

            TenseCardItem(
                tense = tense,
                isCompleted = isCompleted,
                scorePercentage = score,
                onClick = { onSelectTense(tense) }
            )
        }
    }
}

@Composable
fun TenseCardItem(
    tense: EnglishTense,
    isCompleted: Boolean,
    scorePercentage: Int,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 2.dp,
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isCompleted) EmeraldGlow.copy(alpha = 0.5f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("tense_card_${tense.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = when (tense.category) {
                            TenseCategory.PRESENT -> IndigoPrimary.copy(alpha = 0.15f)
                            TenseCategory.PAST -> ElectricViolet.copy(alpha = 0.15f)
                            TenseCategory.FUTURE -> SunsetAmber.copy(alpha = 0.15f)
                        },
                        modifier = Modifier.size(36.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = when (tense.category) {
                                    TenseCategory.PRESENT -> Icons.Default.Schedule
                                    TenseCategory.PAST -> Icons.Default.History
                                    TenseCategory.FUTURE -> Icons.Default.Update
                                },
                                contentDescription = null,
                                tint = when (tense.category) {
                                    TenseCategory.PRESENT -> IndigoPrimary
                                    TenseCategory.PAST -> ElectricViolet
                                    TenseCategory.FUTURE -> SunsetAmber
                                },
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = tense.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = tense.urduName,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                            color = ElectricViolet
                        )
                    }
                }

                if (isCompleted) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EmeraldGlow.copy(alpha = 0.15f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = "Completed",
                                tint = EmeraldGlow,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Score: $scorePercentage%",
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = EmeraldGlow
                            )
                        }
                    }
                } else {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = "Start Lesson",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = tense.easyEnglishExplanation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Formula: ${tense.formula.positive}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
    }
}
