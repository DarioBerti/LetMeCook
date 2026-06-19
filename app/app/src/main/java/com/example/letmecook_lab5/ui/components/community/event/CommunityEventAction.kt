package com.example.letmecook_lab5.ui.components.community.event

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.RestaurantMenu
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.CommunityEventType

@Composable
fun CommunityEventAction(
    type: CommunityEventType
) {
    val (text, icon, color) = when (type) {
        CommunityEventType.REVIEW -> Triple(
            "Tried this recipe",
            Icons.Default.Star,
            MaterialTheme.colorScheme.primary
        )
        CommunityEventType.NEW_RECIPE -> Triple(
            "New recipe published",
            Icons.Default.RestaurantMenu,
            MaterialTheme.colorScheme.primary
        )
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = color,
            modifier = Modifier.size(18.dp)
        )
        Spacer(modifier = Modifier.size(6.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )
    }
}