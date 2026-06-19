package com.example.letmecook_lab5.ui.screens.savedRecipes

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.letmecook_lab5.model.Collection
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.ui.components.common.ImagePlaceholder
import com.example.letmecook_lab5.ui.components.common.TopAppLetMeCook
import com.example.letmecook_lab5.ui.components.recipeList.RecipeCard
import com.example.letmecook_lab5.viewModel.SavedRecipesUiState
import com.example.letmecook_lab5.viewModel.SavedRecipesViewModel
import com.example.letmecook_lab5.viewModel.SavedTab
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.height
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.TextButton
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage

@Composable
fun SavedRecipesRoute(
    onBack: () -> Unit,
    onCollectionClick: (String) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
            viewModel: SavedRecipesViewModel = viewModel(
        factory = SavedRecipesViewModel.Factory
    ),
    isLogged: Boolean
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    SavedRecipesScreen(
        uiState = uiState,
        onBack = onBack,
        onTabSelected = viewModel::selectTab,
        onCollectionClick = onCollectionClick,
        onRecipeClick = onRecipeClick,
        onCreateCollection = { name, desc, uri -> viewModel.createCollection(name, desc, uri) },
        onDeleteCollection = viewModel::deleteCollection,
        isLogged = isLogged
    )
}

@Composable
fun SavedRecipesScreen(
    uiState: SavedRecipesUiState,
    onBack: () -> Unit,
    onTabSelected: (SavedTab) -> Unit,
    onCollectionClick: (String) -> Unit,
    onRecipeClick: (Recipe) -> Unit,
    onCreateCollection: (String, String, String?) -> Unit,
    onDeleteCollection: (String) -> Unit,
    isLogged: Boolean
) {
    var showNewCollectionDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppLetMeCook("Saved Recipes", onClick = onBack)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TabButton(
                    label = "Collections",
                    selected = uiState.selectedTab == SavedTab.COLLECTIONS,
                    modifier = Modifier.weight(1f),
                    isFirst = true,
                    onClick = { onTabSelected(SavedTab.COLLECTIONS) }
                )
                TabButton(
                    label = "All Recipes",
                    selected = uiState.selectedTab == SavedTab.ALL_RECIPES,
                    modifier = Modifier.weight(1f),
                    isFirst = false,
                    onClick = { onTabSelected(SavedTab.ALL_RECIPES) }
                )
            }

            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }else{
                when (uiState.selectedTab) {
                    SavedTab.COLLECTIONS -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        uiState.collections.forEach { col ->
                            CollectionRow(
                                col,
                                onClick = { onCollectionClick(col.id) },
                                onDelete = { onDeleteCollection(col.id) }
                            )
                        }
                        Spacer(modifier = Modifier.height(80.dp))
                    }

                    SavedTab.ALL_RECIPES -> Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        uiState.savedRecipes.forEach { recipe ->
                            RecipeCard(recipe, onClick = { onRecipeClick(recipe) }, isLogged = isLogged)
                            Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        }
                    }
                }
            }
        }
        if (uiState.selectedTab == SavedTab.COLLECTIONS) {
            FloatingActionButton(
                onClick = { showNewCollectionDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 24.dp)
                    .widthIn(min = 200.dp),
                shape = RoundedCornerShape(20.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Text(
                    text = "New Collection",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 24.dp)
                )
            }
        }

        if (showNewCollectionDialog) {
            NewCollectionDialog(
                onDismiss = { showNewCollectionDialog = false },
                onCreate = { name, description, uri ->
                    onCreateCollection(name, description, uri)
                    showNewCollectionDialog = false
                }
            )
        }
    }
}
@Composable
private fun TabButton(
    label: String,
    selected: Boolean,
    isFirst: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    val shape = when {
        isFirst -> RoundedCornerShape(topStart = 50.dp, bottomStart = 50.dp, topEnd = 0.dp, bottomEnd = 0.dp)
        else    -> RoundedCornerShape(topStart = 0.dp, bottomStart = 0.dp, topEnd = 50.dp, bottomEnd = 50.dp)
    }

    if (selected) {
        Button(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(label, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
    } else {
        OutlinedButton(
            onClick = onClick,
            modifier = modifier,
            shape = shape,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
        ) {
            Text(label, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
        }
    }
}
@Composable
fun CollectionRow(
    collection: Collection,
    onClick: () -> Unit,
    clickable: Boolean = true,
    onDelete: (() -> Unit)? = null ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .then(if (clickable) Modifier.clickable { onClick() } else Modifier)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier.size(72.dp).clip(RoundedCornerShape(12.dp))) {
            if (collection.imageUrl != null) {
                AsyncImage(
                    model = collection.imageUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                ImagePlaceholder()
            }
        }
        Spacer(Modifier.size(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(collection.name, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
            Text(
                collection.description,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(Modifier.size(6.dp))
            Text(
                "${collection.recipeIds.size} recipes",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color.White)
                    .border(1.dp, MaterialTheme.colorScheme.primary, RoundedCornerShape(50.dp))
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
        var showConfirm by remember { mutableStateOf(false) }

        if (onDelete != null) {
            IconButton(onClick = { showConfirm = true }) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete collection",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        if (showConfirm) {
            AlertDialog(
                onDismissRequest = { showConfirm = false },
                title = { Text("Delete Collection", fontWeight = FontWeight.Bold) },
                text = { Text("Are you sure you want to delete this collection?") },
                confirmButton = {
                    TextButton(onClick = { showConfirm = false; onDelete!!() }) {
                        Text("Yes", color = MaterialTheme.colorScheme.error, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showConfirm = false }) {
                        Text("No", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun NewCollectionDialog(
    onDismiss: () -> Unit,
    onCreate: (String, String, String?) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    val isValid = name.isNotBlank() && description.isNotBlank()
    var imageUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val context = LocalContext.current
    val galleryLauncher = rememberLauncherForActivityResult(
        androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            result.data?.data?.let { pickedUri ->
                val file = java.io.File(context.cacheDir, "collection_${System.currentTimeMillis()}.jpg")
                context.contentResolver.openInputStream(pickedUri)?.use { input ->
                    file.outputStream().use { input.copyTo(it) }
                }
                imageUri = android.net.Uri.fromFile(file)
            }
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        title = {
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Text(
                    "New Collection",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onDismiss) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Close",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable {
                            galleryLauncher.launch(
                                android.content.Intent(
                                    android.content.Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            )
                        }
                ) {
                    if (imageUri != null) {
                        AsyncImage(
                            model = imageUri,
                            contentDescription = "Collection image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        ImagePlaceholder()
                    }
                    Text(
                        text = if (imageUri != null) "Tap to change photo" else "Tap to add photo",
                        color = Color.Black,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(vertical = 6.dp)
                    )
                }
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name *") },
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description *") },
                    textStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Button(
                    onClick = { onCreate(name, description, imageUri?.toString()) },
                    enabled = isValid,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.4f),
                        disabledContentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Create", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                }
            }
        },
        dismissButton = {}
    )
}