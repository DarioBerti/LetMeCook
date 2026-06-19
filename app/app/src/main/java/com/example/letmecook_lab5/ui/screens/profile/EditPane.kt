package com.example.letmecook_lab5.ui.screens.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.letmecook_lab5.model.User
import com.example.letmecook_lab5.viewModel.ProfileViewModel.FormValidation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

enum class BottomMenuStates { Hidden, Dietary, Cuisine, Ingredients }

private val preferencesOptions = listOf("Pescatarian", "Vegan", "Vegetarian", "Gluten-free", "Carnivore", "Halal", "Seasonal")
private val cuisinesOptions    = listOf("Italian", "Japanese", "Mexican", "Chinese", "American", "English", "French")
private val ingredientsOptions = listOf(
    "Garlic", "Onion", "Extra virgin olive oil", "Fine salt", "Black pepper",
    "Peeled tomatoes", "Pasta", "Carnaroli rice", "Chicken breast", "Ground beef",
    "Eggs", "Butter", "Whole milk", "Parmesan cheese", "Fresh basil",
    "Carrots", "Celery", "Potatoes", "All-purpose flour", "Sugar"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun EditPane(
    user: User,
    validation: FormValidation,
    isCameraOpen: Boolean,
    onNameChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onOpenCamera: () -> Unit,
    onTakePhoto: (Uri) -> Unit,
    onRemovePhoto: () -> Unit,
    onAddDietary: (String) -> Unit,
    onRemoveDietary: (String) -> Unit,
    onAddCuisine: (String) -> Unit,
    onRemoveCuisine: (String) -> Unit,
    onAddIngredient: (String) -> Unit,
    onRemoveIngredient: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var bottomState by remember { mutableStateOf(BottomMenuStates.Hidden) }

    val context = LocalContext.current
    var pendingCamera by remember { mutableStateOf(false) }
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted -> if (granted && pendingCamera) { pendingCamera = false; onOpenCamera() } }

    if (isCameraOpen) {
        CameraPreview(onPhotoTaken = onTakePhoto)
        return
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            EditPaneContent(
                user = user,
                validation = validation,
                onNameChange = onNameChange,
                onNicknameChange = onNicknameChange,
                onEmailChange = onEmailChange,
                onRequestCamera = {
                    val granted = ContextCompat.checkSelfPermission(
                        context, Manifest.permission.CAMERA
                    ) == PackageManager.PERMISSION_GRANTED
                    if (granted) onOpenCamera()
                    else { pendingCamera = true; permissionLauncher.launch(Manifest.permission.CAMERA) }
                },
                onTakePhoto = onTakePhoto,
                onRemovePhoto = onRemovePhoto
            )
            Spacer(Modifier.height(14.dp))
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(12.dp))
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                EditableProfileListSection(
                    title = "Dietary preferences:",
                    items = user.dietaryPreferences,
                    onAdd = { bottomState = BottomMenuStates.Dietary },
                    onDismiss = onRemoveDietary,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                EditableProfileListSection(
                    title = "Types of cuisine:",
                    items = user.typesOfCuisine,
                    onAdd = { bottomState = BottomMenuStates.Cuisine },
                    onDismiss = onRemoveCuisine,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
                EditableProfileListSection(
                    title = "Favorite ingredients:",
                    items = user.favoriteIngredients,
                    onAdd = { bottomState = BottomMenuStates.Ingredients },
                    onDismiss = onRemoveIngredient,
                    color = MaterialTheme.colorScheme.secondaryContainer
                )
            }
            Spacer(Modifier.height(14.dp))
        }
    }

    when (bottomState) {
        BottomMenuStates.Hidden -> {}
        BottomMenuStates.Dietary -> PreferencesBottomMenu(
            list = preferencesOptions.filter { it !in user.dietaryPreferences },
            onDismiss = { bottomState = BottomMenuStates.Hidden },
            sheetState = sheetState, scope = scope,
            onAdd = { onAddDietary(it); bottomState = BottomMenuStates.Hidden }
        )
        BottomMenuStates.Cuisine -> PreferencesBottomMenu(
            list = cuisinesOptions.filter { it !in user.typesOfCuisine },
            onDismiss = { bottomState = BottomMenuStates.Hidden },
            sheetState = sheetState, scope = scope,
            onAdd = { onAddCuisine(it); bottomState = BottomMenuStates.Hidden }
        )
        BottomMenuStates.Ingredients -> PreferencesBottomMenu(
            list = ingredientsOptions.filter { it !in user.favoriteIngredients },
            onDismiss = { bottomState = BottomMenuStates.Hidden },
            sheetState = sheetState, scope = scope,
            onAdd = { onAddIngredient(it); bottomState = BottomMenuStates.Hidden }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PreferencesBottomMenu(
    list: List<String>,
    onDismiss: () -> Unit,
    sheetState: SheetState,
    scope: CoroutineScope,
    onAdd: (String) -> Unit
) {
    ModalBottomSheet(onDismissRequest = onDismiss, sheetState = sheetState) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            items(list) { pref ->
                ListItem(
                    headlineContent = { Text(pref) },
                    modifier = Modifier.clickable {
                        scope.launch { sheetState.hide() }.invokeOnCompletion {
                            if (!sheetState.isVisible) onAdd(pref)
                        }
                    }
                )
            }
        }
    }
}

