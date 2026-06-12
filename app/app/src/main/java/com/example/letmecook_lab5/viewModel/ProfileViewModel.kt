package com.example.letmecook_lab5.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.letmecook_lab5.model.User
import com.example.letmecook_lab5.domain.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.repository.FirebaseStorageRepository
import com.example.letmecook_lab5.session.SessionManager

class ProfileViewModel(
    private val userRepository: UserRepository,
    private val storageRepo: FirebaseStorageRepository,
    private val userId: String
) : ViewModel() {

    data class FormValidation(
        val nameError: String = "",
        val nicknameError: String = "",
        val emailError: String = "",
        val isValid: Boolean = true
    )

    data class ProfileUiState(
        val user: User? = null,
        val draft: User? = null,
        val validation: FormValidation = FormValidation(),
        val isLoading: Boolean = true,
        val isEditing: Boolean = false,
        val isCameraOpen: Boolean = false
    )

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            userRepository.getUserById(userId).collect { user ->
                _uiState.update { it.copy(user = user, isLoading = false) }
                Log.d("PROFILE_VM", "user emitted = $user")
            }
        }
    }

    fun startEditing() {
        _uiState.update {
            it.copy(
                isEditing = true,
                draft = it.user,
                validation = FormValidation()
            )
        }
    }

    fun cancelEditing() {
        _uiState.update {
            it.copy(isEditing = false, draft = null, isCameraOpen = false)
        }
    }

    private fun updateDraft(block: (User) -> User) {
        _uiState.update { s ->
            val d = s.draft ?: return@update s
            s.copy(draft = block(d))
        }
    }

    fun setName(value: String)     = updateDraft { it.copy(fullName = value) }
    fun setNickname(value: String) = updateDraft { it.copy(nickname = value) }
    fun setEmail(value: String)    = updateDraft { it.copy(email = value) }

    fun openCamera()  { _uiState.update { it.copy(isCameraOpen = true) } }
    fun closeCamera() { _uiState.update { it.copy(isCameraOpen = false) } }

    fun onImageCaptured(uri: Uri) {
        _uiState.update { s ->
            val d = s.draft ?: return@update s
            s.copy(draft = d.copy(profileImageUri = uri.toString()), isCameraOpen = false)
        }
    }

    fun removePhoto() = updateDraft { it.copy(profileImageUri = null) }

    fun addDietaryPreference(p: String)    = updateDraft { it.copy(dietaryPreferences = it.dietaryPreferences + p) }
    fun removeDietaryPreference(p: String) = updateDraft { it.copy(dietaryPreferences = it.dietaryPreferences - p) }

    fun addTypeOfCuisine(p: String)    = updateDraft { it.copy(typesOfCuisine = it.typesOfCuisine + p) }
    fun removeTypeOfCuisine(p: String) = updateDraft { it.copy(typesOfCuisine = it.typesOfCuisine - p) }

    fun addFavoriteIngredient(p: String)    = updateDraft { it.copy(favoriteIngredients = it.favoriteIngredients + p) }
    fun removeFavoriteIngredient(p: String) = updateDraft { it.copy(favoriteIngredients = it.favoriteIngredients - p) }

    fun save() {
        val draft = _uiState.value.draft ?: return

        val nameError     = if (draft.fullName.isBlank()) "Name cannot be blank" else ""
        val nicknameError = if (draft.nickname.isBlank()) "Nickname cannot be blank" else ""
        val emailError    = if (!draft.email.isValidEmail()) "Invalid email format" else ""
        val isValid = nameError.isBlank() && nicknameError.isBlank() && emailError.isBlank()

        _uiState.update {
            it.copy(
                validation = FormValidation(nameError, nicknameError, emailError, isValid)
            )
        }

        if (!isValid) return

        viewModelScope.launch {
            val uploadedUrl = storageRepo.uploadIfLocal(
                draft.profileImageUri,
                "profile_images/$userId.jpg"
            )
            userRepository.updateUser(draft.copy(profileImageUri = uploadedUrl))
            _uiState.update { it.copy(isEditing = false, draft = null, isCameraOpen = false) }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val userRepository = application.container.userRepository
                val storageRepository = application.container.storageRepository
                val userId = SessionManagerFacade.currentUser.value?.uid
                    ?: error("ProfileViewModel created with no logged-in user")
                ProfileViewModel(userRepository, storageRepository, userId)
            }
        }
    }
}

private fun String.isValidEmail(): Boolean {
    if (isBlank()) return false
    val regex = "[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}".toRegex()
    return regex.matches(this)
}