package com.example.letmecook_lab5.ui.screens.groceries

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.letmecook_lab5.model.CartIngredient
import com.example.letmecook_lab5.model.CartRecipe
import com.example.letmecook_lab5.ui.components.groceries.GroceriesTabButton
import com.example.letmecook_lab5.ui.components.groceries.ServingsStepper
import com.example.letmecook_lab5.viewModel.GroceriesTabs
import com.example.letmecook_lab5.viewModel.groceriesUiState
import kotlin.collections.forEach

@Composable
fun GroceriesScreen(
    uiState: groceriesUiState,
    groceriesByRecipes: List<CartRecipe>,
    updateRecipeServings: (String, Int) -> Unit,
    toggleIsChecked: (String, String) -> Unit,
    onTabSelected: (GroceriesTabs) -> Unit,
    onRecipeDelete: (String) -> Unit,
    onRecipeClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            GroceriesTabButton(
                label = "By Recipe",
                selected = uiState.tabSelected == GroceriesTabs.BY_RECIPE,
                modifier = Modifier.weight(1f),
                isFirst = true,
                onClick = { onTabSelected(GroceriesTabs.BY_RECIPE) }
            )
            GroceriesTabButton(
                label = "All Recipes",
                selected = uiState.tabSelected == GroceriesTabs.ALL_INGREDIENTS,
                modifier = Modifier.weight(1f),
                isFirst = false,
                onClick = { onTabSelected(GroceriesTabs.ALL_INGREDIENTS) }
            )
        }

        if (uiState.tabSelected == GroceriesTabs.BY_RECIPE)
            groceriesByRecipes.forEach { recipe ->
                IngredientsByRecipe(
                    recipe = recipe,
                    updateRecipeServings = updateRecipeServings,
                    toggleIsChecked = toggleIsChecked,
                    removeRecipe = onRecipeDelete,
                    onRecipeClick = onRecipeClick
                )
                Spacer(modifier = Modifier.padding(vertical = 8.dp))
            }
        else {
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "${groceriesByRecipes.flatMap { it.ingredients }.size} ITEMS",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(50)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            groceriesByRecipes.flatMap { recipe ->
                recipe.ingredients.map { ingredient ->
                    recipe.recipeId to ingredient
                }
            }.sortedBy { it.second.name }
                .forEach { (recipeId, ingredient) ->
                IngredientCart(
                    recipeId = recipeId,
                    ingredient = ingredient,
                    toggleIsChecked = toggleIsChecked
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
/*
        Row(
            modifier = Modifier.fillMaxWidth()
        ){
            if (groceriesByRecipes.flatMap { it.ingredients }.isNotEmpty()){
                Text("${(groceriesByRecipes.flatMap { it.ingredients }.count { it.isChecked }.toFloat() / groceriesByRecipes.flatMap { it.ingredients }.size.toFloat() * 100).toInt()} % Items Collected")
            }
            else {
                Text("No items")
            }

        }
 */
    }
}

@Composable
fun IngredientCart(
    recipeId: String,
    ingredient: CartIngredient,
    toggleIsChecked: (String, String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(50),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { toggleIsChecked(recipeId, ingredient.name) }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = if (ingredient.isChecked) Icons.Filled.CheckCircle else Icons.Outlined.Circle,
                contentDescription = "Toggle ingredient",
                tint = if (ingredient.isChecked) MaterialTheme.colorScheme.primary else Color.LightGray,
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = ingredient.name,
                    fontWeight = FontWeight.Bold,
                    textDecoration = if (ingredient.isChecked) TextDecoration.LineThrough else null,
                    color = if (ingredient.isChecked) Color.Gray else Color.Black
                )
                Text(
                    text = "${ingredient.quantity} ${ingredient.unit}",
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
/*
            AsyncImage(
                model = ingredient.imageUrl,
                contentDescription = "Ingredient Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
*/
        }
    }
}



@Composable
fun IngredientCheckbox(isChecked: Boolean) {
    if (isChecked) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = "Comprato",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
    } else {
        Icon(
            imageVector = Icons.Default.RadioButtonUnchecked,
            contentDescription = "Da comprare",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun IngredientsByRecipe(
    recipe: CartRecipe,
    removeRecipe: (String) -> Unit,
    updateRecipeServings: (String, Int) -> Unit,
    toggleIsChecked: (String, String) -> Unit,
    onRecipeClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 24.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = recipe.recipeImage,
                    contentDescription = "Recipe Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(16.dp)),
                    alignment = Alignment.Center
                )
                Spacer(modifier = Modifier.width(16.dp))

                Column {
                    Text(
                        text = recipe.recipeTitle,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black,
                        modifier = Modifier.clickable { onRecipeClick(recipe.recipeId) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "SERVINGS",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.width(12.dp))

                        ServingsStepper(
                            servings = recipe.servings,
                            onDecrease = { if (recipe.servings > 1) updateRecipeServings(recipe.recipeId, recipe.servings - 1) },
                            onIncrease = { updateRecipeServings(recipe.recipeId, recipe.servings + 1) }
                        )
                    }
                }
            }


            IconButton(
                onClick = { removeRecipe(recipe.recipeId) },
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(24.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    tint = Color.LightGray,
                    modifier = Modifier.size(18.dp),
                    contentDescription = "Rimuovi ricetta"
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        HorizontalDivider(color = Color.LightGray, thickness = 1.dp)

        recipe.ingredients.forEachIndexed { index, ingredient ->
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(onClick = { toggleIsChecked(recipe.recipeId, ingredient.name) })
                        .padding(vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IngredientCheckbox(
                        isChecked = ingredient.isChecked
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Row(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = ingredient.name,
                            fontSize = 18.sp,
                            color = Color.Black,
                            textDecoration = if (ingredient.isChecked) TextDecoration.LineThrough else null,
                        )
                        Text(
                            text = "%.1f %s".format(ingredient.quantity, ingredient.unit),
                            fontSize = 16.sp,
                            color = Color.Black,
                            textAlign = TextAlign.End
                        )
                    }
                }
                HorizontalDivider(
                    color = Color.LightGray,
                    thickness = 1.dp,
                    modifier = Modifier.padding(start = 40.dp)
                )
            }
        }
        Spacer(Modifier.height(8.dp))
    }
}


@Composable
fun GroceriesPlaceholder() {
    Column(
        modifier = Modifier.fillMaxSize()
    ){
        Text("Log in to see your groceries!", modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

