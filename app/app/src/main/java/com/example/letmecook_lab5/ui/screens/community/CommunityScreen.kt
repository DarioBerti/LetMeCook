package com.example.letmecook_lab5.ui.screens.community

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.letmecook_lab5.ui.components.community.CommunityEventCard
import com.example.letmecook_lab5.ui.components.community.CommunityFeedSwitcher
import com.example.letmecook_lab5.viewModel.CommunityViewModel

@Composable
fun CommunityScreen(
    uiState: CommunityViewModel.CommunityUiState,
    onFollowingClick: () -> Unit,
    onPopularClick: () -> Unit,
    onRecipeClick: (String) -> Unit,
    onUserClick: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        CommunityFeedSwitcher(
            showFollowing = uiState.showFollowing,
            onFollowingClick = onFollowingClick,
            onPopularClick = onPopularClick
        )
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(
                uiState.events,
                key = { it.id }
            ) { event ->
                CommunityEventCard(
                    event = event,
                    onRecipeClick = onRecipeClick,
                    onUserClick = onUserClick
                )
            }
        }
    }

}