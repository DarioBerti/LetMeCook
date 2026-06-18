package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelFactoryDsl
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class UserListViewModel(
    private val userRepository: UserRepository,
    private val userId: String,
    private val showFollowers: Boolean
) : ViewModel() {
    data class UserListUiState(
        val users: List<User> = emptyList(),
        val filteredUsers: List<User> = emptyList(),
        val searchText: String = "",
        val isLoading: Boolean = true
    )

    private val _uiState = MutableStateFlow(UserListUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val flowUsers =
                if (showFollowers)
                    userRepository.getFollowers(userId = userId)
                else
                    userRepository.getFollowing(userId = userId)

            flowUsers.collect { users ->
                _uiState.update {
                    it.copy(
                        users = users,
                        filteredUsers = users,
                        isLoading = false
                    )
                }
            }
        }
    }

    fun onSearchChange(text: String) {
        _uiState.update { state ->
            state.copy(
                searchText = text,
                filteredUsers = state.users.filter {
                    it.nickname.contains(text, true) ||
                    it.fullName.contains(text, true)
                }
            )
        }
    }

    companion object {
        fun Factory(
            userId: String,
            showFollowers: Boolean
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val userRepository = application.container.userRepository

                UserListViewModel(
                    userId = userId,
                    userRepository = userRepository,
                    showFollowers = showFollowers
                )
            }
        }
    }

}