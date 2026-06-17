package com.example.letmecook_lab5.ui.components.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FollowButton(
    isFollowing: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        if (isFollowing) {
            OutlinedButton(
                onClick = onClick,
                modifier = Modifier
            ) {
                Text("Following")
            }
        } else {
            Button(
                onClick = onClick
            ) {
                Text("Follow")
            }
        }
    }
}