@Composable
private fun EditPaneContent(
    user: User,
    validation: FormValidation,
    onNameChange: (String) -> Unit,
    onNicknameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onRequestCamera: () -> Unit,
    onTakePhoto: (Uri) -> Unit,
    onRemovePhoto: () -> Unit
) {
    val inputTextStyle = TextStyle(color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)

    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(Modifier.height(12.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    ProfileImage(user.profileImageUri, size = 110.dp)
                    EditPhotoButton(
                        onRequestCamera = onRequestCamera,
                        onPickGallery = onTakePhoto,
                        onRemovePhoto = onRemovePhoto,
                        modifier = Modifier.align(Alignment.BottomEnd)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                OutlinedTextField(
                value = user.fullName,
                onValueChange = onNameChange,
                label = { Text("Name") },
                isError = validation.nameError.isNotBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = inputTextStyle,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            if (validation.nameError.isNotBlank())
                Text(validation.nameError, color = MaterialTheme.colorScheme.error)

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = user.nickname,
                onValueChange = onNicknameChange,
                label = { Text("Nickname") },
                isError = validation.nicknameError.isNotBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = inputTextStyle,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Unspecified,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                )
            )
            if (validation.nicknameError.isNotBlank())
                Text(validation.nicknameError, color = MaterialTheme.colorScheme.error)
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedTextField(
                value = user.email,
                onValueChange = onEmailChange,
                label = { Text("Email Address") },
                isError = validation.emailError.isNotBlank(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                textStyle = inputTextStyle,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrectEnabled = false,
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                )
            )
            if (validation.emailError.isNotBlank())
                Text(validation.emailError, color = MaterialTheme.colorScheme.error)
        }
    }
}

@Composable
private fun EditPhotoButton(
    onRequestCamera: () -> Unit,
    onPickGallery: (Uri) -> Unit,
    onRemovePhoto: () -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    val galleryLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.let(onPickGallery)
        }
    }

    Box(modifier = modifier) {
        IconButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary, CircleShape)
                .size(40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.PhotoCamera,
                contentDescription = "Change profile photo",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                text = { Text("Take a photo") },
                onClick = { expanded = false; onRequestCamera() }
            )
            DropdownMenuItem(
                text = { Text("Choose from gallery") },
                onClick = {
                    expanded = false
                    galleryLauncher.launch(
                        Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    )
                }
            )
            DropdownMenuItem(
                text = { Text("Remove photo") },
                onClick = { expanded = false; onRemovePhoto() }
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun EditableProfileListSection(
    title: String,
    items: List<String>,
    onAdd: () -> Unit,
    onDismiss: (String) -> Unit,
    color: Color
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach { item -> RemovableChip(item, onDismiss, color) }
            IconButton(onClick = onAdd) {
                Icon(Icons.Default.Add, contentDescription = "Add new item")
            }
        }
    }
}

@Composable
private fun RemovableChip(text: String, onDismiss: (String) -> Unit, color: Color) {
    InputChip(
        onClick = { onDismiss(text) },
        label = { Text(text, color = MaterialTheme.colorScheme.onBackground) },
        selected = true,
        trailingIcon = {
            Icon(
                Icons.Default.Close,
                contentDescription = "Remove",
                Modifier.size(InputChipDefaults.AvatarSize),
                tint = MaterialTheme.colorScheme.onBackground
            )
        },
        colors = InputChipDefaults.inputChipColors(
            containerColor = color,
            selectedContainerColor = color
        )
    )
}