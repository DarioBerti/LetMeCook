package com.example.letmecook_lab5.ui.components.recipe.reviews

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.example.letmecook_lab5.model.Review
import com.example.letmecook_lab5.session.SessionManager
import com.example.letmecook_lab5.ui.components.recipe.photos.CommunityPhotoDialog

@Composable
fun ReviewItem(
    review: Review,
    onDelete: (String) -> Unit,
    onUpdate: (Review) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var showMenu by remember { mutableStateOf(false) }
    var showDeleteDialog by remember { mutableStateOf(false) }
    var showReviewSheet by remember { mutableStateOf(false) }

    var selectedPhotoReview by remember { mutableStateOf<Review?>(null) }

    if (selectedPhotoReview != null) {
        Dialog(
            onDismissRequest = { selectedPhotoReview = null }
        ) {
            CommunityPhotoDialog(
                review = selectedPhotoReview!!,
                onDismiss = { selectedPhotoReview = null }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(
                color = Color(0xFFFCEDEE),
                shape = RoundedCornerShape(30.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(30.dp)
                        .background(Color.Gray, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        tint = Color(0xFFFFFFFF),
                        modifier = Modifier.size(20.dp),
                        contentDescription = null
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = review.authorFullName,
                    fontWeight = FontWeight.SemiBold
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                StarRating(rating = review.rating)

                if (review.authorId == SessionManager.CURRENT_LOGGED_IN_USER_ID) {
                    Box {
                        IconButton( onClick = { showMenu = true } ) {
                            Icon(
                                imageVector = Icons.Default.MoreVert,
                                contentDescription = null
                            )
                        }
                        DropdownMenu(
                            expanded = showMenu,
                            onDismissRequest = { showMenu = false }
                        ) {
                            DropdownMenuItem(
                                text = { Text("Edit review") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Filled.Edit,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    showReviewSheet = true
                                }
                            )
                            DropdownMenuItem(
                                text = { Text("Delete review") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Delete,
                                        contentDescription = null
                                    )
                                },
                                onClick = {
                                    showMenu = false
                                    showDeleteDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = review.comment,
            modifier = Modifier.padding(horizontal = 16.dp),
            fontSize = 12.sp
        )
        Spacer(modifier = Modifier.height(6.dp))
        if (review.imageUrl.isNotBlank()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                AsyncImage(
                    model = review.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(20.dp))
                        .clickable {
                            selectedPhotoReview = review
                        },
                    contentScale = ContentScale.Crop
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        AnimatedVisibility(visible = expanded) {
            Column {
                if (review.doTips.isNotEmpty()) {
                    review.doTips.forEach { doTip ->
                        TipsBox(
                            tip = doTip,
                            backgroundColor = Color(0xFFC8FFC8),
                            iconColor = Color(0xFF00FF00),
                            icon = Icons.Filled.CheckCircle
                        )
                    }
                }
                if (review.dontTips.isNotEmpty()) {
                    review.dontTips.forEach { dontTip ->
                        TipsBox(
                            tip = dontTip,
                            backgroundColor = Color(0xFFFFC8C8),
                            iconColor = Color(0xFFFF0000),
                            icon = Icons.Filled.Cancel
                        )
                    }
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text =
                    if (expanded)
                        "Hide tips"
                    else
                        "View tips",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
            )
            IconButton( onClick = { expanded = !expanded } ) {
                Icon(
                    imageVector =
                        if (expanded)
                            Icons.Filled.KeyboardArrowUp
                        else
                            Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
    }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Delete review") },
            text = { Text("Are you sure?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteDialog = false
                        onDelete(review.id)
                    }
                ) {
                    Text("Delete")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDeleteDialog = false }
                ) {
                    Text("Cancel")
                }
            }
        )
    }

    if (showReviewSheet) {
        Dialog(
            onDismissRequest = { showReviewSheet = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 8.dp,
                color = Color.White
            ) {
                ReviewFormDialog(
                    existingReview = review,
                    onDismiss = { showReviewSheet = false },
                    onPublish = onUpdate,
                    recipeId = review.recipeId,
                    userId = SessionManager.CURRENT_LOGGED_IN_USER_ID
                )
            }
        }
    }
}