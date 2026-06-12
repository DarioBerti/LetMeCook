package com.example.letmecook_lab5.ui.components.recipeList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.ui.components.common.ImagePlaceholder
import coil.compose.AsyncImage

@Composable
fun MyRecipeCard(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxSize(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {
            if (recipe.imageUrl.isBlank()) {
                ImagePlaceholder()
            } else {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "Immagine di ${recipe.title}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "%.1f".format(recipe.averageRating),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier= Modifier.padding(horizontal = 2.dp))

                    Text(
                        text = "€ %.2f".format(recipe.cost),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            SavedIconButton(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp))

            FlowRow(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = recipe.title,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun RecipeCard(
    recipe: Recipe,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLogged: Boolean
) {
    Card(
        onClick = { onClick(recipe.id) },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box {

            if (recipe.imageUrl.isBlank()) {
                ImagePlaceholder()
            } else {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "Immagine",
                    modifier = Modifier
                        .fillMaxSize()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "%.1f".format(recipe.averageRating),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier= Modifier.padding(horizontal = 2.dp))

                    Text(
                        text = "€ %.2f".format(recipe.cost),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }


            if (isLogged) SavedIconButton(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 12.dp, bottom = 42.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                recipe.tags.forEach { tag ->
                    Text(
                        text = tag,
                        color = Color.White,
                        style = MaterialTheme.typography.labelMedium,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }

            }

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}


@Composable
fun HomeRecipeCard(
    recipe: Recipe,
    onClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    isLogged: Boolean
) {
    Card(
        onClick = { onClick(recipe.id) },
        modifier = modifier.padding(horizontal = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Box (modifier = Modifier.fillMaxSize()){

            if (recipe.imageUrl.isBlank()) {
                ImagePlaceholder()
            } else {
                AsyncImage(
                    model = recipe.imageUrl,
                    contentDescription = "Immagine",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp),
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(8.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
            ){

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(horizontal = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = "Star",
                        tint = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "%.1f".format(recipe.averageRating),
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }


            if (isLogged) SavedIconButton(modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(2.dp))

            Row(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
                        )
                    )
                    .padding(start = 12.dp, end = 12.dp, bottom = 12.dp, top = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = recipe.title,
                    color = Color.White,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
fun SavedIconButton(
    modifier: Modifier = Modifier
) {
    var isToggled by rememberSaveable { mutableStateOf(false) }

    IconButton(
        onClick = { isToggled = !isToggled },
        modifier = modifier,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.White
        )
    ) {
        Icon(
            imageVector = if (isToggled) Icons.Filled.Bookmark else Icons.Outlined.BookmarkBorder,

            contentDescription = if (isToggled) "Rimuovi dai salvati" else "Salva ricetta",
            tint = MaterialTheme.colorScheme.primary
        )
    }
}