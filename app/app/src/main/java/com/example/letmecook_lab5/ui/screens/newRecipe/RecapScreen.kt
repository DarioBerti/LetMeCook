package com.example.letmecook_lab5.ui.screens.newRecipe

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ChipColors
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.ui.components.newRecipe.recap.RecapImageBox


@Composable
fun RecapScreen(
    title: String,
    imageUrl: String,
    errorMessage: String?,
    tags: List<String>,
    availableTags: List<String>,
    onPublish: () -> Unit,
    onKeepEditing: () -> Unit,
    onAddTag: (String) -> Unit,
    onRemoveTag: (String) -> Unit,
    onClearError: () -> Unit,
    onNavigate: () -> Unit = {}
) {

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        if (errorMessage != null) {
            AlertDialog(
                onDismissRequest = onClearError,
                title = { Text("Validation Error") },
                text = { Text(errorMessage) },
                confirmButton = {
                    TextButton(onClick = onClearError) {
                        Text("OK")
                    }
                }
            )
        }
        RecapImageBox(
            title = title,
            imageUrl = imageUrl
        )
        Spacer(Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Add Hashtags",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${tags.size} SELECTED",
                    style = MaterialTheme.typography.bodySmall,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                tags.forEach { tag ->
                    AssistChip(
                        onClick = { onRemoveTag(tag) },
                        label = {
                            Text(
                                text = "#${tag}",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.ExtraBold,
                                color = MaterialTheme.colorScheme.onBackground
                            )
                        },
                        shape = RoundedCornerShape(24.dp),
                        colors = ChipColors(
                            containerColor = MaterialTheme.colorScheme.inversePrimary,
                            labelColor = MaterialTheme.colorScheme.onBackground,
                            leadingIconContentColor = MaterialTheme.colorScheme.onBackground,
                            trailingIconContentColor = MaterialTheme.colorScheme.onBackground,
                            disabledContainerColor = MaterialTheme.colorScheme.onBackground,
                            disabledLabelColor = MaterialTheme.colorScheme.onBackground,
                            disabledLeadingIconContentColor = MaterialTheme.colorScheme.onBackground,
                            disabledTrailingIconContentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Remove tag",
                                modifier = Modifier.size(8.dp)
                            )
                        }
                    )
                }
                AssistChip(
                    onClick = { expanded = true },
                    label = {
                        Text(
                            text = "Add",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    },
                    shape = RoundedCornerShape(24.dp),
                    colors = ChipColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        labelColor = MaterialTheme.colorScheme.primary,
                        leadingIconContentColor = MaterialTheme.colorScheme.primary,
                        trailingIconContentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.primary,
                        disabledLabelColor = MaterialTheme.colorScheme.primary,
                        disabledLeadingIconContentColor = MaterialTheme.colorScheme.primary,
                        disabledTrailingIconContentColor = MaterialTheme.colorScheme.primary,
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add tag",
                            modifier = Modifier.size(8.dp)
                        )
                    },
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
                )
            }
        }

        Box {
            Spacer(modifier = Modifier.size(1.dp))
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                availableTags.forEach { tag ->
                    DropdownMenuItem(
                        text = { Text("#$tag", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.scrim) },
                        onClick = {
                            onAddTag(tag)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = onPublish,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Publish now")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = onKeepEditing,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Keep editing")
        }
    }

}
