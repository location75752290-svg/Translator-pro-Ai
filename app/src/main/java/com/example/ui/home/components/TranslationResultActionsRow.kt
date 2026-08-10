package com.example.ui.home.components

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.ui.theme.IndigoPrimary
import com.example.util.TranslationCardImageGenerator
import kotlinx.coroutines.launch

@Composable
fun TranslationResultActionsRow(
    translatedText: String,
    originalText: String,
    sourceLangName: String,
    targetLangName: String,
    snackbarHostState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var pendingSaveAction by remember { mutableStateOf<(() -> Unit)?>(null) }

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { _ ->
            pendingSaveAction?.invoke()
            pendingSaveAction = null
        }
    )

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(2.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, IndigoPrimary.copy(alpha = 0.2f)),
        modifier = modifier
            .fillMaxWidth()
            .testTag("translation_actions_card")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // 1. Copy Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.testTag("action_column_copy")
            ) {
                IconButton(
                    onClick = {
                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                        val clip = ClipData.newPlainText("Translation", translatedText)
                        clipboard.setPrimaryClip(clip)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Copied")
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = IndigoPrimary.copy(alpha = 0.12f),
                        contentColor = IndigoPrimary
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("copy_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCopy,
                        contentDescription = "Copy Translation"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Copy",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 2. Share Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.testTag("action_column_share")
            ) {
                IconButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, translatedText)
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Share Translation"))
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = IndigoPrimary.copy(alpha = 0.12f),
                        contentColor = IndigoPrimary
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("share_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share Translation"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Share",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // 3. Save as Image Button
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.testTag("action_column_image")
            ) {
                IconButton(
                    onClick = {
                        val executeSave: () -> Unit = {
                            val bitmap = TranslationCardImageGenerator.generateCardBitmap(
                                context = context,
                                appName = "Translator Pro AI",
                                sourceLang = sourceLangName,
                                targetLang = targetLangName,
                                originalText = originalText,
                                translatedText = translatedText
                            )
                            val success = TranslationCardImageGenerator.saveImageToGallery(context, bitmap)
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Saved to Gallery")
                            }
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED) {
                                executeSave()
                            } else {
                                pendingSaveAction = executeSave
                                storagePermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES)
                            }
                        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                executeSave()
                            } else {
                                pendingSaveAction = executeSave
                                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        } else {
                            executeSave()
                        }
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = IndigoPrimary.copy(alpha = 0.12f),
                        contentColor = IndigoPrimary
                    ),
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("save_image_icon_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Image,
                        contentDescription = "Save Card as Image"
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Save Image",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}
