package com.example.letmecook_lab5.ui.components.community

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.CommunityEvent
import com.example.letmecook_lab5.model.CommunityEventType
import com.example.letmecook_lab5.ui.components.community.event.CommunityEventAction
import com.example.letmecook_lab5.ui.components.community.event.CommunityEventHeader
import com.example.letmecook_lab5.ui.components.community.event.CommunityEventImages

@Composable
fun CommunityEventCard(
    event: CommunityEvent,
    onRecipeClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        tonalElevation = 4.dp
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CommunityEventHeader(
                userName = event.userName,
                userNickname = event.userNickname,
                userImageUrl = event.userImage,
                timestamp = event.timestamp,
                onUserClick = { onUserClick(event.userId) }
            )
            Spacer(modifier = Modifier.height(8.dp))
            CommunityEventAction(type = event.type)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = event.recipeTitle,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.padding(top = 4.dp)
            )
            if (event.type == CommunityEventType.REVIEW && event.text.isNotBlank()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = event.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            CommunityEventImages(
                recipeImage = event.recipeImage,
                reviewImage = event.reviewImage,
                onRecipeClick = { onRecipeClick(event.recipeId) }
            )
        }
    }
}
