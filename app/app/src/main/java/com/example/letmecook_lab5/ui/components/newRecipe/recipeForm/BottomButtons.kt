package com.example.letmecook_lab5.ui.components.newRecipe.recipeForm

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Step

@Composable
fun BottomButtons(
    steps: List<Step>,
    addStep: () -> Unit,
    saveRecipe: () -> Unit,
    onBack: () -> Unit,
    onNextStep: () -> Unit,
    isLast: Boolean,
    stepIndex: Int
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding()
            .background(
                color = MaterialTheme.colorScheme.onPrimary,
                shape = RoundedCornerShape(32.dp))
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // BACK FUNCTIONALITY
        if (stepIndex > 0) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(32.dp))
                    .clickable { onBack() }
                    .padding(4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Step",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "PREVIOUS STEP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else if (stepIndex == 0){
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(32.dp))
                    .clickable { onBack() }
                    .padding(4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back Step",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "RECIPE DETAILS",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Column(modifier = Modifier.weight(1f)) { }
        }

        // PUBLISH FUNCTIONALITY
        if (steps.isNotEmpty()) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = RoundedCornerShape(32.dp))
                    .clickable { saveRecipe() }
                    .padding(4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Upload,
                    contentDescription = "Remove ingredient",
                    tint = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "PUBLISH",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Column(modifier = Modifier.weight(1f)) { }
        }

        // FORWARD FUNCTIONALTY
        if (isLast) {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(32.dp))
                    .clickable { addStep() }
                    .padding(4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Remove ingredient",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "ADD STEP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(32.dp))
                    .clickable { onNextStep() }
                    .padding(4.dp)
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Remove ingredient",
                    tint = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.size(15.dp)
                )
                Text(
                    text = "NEXT STEP",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}