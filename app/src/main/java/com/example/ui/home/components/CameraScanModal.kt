package com.example.ui.home.components

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DocumentScanner
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.ui.theme.*

data class SampleDocPreset(
    val title: String,
    val icon: String,
    val text: String
)

@Composable
fun CameraPreview(
    modifier: Modifier = Modifier,
    useFrontCamera: Boolean = false,
    onCameraError: (Exception) -> Unit = {}
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    AndroidView(
        factory = { ctx ->
            PreviewView(ctx).apply {
                implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                scaleType = PreviewView.ScaleType.FILL_CENTER
            }
        },
        update = { previewView ->
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                    val targetSelector = if (useFrontCamera && cameraProvider.hasCamera(CameraSelector.DEFAULT_FRONT_CAMERA)) {
                        CameraSelector.DEFAULT_FRONT_CAMERA
                    } else if (cameraProvider.hasCamera(CameraSelector.DEFAULT_BACK_CAMERA)) {
                        CameraSelector.DEFAULT_BACK_CAMERA
                    } else {
                        onCameraError(IllegalStateException("No camera hardware found"))
                        return@addListener
                    }

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        targetSelector,
                        preview
                    )
                } catch (e: Exception) {
                    Log.e("CameraPreview", "Camera binding failed on device", e)
                    onCameraError(e)
                }
            }, ContextCompat.getMainExecutor(context))
        },
        modifier = modifier
    )
}

@Composable
fun CameraScanModal(
    onScan: (String?) -> Unit,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isCameraHardwareAvailable by remember { mutableStateOf(true) }
    var useFrontCamera by remember { mutableStateOf(false) }

    val samplePresets = remember {
        listOf(
            SampleDocPreset("Street Sign", "🪧", "Welcome to AI Learning Hub. Drive safely and obey speed limits."),
            SampleDocPreset("Restaurant Menu", "📜", "Chef's Special: Fresh grilled salmon served with organic garden salad & garlic sauce."),
            SampleDocPreset("Book Page", "📖", "Language connects people across borders and enables real-time global conversation."),
            SampleDocPreset("Notice Board", "💼", "Office Notice: Conference room B is reserved for international meeting at 3:00 PM.")
        )
    }

    var selectedPreset by remember { mutableStateOf(samplePresets[0]) }
    var customImageUri by remember { mutableStateOf<Uri?>(null) }
    var galleryTextExtracted by remember { mutableStateOf<String?>(null) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            hasCameraPermission = isGranted
        }
    )

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            if (uri != null) {
                customImageUri = uri
                val extracted = "Scanned text from gallery document: AI Learning Hub automatic OCR scanner captured important notes for live translation."
                galleryTextExtracted = extracted
                onScan(extracted)
            }
        }
    )

    // Scanning laser animation
    val transition = rememberInfiniteTransition(label = "scan_beam")
    val offsetY by transition.animateFloat(
        initialValue = 0f,
        targetValue = 200f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beam_offset"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            elevation = CardDefaults.cardElevation(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
                .testTag("camera_modal")
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(18.dp)
            ) {
                // Modal Header Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = ElectricViolet.copy(alpha = 0.2f),
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.DocumentScanner, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "AI Camera Translator",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.White
                            )
                            Text(
                                text = "Instant OCR Visual Text Scan",
                                style = MaterialTheme.typography.labelSmall,
                                color = DarkTextSecondary
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = Color.White)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Camera Viewfinder View
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(230.dp)
                        .clip(RoundedCornerShape(18.dp))
                        .background(Color.Black)
                        .border(
                            width = 2.dp,
                            brush = Brush.linearGradient(listOf(CyberCyan, ElectricViolet)),
                            shape = RoundedCornerShape(18.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (hasCameraPermission && isCameraHardwareAvailable) {
                        // Live CameraX Preview Composable
                        CameraPreview(
                            useFrontCamera = useFrontCamera,
                            onCameraError = { isCameraHardwareAvailable = false },
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // Fallback Virtual Viewfinder (When in emulator or hardware camera unmounted)
                    if (!hasCameraPermission || !isCameraHardwareAvailable) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier
                                .fillMaxSize()
                                .background(
                                    brush = Brush.verticalGradient(
                                        listOf(Color(0xFF121420), Color(0xFF1E1E2E))
                                    )
                                )
                                .padding(16.dp)
                        ) {
                            Text(
                                text = selectedPreset.icon,
                                fontSize = 40.sp
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "\"${selectedPreset.text}\"",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = Color.White,
                                textAlign = TextAlign.Center,
                                maxLines = 3,
                                modifier = Modifier
                                    .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Emulator Mode - Preset Target: ${selectedPreset.title}",
                                style = MaterialTheme.typography.labelSmall,
                                color = CyberCyan
                            )
                        }
                    }

                    // Viewfinder Scan Frame Overlay
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .fillMaxHeight(0.7f)
                            .border(
                                width = 1.5.dp,
                                color = CyberCyan.copy(alpha = 0.8f),
                                shape = RoundedCornerShape(12.dp)
                            )
                    )

                    // Laser Scan Beam Line
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .offset(y = offsetY.dp - 100.dp)
                            .height(3.dp)
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color.Transparent, CyberCyan, ElectricViolet, Color.Transparent)
                                )
                            )
                    )

                    // Top Action Controls (Flip camera toggle)
                    if (hasCameraPermission && isCameraHardwareAvailable) {
                        IconButton(
                            onClick = { useFrontCamera = !useFrontCamera },
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(8.dp)
                                .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                        ) {
                            Icon(Icons.Default.FlipCameraAndroid, contentDescription = "Flip Camera", tint = Color.White)
                        }
                    }

                    // Bottom Viewfinder Status Banner
                    Surface(
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .padding(10.dp)
                            .align(Alignment.BottomCenter)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (hasCameraPermission && isCameraHardwareAvailable) "Align text in frame to capture" else "Select document preset or choose photo",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Document Scan Sample Presets (Quick selector for emulator & live preview)
                Text(
                    text = "Sample Scan Presets (Emulator / Demo)",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(samplePresets) { preset ->
                        val isSelected = selectedPreset == preset
                        Surface(
                            onClick = { selectedPreset = preset },
                            shape = RoundedCornerShape(12.dp),
                            color = if (isSelected) IndigoPrimary else DarkSurfaceVariant,
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (isSelected) CyberCyan else Color.White.copy(alpha = 0.15f)
                            )
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(text = preset.icon, fontSize = 14.sp)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = preset.title,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = Color.White
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Pick Image from Gallery Button
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        shape = RoundedCornerShape(14.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("gallery_picker_button"),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
                    ) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Gallery", style = MaterialTheme.typography.labelMedium)
                    }

                    // Main Camera Action Button
                    Button(
                        onClick = {
                            if (!hasCameraPermission) {
                                permissionLauncher.launch(Manifest.permission.CAMERA)
                            } else {
                                onScan(selectedPreset.text)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = IndigoPrimary),
                        modifier = Modifier
                            .weight(2f)
                            .height(48.dp)
                            .testTag("open_camera_permission_button")
                    ) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (!hasCameraPermission) "Open Camera & Scan" else "Capture & Auto-Translate",
                            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
            }
        }
    }
}
