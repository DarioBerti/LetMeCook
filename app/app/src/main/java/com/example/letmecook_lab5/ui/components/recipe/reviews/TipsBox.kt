package com.example.letmecook_lab5.ui.components.recipe.reviews

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TipsBox(
    tip: String,
    backgroundColor: Color,
    iconColor: Color,
    icon: ImageVector
) {
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .background(
                color = backgroundColor,
                shape = RoundedCornerShape(20.dp)
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier
                .padding(8.dp)
                .size(16.dp)

        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = tip,
            fontSize = 12.sp,
            modifier = Modifier.padding(8.dp)
        )
    }
}