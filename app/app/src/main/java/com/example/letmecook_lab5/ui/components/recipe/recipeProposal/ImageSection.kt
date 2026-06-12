package com.example.letmecook_lab5.ui.components.recipe.recipeProposal

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.letmecook_lab5.ui.components.common.ImagePlaceholder
import androidx.compose.material.icons.outlined.CheckCircle


@Composable
fun ImageSection(
    imageUrl: String?,
    rating: Float,
    isSaved: Boolean,
    onSaveClick: () -> Unit,
    onShowReviewSheet: () -> Unit,
    hasReviewed: Boolean,
    isLogged: Boolean
) {
    val primary = MaterialTheme.colorScheme.primary
    val secondary = MaterialTheme.colorScheme.secondary
    val surface = MaterialTheme.colorScheme.surface

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(shape = RoundedCornerShape(12.dp))
            .height(220.dp)
    ) {
        if (!imageUrl.isNullOrEmpty()) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Recipe image",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            ImagePlaceholder()
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(surface, shape = RoundedCornerShape(6.dp))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector        = Icons.Rounded.Star,
                contentDescription = "Star",
                tint               = secondary,
                modifier           = Modifier.size(25.dp)
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text     = "%.1f".format(rating),
                fontSize = 12.sp,
                color    = Color.Black
            )
        }

        if (isLogged) Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier
                .background(surface, RoundedCornerShape(6.dp))
                .padding(4.dp)
                .clickable { onShowReviewSheet() } ) {
                Icon(
                    imageVector        = if (hasReviewed) Icons.Default.CheckCircle else Icons.Outlined.CheckCircle,                    contentDescription = "Mark as cooked",
                    tint               = secondary,
                    modifier           = Modifier.size(25.dp)
                )
            }
            Spacer(Modifier.width(8.dp))
            Box(modifier = Modifier
                .background(surface, RoundedCornerShape(6.dp))
                .padding(4.dp)
                .clickable { onSaveClick() }) {
                Icon(
                    imageVector        = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Save recipe",
                    tint               = primary,
                    modifier           = Modifier.size(25.dp)
                )
            }
        }
    }
}