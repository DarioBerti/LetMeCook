package com.example.letmecook_lab5.ui.components.notification

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.model.NotificationType
import com.example.letmecook_lab5.ui.screens.utils.formatTimeAgo

@Composable
fun NotificationItem(
    notification: Notification,
    onClick: (Notification) -> Unit,
    onMarkAsRead: () -> Unit,
    onDelete: () -> Unit
) {

    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (!notification.isRead) {
                        onMarkAsRead()
                    }
                    false
                }
                SwipeToDismissBoxValue.EndToStart -> {
                    onDelete()
                    false
                }
                else -> false
            }
        }
    )

    SwipeToDismissBox(
        state = dismissState,
        backgroundContent = {
            val direction = dismissState.dismissDirection
            val showMarkAsRead = (!notification.isRead) && direction == SwipeToDismissBoxValue.StartToEnd
            val showDelete = direction == SwipeToDismissBoxValue.EndToStart
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        when {
                            showMarkAsRead -> Color(0xFFB4FFB4)
                            showDelete -> Color(0xFFFFB4B4)
                            else -> Color.Transparent
                        }
                    )
                    .padding(horizontal = 24.dp),
                contentAlignment =
                    if (showMarkAsRead)
                        Alignment.CenterStart
                    else
                        Alignment.CenterEnd
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector =
                            if (showMarkAsRead)
                                Icons.Default.CheckCircleOutline
                            else
                                Icons.Default.DeleteOutline,
                        contentDescription = null,
                        tint = when {
                            showMarkAsRead -> Color.Gray
                            showDelete -> Color.White
                            else -> Color.Transparent
                        }
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = when {
                            showMarkAsRead -> "Mark as Read"
                            showDelete -> "Delete"
                            else -> ""
                        },
                        color = when {
                            showMarkAsRead -> Color.Gray
                            showDelete -> Color.White
                            else -> Color.Transparent
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) {
        ElevatedCard(
            onClick = {
                onMarkAsRead()
                onClick(notification)
            },
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.elevatedCardColors(
                containerColor =
                    if (notification.isRead)
                        Color.LightGray
                    else
                        Color.White
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.Top
                ) {
                    NotificationIcon(
                        type = notification.type,
                        isRead = notification.isRead
                    )
                    Spacer(Modifier.width(16.dp))
                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            text = notification.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = notification.message,
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = formatTimeAgo(notification.timestamp),
                            fontWeight = FontWeight.Light,
                            fontSize = 14.sp,
                            color = Color.DarkGray
                        )
                    }
                }
            }
        }
    }
    Spacer(Modifier.height(12.dp))
}

@Composable
private fun NotificationIcon(
    type: NotificationType,
    isRead: Boolean
) {
    val icon = when(type) {
        NotificationType.TEST               -> Icons.Default.Notifications
        NotificationType.REVIEW_RECEIVED    -> Icons.Default.Star
        NotificationType.RECIPE_DUPLICATED  -> Icons.Default.Lightbulb
        NotificationType.RECOMMENDATION     -> Icons.Default.Restaurant
        NotificationType.FOLLOW_RECEIVED    -> Icons.Default.PersonAdd
    }

    val color = when(type) {
        NotificationType.TEST               -> Color.DarkGray
        NotificationType.REVIEW_RECEIVED    -> Color.Yellow
        NotificationType.RECIPE_DUPLICATED  -> Color.Blue
        NotificationType.RECOMMENDATION     -> Color.Green
        NotificationType.FOLLOW_RECEIVED    -> Color.Magenta
    }

    Box(
        modifier = Modifier
            .size(48.dp)
            .background(
                color = if (isRead) Color.Gray else Color.LightGray,
                shape = CircleShape
            ),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon as ImageVector,
            contentDescription = null,
            tint = color
        )
    }
}