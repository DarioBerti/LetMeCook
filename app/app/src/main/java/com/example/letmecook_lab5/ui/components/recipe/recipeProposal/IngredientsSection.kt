package com.example.letmecook_lab5.ui.components.recipe.recipeProposal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.model.Ingredient
import com.example.letmecook_lab5.ui.screens.recipeList.IngredientRow
import kotlin.collections.forEach
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween


@Composable
fun IngredientsSection(
    ingredients: List<Ingredient>,
    baseServings: Int,
    isLogged: Boolean,
    onAddToCart: (List<Ingredient>, Int) -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary

    val base = baseServings.coerceAtLeast(1)
    var servings by remember { mutableStateOf(base) }
    var selectedIds by remember { mutableStateOf(setOf<String>()) }
    val factor = servings.toDouble() / base
    val scaled = ingredients.map { it.copy(quantity = it.quantity * factor) }
    val allIds = ingredients.map { it.id }.toSet()
    val allSelected = allIds.isNotEmpty() && selectedIds.containsAll(allIds)
    val hasSelection = selectedIds.isNotEmpty()

    val pulse = rememberInfiniteTransition(label = "cartPulse")
    val cartScale by pulse.animateFloat(
        initialValue = 1f,
        targetValue = if (hasSelection) 1.08f else 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "cartScale"
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier          = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text       = "Ingredients",
                fontSize   = 18.sp,
                fontWeight = FontWeight.Bold,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier.padding(start = 8.dp)
            )
            Spacer(Modifier.weight(1f))
            if (isLogged) Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (servings > 1) servings-- }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Remove, tint = primary, modifier = Modifier.size(14.dp), contentDescription = "-")
                }
                Text(
                    text       = "$servings Servings",
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground,
                    fontSize   = 16.sp,
                    modifier   = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(onClick = { servings++ }, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Add, tint = primary, modifier = Modifier.size(14.dp), contentDescription = "+")
                }
            }

            Spacer(Modifier.width(8.dp))

            if (isLogged) Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(if (hasSelection) primary.copy(alpha = 0.15f) else Color.Transparent)
                    .clickable {
                        val toAdd = scaled.filter { it.id in selectedIds }
                        if (toAdd.isNotEmpty()) {
                            onAddToCart(toAdd, servings)
                            selectedIds = emptySet()
                        }
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector        = Icons.Default.ShoppingCart,
                    contentDescription = "Add to cart",
                    tint               = primary,
                    modifier           = Modifier
                        .size(25.dp)
                        .scale(cartScale)
                )
            }
        }

        if (isLogged) Text(
            text = if (allSelected) "Deselect all" else "Select all",
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            color = primary,
            modifier = Modifier
                .clickable { selectedIds = if (allSelected) emptySet() else allIds }
                .padding(start = 12.dp, top = 4.dp)
        )

        scaled.forEach { ingredient ->
            IngredientRow(
                ingredient = ingredient,
                selected   = ingredient.id in selectedIds,
                onToggle   = {
                    selectedIds = if (ingredient.id in selectedIds)
                        selectedIds - ingredient.id
                    else
                        selectedIds + ingredient.id
                }
            )
        }
        Spacer(Modifier.height(8.dp))
    }
}