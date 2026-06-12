package com.example.letmecook_lab5.ui.components.recipe.reviews

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoLibrary
import androidx.compose.material.icons.outlined.AddAPhoto
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.letmecook_lab5.model.Review
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.letmecook_lab5.ui.components.common.CameraPreview
import com.example.letmecook_lab5.ui.components.common.takePhoto
import java.util.UUID


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFormDialog(
    existingReview: Review? = null,
    onDismiss: () -> Unit,
    onPublish: (Review) -> Unit,
    recipeId: String,
    userId: String
) {
    var rating by remember { mutableIntStateOf(existingReview?.rating ?: 0) }
    var comment by remember { mutableStateOf(existingReview?.comment ?: "") }
    var doInput by remember { mutableStateOf("") }
    var dontInput by remember { mutableStateOf("") }
    var doTips by remember { mutableStateOf(existingReview?.doTips ?: emptyList()) }
    var dontTips by remember { mutableStateOf(existingReview?.dontTips ?: emptyList()) }
    var selectedImageUri by remember { mutableStateOf(existingReview?.imageUrl?.takeIf { it.isNotBlank() }?.let(Uri::parse)) }
    var showImageSourceSheet by remember { mutableStateOf(false) }

    var showCamera by remember { mutableStateOf(false) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission()
        ) { granted ->

            hasCameraPermission = granted
        }


    val galleryLauncher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            selectedImageUri = uri
        }

    if (showImageSourceSheet) {
        ModalBottomSheet(
            onDismissRequest = { showImageSourceSheet = false }
        ) {
            ImageSourceSheetContent(
                onCameraClick = {
                    showImageSourceSheet = false
                    if (hasCameraPermission) {
                        showCamera = true
                    } else {
                        permissionLauncher.launch(
                            Manifest.permission.CAMERA
                        )
                    }
                },
                onGalleryClick = {
                    showImageSourceSheet = false
                    galleryLauncher.launch("image/*")
                }
            )
        }
    }

    if (showCamera) {
        Dialog(
            onDismissRequest = { showCamera = false },
            properties = DialogProperties( usePlatformDefaultWidth = false )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {
                CameraPreview(
                    imageCapture = imageCapture,
                    modifier = Modifier.fillMaxSize(),
                )

                FloatingActionButton(
                    onClick = {
                        takePhoto(
                            context = context,
                            imageCapture = imageCapture,
                            onImageCaptured = { uri ->
                                selectedImageUri = uri
                                showCamera = false
                            },
                            onError = { showCamera = false}
                        )
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Camera,
                        contentDescription = null
                    )
                }
            }
        }
    }


    Column(
        modifier = Modifier
            .padding(20.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "How was the dish?",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            IconButton(
                onClick = onDismiss,
                modifier = Modifier.size(22.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Close,
                    contentDescription = "Close",
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        StarRatingInput(
            rating = rating,
            onRatingChanged = { rating = it }
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color(0xFFFFC3BD))
                    .clickable { showImageSourceSheet = true },
                contentAlignment = Alignment.Center
            ) {
                if (selectedImageUri != null) {
                    AsyncImage(
                        model = selectedImageUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.AddAPhoto,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp),
                        tint = Color.DarkGray
                    )
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            OutlinedTextField(
                value = comment,
                onValueChange = { comment = it },
                modifier = Modifier
                    .weight(1f)
                    .height(90.dp),
                placeholder = {
                    Text(
                        text = "Share your experience...",
                        fontSize = 12.sp,
                        color = Color.Gray
                    ) },
                shape = RoundedCornerShape(24.dp),
                textStyle = TextStyle(
                    fontSize = 12.sp,
                    color = Color.Black
                ),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFE9D9DB),
                    unfocusedContainerColor = Color(0xFFE9D9DB),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        TipsInput(
            title = "DO Tips",
            backgroundColor = Color(0xFFC8FFC8),
            iconColor = Color(0xFF00FF00),
            currentInput = doInput,
            onInputChange = { doInput = it },
            tips = doTips,
            onAddTip = {
                if (doInput.isNotBlank()) {
                    doTips = doTips + doInput.trim()
                    doInput = ""
                }
            },
            onRemoveTip = { tip ->
                doTips = doTips - tip
            }
        )
        Spacer(modifier = Modifier.height(12.dp))
        TipsInput(
            title = "DON'T Tips",
            backgroundColor = Color(0xFFFFC8C8),
            iconColor = Color(0xFFFF0000),
            currentInput = dontInput,
            onInputChange = { dontInput = it },
            tips = dontTips,
            onAddTip = {
                if (dontInput.isNotBlank()) {
                    dontTips = dontTips + dontInput.trim()
                    dontInput = ""
                }
            },
            onRemoveTip = { tip ->
                dontTips = dontTips - tip
            }
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = {
                val review = Review(
                    id = existingReview?.id ?: UUID.randomUUID().toString(),
                    recipeId = existingReview?.recipeId ?: recipeId,
                    authorId = existingReview?.authorId ?: userId,
                    rating = rating,
                    comment = comment,
                    imageUrl = selectedImageUri?.toString() ?: "",
                    doTips = doTips,
                    dontTips = dontTips
                )
                onPublish(review)
                onDismiss()
            },
            modifier = Modifier.fillMaxWidth(),
            enabled =
                rating > 0 &&
                        comment.isNotBlank()
        ) {
            Text(
                text =
                    if (existingReview == null)
                        "Publish Review"
                    else
                        "Save changes"
            )
        }
    }
}

@Composable
fun ImageSourceSheetContent(
    onCameraClick: () -> Unit,
    onGalleryClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Choose image source",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onCameraClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.CameraAlt,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Camera")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onGalleryClick() }
                .padding(vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.PhotoLibrary,
                contentDescription = null
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text("Gallery")
        }
    }
}