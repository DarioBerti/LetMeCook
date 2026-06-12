package com.example.letmecook_lab5.ui.components.recipeList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun FilterMenu(
    onIngredientDeselect: (String) -> Unit,
    onIngredientsDisplay: () -> Unit,
    onTagSelect: (String) -> Unit,
    onTagDeselect: (String) -> Unit,
    onMaxCostUpdate: (Double) -> Unit,
    onMinCostUpdate: (Double) -> Unit,
    ingredientsSelected: Set<String>,
    tagsSelected: Set<String>,
    totalTags: List<String>,
    minCost: Double,
    maxCost: Double,
    inputMinCost: Double,
    inputMaxCost: Double
) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 16.dp, bottomEnd = 16.dp))
            .background(MaterialTheme.colorScheme.tertiaryContainer),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        TagFilterSection(
            tags = totalTags,
            selectedTags = tagsSelected,
            onTagSelect = onTagSelect,
            onTagDeselect = onTagDeselect
        )
        PriceRangeSlider(
            onMaxCostUpdate = { onMaxCostUpdate(it) },
            onMinCostUpdate = { onMinCostUpdate(it) },
            minCost = minCost,
            maxCost = maxCost,
            inputMaxCost = inputMaxCost,
            inputMinCost = inputMinCost
        )
        IngredientFilterSection(
            items = ingredientsSelected,
            onAdd = onIngredientsDisplay,
            onDismiss = onIngredientDeselect
        )
    }
}

@Composable
fun SearchAndFiltersSection(
    query: String,
    onQueryChanged: (String) -> Unit,
    onTrailingButton: () -> Unit,
){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RecipeSearchBar(
            query = query,
            onQueryChanged = onQueryChanged,
            modifier = Modifier.weight(1f)
        )

        Spacer(modifier = Modifier.width(8.dp))

        IconButton(
            onClick = { onTrailingButton() },
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
        ) {
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Expand filters",
                tint = Color.White,
                modifier = Modifier.size(28.dp),
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeSearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChanged,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.Black,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White
        ),
        placeholder = { Text("What are we cooking today?",
            style = MaterialTheme.typography.labelLarge,
            color = Color.Black.copy(alpha = 0.5f))},
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = MaterialTheme.colorScheme.primary
            )
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(onClick = { onQueryChanged("") }) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = "Clear Text"
                    )
                }
            }
        },
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done
        ),
        modifier = modifier.fillMaxWidth()
    )
}

@Composable
fun PriceRangeSlider(
    onMaxCostUpdate: (Double) -> Unit,
    onMinCostUpdate: (Double) -> Unit,
    minCost: Double,
    maxCost: Double,
    inputMaxCost: Double,
    inputMinCost: Double,
) {
    var sliderPosition by remember { mutableStateOf(inputMinCost.toFloat()..inputMaxCost.toFloat()) }
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top
    ){
        Row(modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Cost:",
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                text = "€ %.1f0 — €%.1f0".format(
                    sliderPosition.start,
                    sliderPosition.endInclusive
                ),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        }

        RangeSlider(
            value = sliderPosition,
            onValueChange = { range -> sliderPosition = range },
            valueRange = minCost.toFloat()..maxCost.toFloat(),
            onValueChangeFinished = {
                onMaxCostUpdate(sliderPosition.endInclusive.toDouble())
                onMinCostUpdate(sliderPosition.start.toDouble() )
            },
        )

    }
}