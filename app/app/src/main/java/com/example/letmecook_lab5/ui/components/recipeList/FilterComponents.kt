package com.example.letmecook_lab5.ui.components.recipeList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun IngredientChip(
    text: String,
    onDismiss: (String) -> Unit,
    color: Color
) {
    InputChip(
        onClick = {
            onDismiss(text)
        },
        label = {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
                color = Color.White,
            ) },
        selected = true,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Localized description",
                Modifier.size(InputChipDefaults.AvatarSize),

                )
        },
        colors = InputChipDefaults.inputChipColors(
            selectedContainerColor = color,
            selectedLabelColor = Color.White,
            selectedTrailingIconColor = Color.White
        )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalBottomIngredients(
    list: List<String>,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    onAdd: (String) -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {
            onDismiss()
        },
        sheetState = sheetState
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.7f),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(list) { elem ->
                ListItem(
                    headlineContent = {
                        Text(text = elem)
                    },
                    modifier = Modifier.clickable{
                        scope.launch {sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible){
                                onAdd(elem)
                            }
                        }
                    }
                )
            }
        }
    }
}

@Composable

fun IngredientFilterSection(
    items: Set<String>,
    onAdd: () -> Unit,
    onDismiss: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Ingredients: ",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.fillMaxWidth()

        )


        FlowRow(
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items.forEach { item ->
                IngredientChip(item, onDismiss, MaterialTheme.colorScheme.secondaryContainer)
            }

            IconButton(
                onClick = { onAdd() },
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color.White
                ),
                modifier = Modifier.size(32.dp)
            ) {
                Icon(
                    modifier = Modifier.size(24.dp),
                    tint = MaterialTheme.colorScheme.primary,
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add new item"
                )
            }

        }
    }
}

@Composable
fun TagFilterSection(
    tags : List<String>,
    selectedTags : Set<String>,
    onTagSelect: (String) -> Unit,
    onTagDeselect: (String) -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp,)
    ) {
        tags.forEach {
            item {
                TagChip(
                    text = it,
                    onAdd = onTagSelect,
                    onDismiss = onTagDeselect,
                    selected = selectedTags.contains(it)
                )

                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            }
        }
    }
}

@Composable
fun TagChip(
    text: String,
    onAdd : (String) -> Unit,
    onDismiss: (String) -> Unit,
    selected: Boolean ,
) {
    FilterChip(
        onClick = {
            if (selected) onDismiss(text)
            else onAdd(text)
        },
        label = { Text(text,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold) },
        selected = selected,
        border = FilterChipDefaults.filterChipBorder(
            borderColor = MaterialTheme.colorScheme.primary,
            enabled = true,
            selected = selected
        ),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = Color.White,
            labelColor = MaterialTheme.colorScheme.primary,
            selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            selectedLabelColor = Color.White,
            selectedLeadingIconColor = Color.White
        ),
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Done icon",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        },
    )
}