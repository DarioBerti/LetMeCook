package com.example.letmecook_lab5.ui.components.notification

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.navigation.NotificationsRoute

@Composable
fun NotificationSnackbar(
    data: SnackbarData,
    onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .padding(12.dp)
            .fillMaxWidth(),
        onClick = {
            data.dismiss()
            onClick()
        }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = null
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "New notification",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = data.visuals.message,
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}