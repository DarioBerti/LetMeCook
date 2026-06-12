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


@Composable
fun IngredientsSection(
    ingredients: List<Ingredient>,
    baseServings: Int,
    isLogged: Boolean
) {
    val primary = MaterialTheme.colorScheme.primary

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
                IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Remove, tint = primary, modifier = Modifier.size(14.dp), contentDescription = "-")
                }
                Text(
                    text       = "$baseServings Servings",
                    fontWeight = FontWeight.Bold,
                    color      = MaterialTheme.colorScheme.onBackground,
                    fontSize   = 16.sp,
                    modifier   = Modifier.padding(horizontal = 4.dp)
                )
                IconButton(onClick = {}, modifier = Modifier.size(24.dp)) {
                    Icon(imageVector = Icons.Default.Add, tint = primary, modifier = Modifier.size(14.dp), contentDescription = "+")
                }
            }
            Spacer(Modifier.width(10.dp))
            if (isLogged) Icon(
                imageVector        = Icons.Default.ShoppingCart,
                contentDescription = "Add to cart",
                tint               = primary,
                modifier           = Modifier.size(25.dp)
            )
        }

        ingredients.forEach { ingredient ->
            IngredientRow(ingredient = ingredient)
        }
        Spacer(Modifier.height(8.dp))
    }
}