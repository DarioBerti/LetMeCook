package com.example.letmecook_lab5.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Person
import com.example.letmecook_lab5.model.User
import com.example.letmecook_lab5.session.SessionManager
import com.example.letmecook_lab5.viewModel.ProfileViewModel.ProfileUiState
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.collectAsState
import com.example.letmecook_lab5.auth.SessionManagerFacade


// Layout per modalità verticale
@Composable
fun PortraitLayout(
    user: User,
    maxHeight: Dp,
    isOwner: Boolean,
    onPublishedClick: () -> Unit,
    onCookedClick: () -> Unit,
    onSavedClick: () -> Unit,
    onEditClick: (() -> Unit)? = null,
    onLogoutClick: () -> Unit
)
{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            ProfileInfo(
                user = user,
                isOwner = isOwner,
                onSavedClick = onSavedClick,
                onCookedClick = onCookedClick,
                onPublishedClick = onPublishedClick,
                onLogoutClick = onLogoutClick,
                onEditClick = onEditClick
            )
        }
    }
}

// Lavout per modalità orizzontale
@Composable
fun LandscapeLayout(
    user: User,
    maxHeight: Dp,
    isOwner: Boolean,
    onPublishedClick: () -> Unit,
    onCookedClick: () -> Unit,
    onSavedClick: () -> Unit,
    onEditClick: (() -> Unit)? = null,
    onLogoutClick: () -> Unit
)
{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {
        item {
            ProfileInfo(
                user = user,
                isOwner = isOwner,
                onSavedClick = onSavedClick,
                onCookedClick = onCookedClick,
                onPublishedClick = onPublishedClick,
                onLogoutClick = onLogoutClick,
                onEditClick = onEditClick
            )
        }
    }
}
@Composable
fun ProfileScreen(
    uiState: ProfileUiState,
    onEditClick: () -> Unit,
    onDoneClick: () -> Unit,
    onCancelEdit: () -> Unit,
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
    onRemoveIngredient: (String) -> Unit,
    onPublishedClick: () -> Unit,
    onCookedClick: () -> Unit,
    onSavedClick: () -> Unit,
    onBack: () -> Unit,
    onLogoutClick: () -> Unit
) {
    val displayUser = uiState.draft ?: uiState.user ?: return
    val currentUid = SessionManagerFacade.currentUser.collectAsState().value?.uid
    val isOwner = displayUser.id == currentUid
    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        if (uiState.isEditing && isOwner) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onCancelEdit,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) { Text("Cancel", fontWeight = FontWeight.Bold) }
                    Button(onClick = onDoneClick) { Text("Done") }
                }
                EditPane(
                    user = displayUser,
                    validation = uiState.validation,
                    isCameraOpen = uiState.isCameraOpen,
                    onNameChange = onNameChange,
                    onNicknameChange = onNicknameChange,
                    onEmailChange = onEmailChange,
                    onOpenCamera = onOpenCamera,
                    onTakePhoto = onTakePhoto,
                    onRemovePhoto = onRemovePhoto,
                    onAddDietary = onAddDietary,
                    onRemoveDietary = onRemoveDietary,
                    onAddCuisine = onAddCuisine,
                    onRemoveCuisine = onRemoveCuisine,
                    onAddIngredient = onAddIngredient,
                    onRemoveIngredient = onRemoveIngredient
                )
            }
        } else {
            val isLandscape = maxWidth > maxHeight
            if (isLandscape) {
                LandscapeLayout(
                    user = displayUser,
                    maxHeight = maxHeight,
                    isOwner = isOwner,
                    onEditClick = if (isOwner) onEditClick else null,
                    onSavedClick = onSavedClick,
                    onCookedClick = onCookedClick,
                    onPublishedClick = onPublishedClick,
                    onLogoutClick = onLogoutClick
                )
            } else {
                PortraitLayout(
                    user = displayUser,
                    maxHeight = maxHeight,
                    isOwner = isOwner,
                    onEditClick = if (isOwner) onEditClick else null,
                    onSavedClick = onSavedClick,
                    onCookedClick = onCookedClick,
                    onPublishedClick = onPublishedClick,
                    onLogoutClick = onLogoutClick
                )
            }
        }
    }
}


