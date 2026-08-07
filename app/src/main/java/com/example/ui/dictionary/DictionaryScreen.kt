package com.example.ui.dictionary

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import com.example.R
import com.example.ui.theme.*

@Composable
fun DictionaryScreen(
    viewModel: DictionaryViewModel
) {
    val searchQuery by viewModel.searchQuery.collectAsState()
    val currentWord by viewModel.currentWord.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    val showSavedTab by viewModel.showSavedTab.collectAsState()
    val savedWords by viewModel.savedWords.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .testTag("dictionary_screen")
    ) {
        // Title Header
        Text(
            text = "AI Smart Dictionary",
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = "Instant Definitions, IPA Phonetics & Synonyms",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Search Input Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            placeholder = { Text("Search word or phrase...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            trailingIcon = {
                IconButton(
                    onClick = { viewModel.lookupWord(searchQuery) },
                    modifier = Modifier.testTag("submit_dict_search_button")
                ) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Lookup", tint = IndigoPrimary)
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = IndigoPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline
            ),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("dict_search_input")
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Tab Selection Chips
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            FilterChip(
                selected = !showSavedTab,
                onClick = { viewModel.setShowSavedTab(false) },
                label = { Text("Word Lookup") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IndigoPrimary,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("dict_tab_lookup")
            )
            FilterChip(
                selected = showSavedTab,
                onClick = { viewModel.setShowSavedTab(true) },
                label = { Text("Saved Words (${savedWords.size})") },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = IndigoPrimary,
                    selectedLabelColor = Color.White
                ),
                modifier = Modifier.testTag("dict_tab_saved")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (showSavedTab) {
            // Saved Words List
            if (savedWords.isEmpty()) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    Text(
                        text = "No saved words in your vocabulary list.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    items(savedWords, key = { it.word }) { saved ->
                        Card(
                            onClick = {
                                viewModel.setShowSavedTab(false)
                                viewModel.lookupWord(saved.word)
                            },
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("saved_word_card_${saved.word}")
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = saved.word,
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = IndigoPrimary
                                    )
                                    IconButton(onClick = { viewModel.speakWord(saved.word) }) {
                                        Icon(Icons.Default.VolumeUp, contentDescription = "Speak", tint = IndigoPrimary)
                                    }
                                }
                                Text(
                                    text = "${saved.phonetic} • ${saved.partOfSpeech}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = saved.definition,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
                    }
                }
            }
        } else {
            // Word Lookup Content Card
            if (isLoading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    CircularProgressIndicator(color = IndigoPrimary)
                }
            } else if (currentWord != null) {
                val word = currentWord!!
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(4.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .testTag("dict_result_card")
                ) {
                    LazyColumn(modifier = Modifier.padding(20.dp)) {
                        item {
                            Row(
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = IndigoPrimary.copy(alpha = 0.15f)
                                ) {
                                    Text(
                                        text = "CEFR ${word.cefrLevel}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = IndigoPrimary,
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.toggleSaveCurrentWord() },
                                    modifier = Modifier.testTag("bookmark_word_button")
                                ) {
                                    Icon(
                                        imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Outlined.BookmarkBorder,
                                        contentDescription = "Save Word",
                                        tint = if (isSaved) IndigoPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column {
                                    Text(
                                        text = word.word,
                                        style = MaterialTheme.typography.displayMedium.copy(fontWeight = FontWeight.ExtraBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = "${word.phonetic} • ${word.partOfSpeech}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }

                                IconButton(
                                    onClick = { viewModel.speakWord(word.word) },
                                    colors = IconButtonDefaults.iconButtonColors(containerColor = IndigoPrimary.copy(alpha = 0.15f))
                                ) {
                                    Icon(Icons.Default.VolumeUp, contentDescription = "Pronounce", tint = IndigoPrimary)
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            Text(
                                text = "Definition",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = word.definition,
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = "Example Sentence",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = "\"${word.exampleSentence}\"",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier.padding(12.dp)
                                )
                            }

                            if (word.synonyms.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Text(
                                    text = "Synonyms",
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    word.synonyms.forEach { syn ->
                                        SuggestionChip(
                                            onClick = { viewModel.lookupWord(syn) },
                                            label = { Text(syn) }
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
