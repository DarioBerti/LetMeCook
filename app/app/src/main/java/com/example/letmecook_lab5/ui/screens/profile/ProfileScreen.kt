package com.example.letmecook_lab5.ui.screens.profile

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import android.net.Uri
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(maxHeight * 1f / 3f),
                contentAlignment = Alignment.Center
            ) {
                ProfileImage(user.profileImageUri, onEditClick = onEditClick)
            }
        }
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = maxHeight * 2f / 3f)
            ) {
                ProfileInfo(
                    user = user,
                    isOwner = isOwner,
                    onSavedClick = onSavedClick,
                    onCookedClick = onCookedClick,
                    onPublishedClick = onPublishedClick,
                    onLogoutClick = onLogoutClick
                )
            }
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
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            ProfileImage(user.profileImageUri, onEditClick = onEditClick)
        }
        Box(
            modifier = Modifier
                .weight(2f)
                .fillMaxHeight(),
            contentAlignment = Alignment.Center
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                item { ProfileInfo(
                    user = user,
                    isOwner = isOwner,
                    onSavedClick = onSavedClick,
                    onCookedClick = onCookedClick,
                    onPublishedClick = onPublishedClick,
                    onLogoutClick = onLogoutClick
                ) }
            }
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
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (uiState.isEditing && isOwner) {
            Column(modifier = Modifier.fillMaxSize()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onCancelEdit) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Cancel",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
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
            .padding(vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface
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
            .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(20.dp))
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Text(
            text = value,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = MaterialTheme.colorScheme.onBackground,
            lineHeight = 16.sp
        )
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 10.sp
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
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 10.sp
        )
    }
}

// Composable riutilizzabile x la foto profilo
@Composable
fun ProfileImage(imageUri: String?, onEditClick: (() -> Unit)? = null) {
    Box(modifier = Modifier.size(150.dp)) {
        if (imageUri != null) {
            AsyncImage(
                model = imageUri,
                contentDescription = "Immagine del profilo dell'utente",
                modifier = Modifier
                    .size(150.dp)
                    .border(width = 3.dp, color = MaterialTheme.colorScheme.primary, CircleShape)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .size(150.dp)
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
    onLogoutClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        // Full name
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text        = user.fullName,
                fontSize    = 22.sp,
                fontWeight  = FontWeight.Bold,
                color       = MaterialTheme.colorScheme.onBackground
            )
        }
        // Cooking role
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(vertical = 2.dp),
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(
                text        = user.cookingRole,
                fontSize    = 16.sp,
                fontWeight  = FontWeight.SemiBold,
                color       = MaterialTheme.colorScheme.primary
            )
        }
        Button(
            onClick = onLogoutClick,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text("Logout")
        }
        // Followers + Following
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
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
        // Nickname
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text        = "@${user.nickname}",
                fontSize    = 16.sp,
                fontWeight  = FontWeight.SemiBold,
                color       = MaterialTheme.colorScheme.onBackground
            )
        }
        // Email
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text        = user.email,
                fontSize    = 14.sp,
                fontWeight  = FontWeight.Medium,
                color       = MaterialTheme.colorScheme.onSurface
            )
        }
        // For you
        if(isOwner){
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp),
            ) {
                Text(
                    text        = "For you",
                    fontSize    = 16.sp,
                    fontWeight  = FontWeight.SemiBold,
                    color       = MaterialTheme.colorScheme.onBackground
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth()
                    .padding(vertical = 4.dp),
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
        // Preferences
        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            Text(
                text        = "Preferences",
                fontSize    = 16.sp,
                fontWeight  = FontWeight.SemiBold,
                color       = MaterialTheme.colorScheme.onBackground
            )
        }
        ProfileListSection("DIETARY PREFERENCES",
            user.dietaryPreferences,
            color = MaterialTheme.colorScheme.primaryContainer)
        ProfileListSection("TYPES OF CUISINE",
            user.typesOfCuisine,
            color = MaterialTheme.colorScheme.secondaryContainer)
        ProfileListSection("FAVORITE INGREDIENTS",
            user.favoriteIngredients,
            color = MaterialTheme.colorScheme.tertiaryContainer)
    }
}


