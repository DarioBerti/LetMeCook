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
            Icon(
                imageVector        = Icons.Default.ShoppingBasket,
                contentDescription = "Ingredients basket",
                tint               = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier           = Modifier.size(20.dp)
            )
            Text(
                text       = "Ingredients",
                fontSize   = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color      = MaterialTheme.colorScheme.onBackground,
                modifier   = Modifier
                    .weight(1f)
                    .padding(start = 8.dp)
            )
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
            Spacer(Modifier.width(10.dp))
            if (isLogged) Icon(
                imageVector        = Icons.Default.ShoppingCart,
                contentDescription = "Add to cart",
                tint               = primary,
                modifier           = Modifier
                    .size(25.dp)
                    .clickable {
                        val toAdd = scaled.filter { it.id in selectedIds }
                        if (toAdd.isNotEmpty()) {
                            onAddToCart(toAdd, servings)
                        }
                    }
            )
        }

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