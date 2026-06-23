package com.example.letmecook_lab5.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.navigation.MainActions
import com.example.letmecook_lab5.ui.components.recipeList.HomeRecipeCard
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush

import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.ui.components.recipeList.RecipeCard


@Composable
fun HomeScreen(
    actions: MainActions,
    topTen: List<Recipe>,
    newRecipes: List<Recipe>,
    fastRecipes: List<Recipe>,
    isLogged: Boolean
) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
    ) {

        Text(
            text = "HOT RIGHT NOW",
            style = MaterialTheme.typography.labelLarge.copy(letterSpacing = 1.sp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
        Text(
            text = "Trending Now",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.Black,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        TopTenRow(
            recipes = topTen,
            onClick = actions.openRecipe,
            modifier = Modifier.width(330.dp).height(330.dp),
            isLogged = isLogged
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "FRESHLY ADDED",
            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )
        Row( modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "New Recipes",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp).align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "View All",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp, top = 4.dp).align(Alignment.CenterVertically)
                    .clickable { actions.goNewRecipes() }
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        RecipesRow(
            recipes = newRecipes,
            onClick = actions.openRecipe,
            modifier = Modifier.width(230.dp).height(160.dp),
            isLogged = isLogged
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "QUICK BITES",
            style = MaterialTheme.typography.labelMedium.copy(letterSpacing = 1.sp),
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(start = 16.dp)
        )

        Row( modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Fast Recipes",
                style = MaterialTheme.typography.titleLarge,
                color = Color.Black,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp).align(Alignment.CenterVertically)
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = "View All",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(end = 16.dp, top = 4.dp).align(Alignment.CenterVertically)
                    .clickable(onClick = actions.goFastRecipes)
            )

        }

        Spacer(modifier = Modifier.height(16.dp))

        RecipesRow(
            recipes = fastRecipes,
            modifier = Modifier.width(230.dp).height(160.dp),
            onClick = actions.openRecipe,
            isLogged = isLogged
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (isLogged) ShareRecipeButton(onClick = actions.goPublishedRecipes)
    }
}

@Composable
fun ShareRecipeButton(
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 32.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(32.dp)).clip(RoundedCornerShape(32.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                    )
                )
                .clickable { onClick() }
                .padding(horizontal = 24.dp, vertical = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "#ShareYourRecipe",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.ExtraBold
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Publish your own recipe to help the whole LetMeCook community!",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Share recipe",
                tint = Color.White,
                modifier = Modifier
                    .padding(start = 16.dp)
                    .size(28.dp)
            )
        }
    }
}

@Composable
fun TopTenRow(
    recipes: List<Recipe>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    isLogged: Boolean
) {
    LazyRow() {
        items(recipes) { recipe ->
            RecipeCard(
                recipe = recipe,
                modifier = modifier,
                onClick = onClick,
                isLogged = isLogged
            )
        }
    }
}
@Composable
fun RecipesRow(
    recipes: List<Recipe>,
    modifier: Modifier = Modifier,
    onClick: (String) -> Unit = {},
    isLogged: Boolean
) {
    LazyRow() {
        items(recipes) { recipe ->
            HomeRecipeCard(
                recipe = recipe,
                modifier = modifier,
                onClick = onClick,
                isLogged = isLogged
            )
        }
    }
}