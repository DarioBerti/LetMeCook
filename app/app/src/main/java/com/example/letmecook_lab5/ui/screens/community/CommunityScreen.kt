package com.example.letmecook_lab5.ui.screens.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.letmecook_lab5.viewModel.CommunityViewModel

@Composable
fun CommunityScreen(
    uiState: CommunityViewModel.CommunityUiState,
    onFollowingClick: () -> Unit,
    onPopularClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text("Community Screen")
    }
}