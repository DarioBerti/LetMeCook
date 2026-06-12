package com.example.letmecook_lab5.ui.screens.notifications

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsPaused
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.ui.components.notification.NotificationItem
import com.example.letmecook_lab5.viewModel.NotificationViewModel

@Composable
fun NotificationsScreen(
    notificationViewModel: NotificationViewModel,
    onClick: (Notification) -> Unit
) {
    val notifications by notificationViewModel.notifications.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(
                items = notifications,
                key = { it.id }
            ) { n ->
                NotificationItem(
                    notification = n,
                    onClick = onClick,
                    onMarkAsRead = { notificationViewModel.markAsRead(n.id) },
                    onDelete = { notificationViewModel.deleteNotification(n.id)}
                )
            }
            item {
                Spacer(Modifier.height(12.dp))
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsPaused,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = Color.Gray
                    )
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text =
                            if (notifications.isEmpty())
                                "NO NOTIFICATIONS"
                            else
                                "NO MORE NOTIFICATIONS",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}