@Composable
// Composable riutilizzabile per le info "culinarie"
fun ProfileListSection(title: String, items: List<String>, color: Color) {
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )

        if (items.isEmpty()) {
            Text(
                text        = "No preferences yet",
                fontSize    = 12.sp,
                color       = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(items) { item ->
                    Box(
                        modifier = Modifier
                            .background(
                                color = color,
                                shape = RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = item,
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            }
        }
    }
}

// Composable ritilizzabile per le info su numero followers/following
@Composable
fun StatBox(title: String, value: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.primary, RoundedCornerShape(20.dp))
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            fontSize = 14.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimary,
            lineHeight = 14.sp
        )
        Text(
            text = title,
            fontSize = 9.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onPrimary,
            lineHeight = 9.sp
        )

    }
}

@Composable
fun ForYouBox(title: String, icon: @Composable () -> Unit, onClick: () -> Unit, modifier: Modifier){
    Column(
        modifier = modifier
            .shadow(4.dp, RoundedCornerShape(20.dp))
            .background(MaterialTheme.colorScheme.surface, RoundedCornerShape(20.dp))
            .padding(vertical = 8.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        icon()
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.Black,
            lineHeight = 10.sp
        )
    }
}

// Composable riutilizzabile x la foto profilo
@Composable
fun ProfileImage(imageUri: String?, onEditClick: (() -> Unit)? = null, size: Dp = 150.dp) {
    Box(modifier = Modifier.size(size)) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Immagine del profilo dell'utente",
                modifier = Modifier
                    .size(size)
                    .border(width = 3.dp, color = MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(size)
                    .border(width = 3.dp, color = MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "User placeholder",
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.size(64.dp)
                )
            }
        }
        if (onEditClick != null) {
            IconButton(
                onClick = onEditClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(36.dp)
                    .background(MaterialTheme.colorScheme.surface, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Edit,
                    contentDescription = "Edit profile",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

// Composable riutilizzabile x le info profilo
@Composable
fun ProfileInfo(
    user: User,
    isOwner: Boolean,
    onSavedClick: () -> Unit,
    onCookedClick: () -> Unit,
    onPublishedClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onEditClick: (() -> Unit)? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Header: picture on the left, name/nickname + stats on the right
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            ProfileImage(
                imageUri = user.profileImageUri,
                onEditClick = onEditClick,
                size = 110.dp
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text        = user.fullName,
                    fontSize    = 20.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text        = user.cookingRole,
                    fontSize    = 14.sp,
                    fontWeight  = FontWeight.SemiBold,
                    color       = MaterialTheme.colorScheme.primary
                )
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatBox(
                        "FOLLOWERS",
                        user.followers.toString(),
                        modifier = Modifier.weight(1f)
                    )
                    StatBox(
                        "FOLLOWING",
                        user.following.toString(),
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
        // For you
        if(isOwner){
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ForYouBox(
                    title = "PUBLISHED",
                    icon = {
                        Icon(Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Published",
                            tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.weight(1f),
                    onClick = onPublishedClick
                )
                ForYouBox(
                    title = "COOKED",
                    icon = {
                        Icon(Icons.Default.Restaurant,
                            contentDescription = "Cooked",
                            tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.weight(1f),
                    onClick = onCookedClick
                )
                ForYouBox(
                    title = "SAVED",
                    icon = {
                        Icon(Icons.Default.BookmarkBorder,
                            contentDescription = "Saved",
                            tint = MaterialTheme.colorScheme.primary)
                    },
                    modifier = Modifier.weight(1f),
                    onClick = onSavedClick
                )
            }
        }
        Spacer(Modifier.height(14.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 8.dp, bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            ProfileListSection("Dietary preferences:",
                user.dietaryPreferences,
                color = MaterialTheme.colorScheme.secondaryContainer)
            ProfileListSection("Types of cuisine:",
                user.typesOfCuisine,
                color = MaterialTheme.colorScheme.secondaryContainer)
            ProfileListSection("Favorite ingredients:",
                user.favoriteIngredients,
                color = MaterialTheme.colorScheme.secondaryContainer)
        }
        Spacer(Modifier.height(14.dp))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(12.dp))
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp, bottom = 16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text        = "Username: ",
                    fontSize    = 14.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = Color.Black
                )
                Text(
                    text        = "@${user.nickname}",
                    fontSize    = 14.sp,
                    fontWeight  = FontWeight.Medium,
                    color       = Color.Black
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(top = 4.dp)
            ) {
                Text(
                    text        = "Email: ",
                    fontSize    = 14.sp,
                    fontWeight  = FontWeight.Bold,
                    color       = Color.Black
                )
                Text(
                    text        = user.email,
                    fontSize    = 14.sp,
                    fontWeight  = FontWeight.Medium,
                    color       = Color.Black
                )
            }
            Button(
                onClick = onLogoutClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text("Logout")
            }
        }
        Spacer(Modifier.height(14.dp))
    }
}


