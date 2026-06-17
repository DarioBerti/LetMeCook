package com.example.letmecook_lab5.ui.screens.recipeList

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.AcUnit
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.CheckBoxOutlineBlank
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingBasket
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.letmecook_lab5.model.Recipe
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.session.SessionManager
import com.example.letmecook_lab5.ui.components.common.ImagePlaceholder
import com.example.letmecook_lab5.viewModel.ShowRecipeDetailsUiState
import com.example.letmecook_lab5.viewModel.ShowRecipeDetailsViewModel
import com.example.letmecook_lab5.model.Ingredient
import com.example.letmecook_lab5.model.Step
import coil.compose.AsyncImage
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.Review
import com.example.letmecook_lab5.ui.components.recipe.photos.CommunityPhotoDialog
import com.example.letmecook_lab5.ui.components.recipe.reviews.ReviewItem
import com.example.letmecook_lab5.ui.components.recipe.reviews.ReviewFormDialog
import com.example.letmecook_lab5.viewModel.ReviewViewModel
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.DisposableEffect
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.ui.components.recipe.recipeProposal.AuthorRow
import com.example.letmecook_lab5.ui.components.recipe.recipeProposal.ImageSection
import com.example.letmecook_lab5.ui.components.recipe.recipeProposal.IngredientsSection
import com.example.letmecook_lab5.ui.screens.utils.RecipeOptions
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material3.ElevatedCard
import kotlinx.coroutines.delay

