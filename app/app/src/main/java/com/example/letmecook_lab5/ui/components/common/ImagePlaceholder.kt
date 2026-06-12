package com.example.letmecook_lab5.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ImagePlaceholder(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxSize()
            .height(220.dp)
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Default.Fastfood,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(64.dp)
        )
    }
}