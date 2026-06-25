package com.example.letmecook_lab5.ui.components.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.model.Collection
import kotlin.collections.forEach

@Composable
fun SaveRecipeDialog(
    recipeId: String,
    collections: List<Collection>,
    onSave: (String, List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCollectionIds by remember {
        mutableStateOf(collections.filter { recipeId in it.recipeIds }.map { it.id }.toSet())
    }

    LaunchedEffect(collections) {
        selectedCollectionIds = collections.filter { recipeId in it.recipeIds }.map { it.id }.toSet()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Save to collection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column {
                collections.forEach { col ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedCollectionIds = if (col.id in selectedCollectionIds)
                                    selectedCollectionIds - col.id
                                else
                                    selectedCollectionIds + col.id
                            }
                            .background(
                                if (col.id in selectedCollectionIds)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = col.id in selectedCollectionIds,
                            onCheckedChange = { checked ->
                                selectedCollectionIds = if (checked)
                                    selectedCollectionIds + col.id
                                else
                                    selectedCollectionIds - col.id
                            }
                        )
                        Text(
                            col.name,
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 18.sp,
                            fontWeight = if (col.id in selectedCollectionIds) FontWeight.ExtraBold else FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        onSave(recipeId,selectedCollectionIds.toList())
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        dismissButton = {}
    )
}