@Composable
fun ShowRecipeProposalDetailsRoute(
    reviewViewModel: ReviewViewModel,
    onBack: () -> Unit,
    onEdit: () -> Unit,
    onCreateNew: () -> Unit,
    onViewAllReviewsClick: () -> Unit,
    onViewAllPhotosClick: () -> Unit,
    viewModel: ShowRecipeDetailsViewModel = viewModel(
        factory = ShowRecipeDetailsViewModel.Factory
    ),
    isLogged: Boolean,
    currentUserId: String?
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recipe = uiState.recipe

    if (uiState.isDeleted) {
        LaunchedEffect(Unit) { onBack() }
        return
    }

    val reviews by reviewViewModel.recipeReviews.collectAsState()
    val averageRating by reviewViewModel.averageRating.collectAsState()

    if(recipe != null) {
        ShowRecipeProposalDetails(
            recipe = recipe,
            reviews = reviews,
            averageRating = averageRating,
            onDeleteConfirmed = viewModel::deleteRecipe,
            onEdit = onEdit,
            onCreateNew = onCreateNew,
            onViewAllReviewsClick = onViewAllReviewsClick,
            onViewAllPhotosClick = onViewAllPhotosClick,
            onAddReview = reviewViewModel::addReview,
            onDeleteReview = reviewViewModel::deleteReview,
            onUpdateReview = reviewViewModel::updateReview,
            onSaveToCollections = viewModel::saveToCollections,
            collections = uiState.collections,
            isLogged = isLogged,
            currentUserId = currentUserId,
            ownerName = uiState.ownerName,
            ownerAvatar = uiState.ownerAvatar,
            onAddToGroceryList = viewModel::addToGroceryList
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowRecipeProposalDetails(
    recipe: Recipe,
    reviews: List<Review>,
    averageRating: Double,
    onDeleteConfirmed: () -> Unit,
    onEdit: () -> Unit,
    onCreateNew: () -> Unit,
    onViewAllReviewsClick: () -> Unit,
    onViewAllPhotosClick: () -> Unit,
    onAddReview: (Review) -> Unit,
    onDeleteReview: (String) -> Unit,
    onUpdateReview: (Review) -> Unit,
    collections: List<Collection>,
    onSaveToCollections: (List<String>) -> Unit,
    isLogged: Boolean,
    currentUserId: String?,
    ownerName: String,
    ownerAvatar: String?,
    onAddToGroceryList: (List<Ingredient>, Int) -> Unit
) {
    Log.d("Recipe", recipe.toString())
    val onBackground = MaterialTheme.colorScheme.onBackground
    var showOptionsMenu by remember { mutableStateOf(false) }
    var showSaveDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmation by remember { mutableStateOf(false) }
    var showReviewSheet by remember { mutableStateOf(false) }
    var showCartConfirmation by remember { mutableStateOf(false) }
    val isSaved = collections.any { recipe.id in it.recipeIds }
    val isOwner = currentUserId != null && recipe.ownerId == currentUserId
    val hasReviewed = currentUserId != null && reviews.any { it.authorId == currentUserId }

    // publish owner status to the shared top bar, and reset on leave
    DisposableEffect(isOwner) {
        RecipeOptions.isOwner = isOwner
        onDispose {
            RecipeOptions.isOwner = false
            RecipeOptions.openMenu = false
        }
    }

    LaunchedEffect(RecipeOptions.openMenu) {
        if (RecipeOptions.openMenu) {
            showOptionsMenu = true
            RecipeOptions.openMenu = false
        }
    }

    if (showSaveDialog) {
        SaveRecipeDialog(
            recipe = recipe,
            collections = collections,
            onSave = onSaveToCollections,
            onDismiss = { showSaveDialog = false }
        )
    }

    if (showReviewSheet) {
        Dialog(
            onDismissRequest = { showReviewSheet = false }
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                shape = RoundedCornerShape(32.dp),
                tonalElevation = 8.dp,
                color = Color.White
            ) {
                ReviewFormDialog(
                    onDismiss = { showReviewSheet = false },
                    onPublish = onAddReview,
                    recipeId = recipe.id,
                    userId = SessionManagerFacade.currentUser.value?.uid ?: "101"
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            item {
                ImageSection(
                    imageUrl = recipe.imageUrl,
                    rating   = averageRating.toFloat(),
                    isSaved  = isSaved,
                    onSaveClick = { showSaveDialog = true },
                    onShowReviewSheet = { showReviewSheet = true },
                    hasReviewed = hasReviewed,
                    isLogged = isLogged
                )
            }
            if (isLogged) item {
                AuthorRow(authorName = ownerName, avatarUrl = ownerAvatar)
            }
            item {
                Text(
                    text       = recipe.title,
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    style = MaterialTheme.typography.headlineLarge,
                    color      = onBackground,
                    modifier   = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
            item { StatsRow(recipe) }
            item { Spacer(Modifier.height(12.dp)) }
            if (isLogged) item { CreateNewButton(onClick = onCreateNew) }
            item {
                IngredientsSection(
                    ingredients  = recipe.ingredients,
                    baseServings = recipe.servings,
                    isLogged     = isLogged,
                    onAddToCart  = { items, servings ->
                        onAddToGroceryList(items, servings)
                        showCartConfirmation = true
                    }
                )
            }
            item { Spacer(Modifier.height(12.dp)) }
            item { PreparationSection(steps = recipe.steps) }
            item { Spacer(Modifier.height(12.dp)) }
            item { StorageSection(info = recipe.storageInstructions, ownerName = ownerName) }
            item { Spacer(Modifier.height(12.dp)) }
            item { CommunityPhotosSection(
                photoReviews = reviews.filter { it.imageUrl.isNotBlank() },
                onViewAllPhotosClick = onViewAllPhotosClick
            ) }
            item { Spacer(Modifier.height(12.dp)) }
            item {
                ReviewsSection(
                    reviews = reviews,
                    onViewAllReviewsClick = onViewAllReviewsClick,
                    onDeleteReview = onDeleteReview,
                    onUpdateReview = onUpdateReview
                ) }
        }
        if (showCartConfirmation) {
            CartAddedPopup(
                modifier = Modifier.align(Alignment.BottomCenter),
                onDismiss = { showCartConfirmation = false }
            )
        }
    }

    if (showOptionsMenu) {
        AlertDialog(
            onDismissRequest = { showOptionsMenu = false },
            title = {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Recipe Options",
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showOptionsMenu = false }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close"
                        )
                    }
                }
            },
            text = {
                Column {
                    TextButton(
                        onClick = {
                            showOptionsMenu = false
                            onEdit()
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Edit Recipe",
                            color = MaterialTheme.colorScheme.primary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    TextButton(
                        onClick = { showOptionsMenu = false
                            showDeleteConfirmation = true },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "Delete Recipe",
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            },
            confirmButton = {},
            dismissButton = {}
        )
    }

    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = {
                Text(text = "Delete Recipe", fontWeight = FontWeight.Bold)
            },
            text = {
                Text(text = "Are you sure you want to delete this recipe?")
            },
            confirmButton = {
                TextButton(onClick = { showDeleteConfirmation = false; onDeleteConfirmed() }) {
                    Text("Yes", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("No", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun CartAddedPopup(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    LaunchedEffect(Unit) {
        delay(2500)
        onDismiss()
    }

    ElevatedCard(
        modifier = modifier
            .padding(12.dp)
            .fillMaxWidth(),
        onClick = onDismiss
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(
                    text = "Added to groceries",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(
                    text = "Your ingredients were added to your groceries list",
                    fontWeight = FontWeight.Normal,
                    fontSize = 12.sp,
                    color = Color.DarkGray
                )
            }
        }
    }
}

@Composable
private fun SaveRecipeDialog(
    recipe: Recipe,
    collections: List<Collection>,
    onSave: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    var selectedCollectionIds by remember {
        mutableStateOf(collections.filter { recipe.id in it.recipeIds }.map { it.id }.toSet())
    }

    LaunchedEffect(collections) {
        selectedCollectionIds = collections.filter { recipe.id in it.recipeIds }.map { it.id }.toSet()
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "Save to collection",
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close")
                }
            }
        },
        text = {
            Column {
                collections.forEach { col ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .clickable {
                                selectedCollectionIds = if (col.id in selectedCollectionIds)
                                    selectedCollectionIds - col.id
                                else
                                    selectedCollectionIds + col.id
                            }
                            .background(
                                if (col.id in selectedCollectionIds)
                                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                                else
                                    MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            )
                            .padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = col.id in selectedCollectionIds,
                            onCheckedChange = { checked ->
                                selectedCollectionIds = if (checked)
                                    selectedCollectionIds + col.id
                                else
                                    selectedCollectionIds - col.id
                            }
                        )
                        Text(
                            col.name,
                            modifier = Modifier.padding(start = 8.dp),
                            fontSize = 18.sp,
                            fontWeight = if (col.id in selectedCollectionIds) FontWeight.ExtraBold else FontWeight.SemiBold
                        )
                    }
                }
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = {
                        onSave(selectedCollectionIds.toList())
                        onDismiss()
                    },
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Save", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        dismissButton = {}
    )
}




@Composable
private fun StatsRow(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        StatItem(icon = Icons.Default.BarChart,    label = recipe.difficulty)
        StatItem(icon = Icons.Default.AccessTime,  label = "${recipe.cookingTime} MINS")
        StatItem(icon = Icons.Default.Bolt,        label = "${recipe.calories} CAL")
        StatItem(icon = Icons.Default.AttachMoney, label = "€ %.2f".format(recipe.cost))
    }
}

@Composable
fun StatItem(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    label: String
) {

    Column(
        modifier            = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = null,
            tint               = MaterialTheme.colorScheme.primary,
            modifier           = Modifier.size(25.dp)
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text      = label,
            fontSize  = 12.sp,
            color     = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
private fun CreateNewButton(onClick: () -> Unit) {
    val onTertiary = MaterialTheme.colorScheme.onTertiary

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 35.dp)
            .height(35.dp)
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(100))
            .clickable { onClick() },
        horizontalArrangement = Arrangement.Center,
        verticalAlignment     = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = Icons.Default.Edit,
            contentDescription = "Edit icon",
            tint               = onTertiary,
            modifier           = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(8.dp))
        Text(
            text       = "Create New from This",
            fontSize   = 15.sp,
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodyLarge,
            color      = onTertiary
        )
    }
}



@Composable
fun IngredientRow(
    ingredient: Ingredient,
    selected: Boolean = false,
    onToggle: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val qtyStr = if (ingredient.quantity % 1 == 0.0)
        ingredient.quantity.toInt().toString()
    else "%.1f".format(ingredient.quantity)

    val label = "$qtyStr${if (ingredient.unit.isNotEmpty()) " ${ingredient.unit}" else ""} ${ingredient.name}"

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onToggle() }
            .padding(top = 14.dp, start = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = if (selected) Icons.Default.CheckBox else Icons.Default.CheckBoxOutlineBlank,
            contentDescription = null,
            tint               = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier           = Modifier.size(18.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(text = label, fontSize = 14.sp, color = MaterialTheme.colorScheme.onBackground)
    }
}

@Composable
private fun PreparationSection(steps: List<Step>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp)
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(horizontal = 16.dp)
            .padding(top = 16.dp, bottom = 24.dp)
    ) {
        Text(
            text = "Preparation",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 14.dp)
        )
        steps.forEachIndexed { index, step ->
            StepItem(step = step, number = index + 1)
        }
    }
}

@Composable
fun StepItem(step: Step, number: Int, modifier: Modifier = Modifier) {
    val primary = MaterialTheme.colorScheme.primary
    val onPrimary = MaterialTheme.colorScheme.onPrimary
    val onBackground = MaterialTheme.colorScheme.onBackground

    Row(modifier = modifier.fillMaxWidth()) {
        Box(
            modifier         = Modifier
                .size(28.dp)
                .background(primary, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text       = number.toString(),
                fontSize   = 14.sp,
                fontWeight = FontWeight.Bold,
                color      = onPrimary
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = step.title,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                color      = onBackground
            )
            Text(
                text       = step.description,
                fontSize   = 13.sp,
                color      = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 17.sp,
                modifier   = Modifier.padding(top = 4.dp)
            )

            if (!step.photo.isNullOrEmpty()) {
                AsyncImage(
                    model = step.photo,
                    contentDescription = "Step $number image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            } else {
                ImagePlaceholder(
                    Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .padding(top = 10.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        }
    }
}

@Composable
private fun StorageSection(info: String, ownerName: String) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onSurface.copy(0.6f),
                shape = RoundedCornerShape(8.dp)
            )
            .background(MaterialTheme.colorScheme.outline, RoundedCornerShape(8.dp))
            .padding(horizontal = 16.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector        = Icons.Default.AcUnit,
            contentDescription = "Storage icon",
            tint               = MaterialTheme.colorScheme.scrim,
            modifier           = Modifier
                .size(40.dp)
                .padding(end = 14.dp)
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text       = "Storage",
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color      = MaterialTheme.colorScheme.onSurface,
            )
            Text(
                text     = info.ifBlank { "The cook $ownerName didn't specify any storage instructions :)" },
                fontSize = 13.sp,
                color    = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(top = 2.dp)
            )
        }
    }
}

@Composable
private fun CommunityPhotosSection(
    photoReviews: List<Review>,
    onViewAllPhotosClick: () -> Unit
) {
    val primary = MaterialTheme.colorScheme.primary
    val onBackground = MaterialTheme.colorScheme.onBackground
    val onSurfaceVariant = MaterialTheme.colorScheme.onSurfaceVariant
    val surfaceVariant = MaterialTheme.colorScheme.surfaceVariant

    var selectedPhotoReview by remember { mutableStateOf<Review?>(null) }

    if (selectedPhotoReview != null) {
        Dialog(
            onDismissRequest = { selectedPhotoReview = null }
        ) {
            CommunityPhotoDialog(
                review = selectedPhotoReview!!,
                onDismiss = { selectedPhotoReview = null }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Community photos",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (photoReviews.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text =
                            if (photoReviews.size == 1)
                                "(1 photo)"
                            else
                                "(${photoReviews.size} photos)",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (photoReviews.isNotEmpty()) {
                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onViewAllPhotosClick()
                    }
                )
            }
        }
        if (photoReviews.isNotEmpty()) {
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(photoReviews.take(5)) { review ->
                    AsyncImage(
                        model = review.imageUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(120.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .clickable {
                                selectedPhotoReview = review
                            },
                        contentScale = ContentScale.Crop
                    )
                }
                if (photoReviews.size > 5) {
                    item {
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(RoundedCornerShape(20.dp))
                                .background(color = Color.DarkGray)
                                .clickable { onViewAllPhotosClick() },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "+${photoReviews.size - 5}",
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.LightGray
                            )
                        }
                    }
                }
            }
        } else {
            Text(
                text = "No photos yet, be the first to share your result",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
private fun ReviewsSection(
    reviews: List<Review>,
    onViewAllReviewsClick: () -> Unit,
    onDeleteReview: (String) -> Unit,
    onUpdateReview: (Review) -> Unit
) {
    val previewReview = reviews.take(3)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(bottom = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Reviews",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (reviews.isNotEmpty()) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text =
                            if (reviews.size == 1)
                                "(1 review)"
                            else
                                "(${reviews.size} reviews)",
                        fontSize = 13.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            if (reviews.isNotEmpty()) {
                Text(
                    text = "View All",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onViewAllReviewsClick()
                    }
                )
            }
        }
        if (reviews.isNotEmpty()) {
            previewReview.forEach { review ->
                ReviewItem(
                    review = review,
                    onDelete = onDeleteReview,
                    onUpdate = onUpdateReview
                )
            }
        } else {
            Text(
                text = "No reviews yet, be the first to try this recipe",
                fontSize = 13.sp,
                color = Color.DarkGray
            )
        }
    }
}