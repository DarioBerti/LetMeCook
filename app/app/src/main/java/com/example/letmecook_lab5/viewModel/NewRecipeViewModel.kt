package com.example.letmecook_lab5.viewModel
import android.util.Log
import com.example.letmecook_lab5.model.Difficulty
import com.example.letmecook_lab5.model.Ingredient
import com.example.letmecook_lab5.model.Step
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.model.Recipe
import com.example.letmecook_lab5.model.calculateCost
import com.example.letmecook_lab5.model.toRecipe
import com.example.letmecook_lab5.domain.RecipeRepository
import com.example.letmecook_lab5.session.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.letmecook_lab5.domain.IngredientRepository
import com.example.letmecook_lab5.model.calculateCalories
import com.example.letmecook_lab5.repository.FirebaseStorageRepository
import android.net.Uri
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.NotificationRepository
import com.example.letmecook_lab5.domain.UserRepository
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.model.NotificationType
import kotlinx.coroutines.flow.first
import java.util.Locale
import java.util.Locale.getDefault
import java.util.UUID

class NewRecipeViewModel(
    private val repo: RecipeRepository,
    private val ingredientRepository: IngredientRepository,
    private val storageRepo: FirebaseStorageRepository,
    private val notificationRepository: NotificationRepository,
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(NewRecipeState())
    val uiState: StateFlow<NewRecipeState> = _uiState.asStateFlow()

    private var originalRecipe: Recipe? = null


    init{
        viewModelScope.launch {
            try {
                val ingredients = ingredientRepository.getAllIngredients()
                _uiState.value = _uiState.value.copy(
                    availableIngredients = ingredients
                )
            } catch (e: Exception) {
                Log.d("NewRecipeViewModel", "Error fetching ingredients: ${e.message}")
            }
        }
    }

    // functions:

    fun titleChanged (value: String) {
        _uiState.update { it.copy (title = value) }
    }

    fun imageChanged (value: String) {
        _uiState.update { it.copy (imageUrl = value) }
    }

    fun difficultyChanged (value: Difficulty) {
        _uiState.update { it.copy (difficulty = value) }
    }

    fun cookingTimeChanged(value: Int) {
        _uiState.update { it.copy(cookingTime = value) }
    }

    fun increaseServings() {
        _uiState.update { it.copy(servings = it.servings + 1) }
    }

    fun decreaseServings() {
        _uiState.update {
            if (it.servings > 1) it.copy(servings = it.servings - 1)
            else it
        }
    }

    fun addIngredient(value: Ingredient) {
        _uiState.update {
            val updated = it.ingredients + value
            it.copy(
                ingredients = updated,
                estimatedCost = calculateCost(updated),
                calories = calculateCalories(updated)
            )
        }
    }

    fun removeIngredient(index: Int) {
        _uiState.update {
            val updated = it.ingredients.toMutableList().apply {
                removeAt(index)
            }
            it.copy(
                ingredients = updated,
                estimatedCost = calculateCost(updated)
            )
        }
    }

    fun addStep() {
        _uiState.update { state ->
            val index = state.currentStepIndex

            val newStep = Step(
                title = "",
                description = "",
                photo = null
            )

            val updated = state.steps.toMutableList()

            if (index == -1) {
                updated.add(newStep)
                return@update state.copy(
                    steps = updated,
                    currentStepIndex = 0
                )
            }

            updated.add(index + 1, newStep)

            state.copy(
                steps = updated,
                currentStepIndex = index + 1
            )
        }
    }

    fun goBackStep() {
        _uiState.update { state ->
            if (state.currentStepIndex > 0) {
                state.copy(currentStepIndex = state.currentStepIndex - 1)
            } else {
                state.copy(currentStepIndex = -1)
            }
        }
    }

    fun nextStep() {
        _uiState.update { state ->
            if (state.currentStepIndex < state.steps.lastIndex) {
                state.copy(currentStepIndex = state.currentStepIndex + 1)
            } else state
        }
    }

    fun updateStep(index: Int, updated: Step) {
        _uiState.update {
            val list = it.steps.toMutableList()
            list[index] = updated

            it.copy(steps = list)
        }
    }

    fun deleteCurrentStep() {
        _uiState.update { state ->
            val index = state.currentStepIndex
            val updated = state.steps.toMutableList()

            if (index !in updated.indices) return@update state

            updated.removeAt(index)

            when {
                updated.isEmpty() -> state.copy(
                    steps = emptyList(),
                    currentStepIndex = -1
                )

                index > 0 -> state.copy(
                    steps = updated,
                    currentStepIndex = index - 1
                )

                else -> state.copy(
                    steps = updated,
                    currentStepIndex = 0
                )
            }
        }
    }

    fun stepTitleChanged(value: String) {
        _uiState.update { state ->
            val updatedSteps = state.steps.toMutableList()
            val index = state.currentStepIndex

            if (index in updatedSteps.indices) {
                val oldStep = updatedSteps[index]
                updatedSteps[index] = oldStep.copy(title = value)
            }

            state.copy(steps = updatedSteps)
        }
    }

    fun stepDescriptionChanged(value: String) {
        _uiState.update { state ->
            val updatedSteps = state.steps.toMutableList()
            val index = state.currentStepIndex

            if (index in updatedSteps.indices) {
                val oldStep = updatedSteps[index]
                updatedSteps[index] = oldStep.copy(description = value)
            }

            state.copy(steps = updatedSteps)
        }
    }

    fun stepImageChanged(value: String) {
        _uiState.update { state ->
            val updatedSteps = state.steps.toMutableList()
            val index = state.currentStepIndex

            if (index in updatedSteps.indices) {
                val oldStep = updatedSteps[index]
                updatedSteps[index] = oldStep.copy(photo = value)
            }

            state.copy(steps = updatedSteps)
        }
    }

    fun addTag(value: String) {
        _uiState.update {
            if (!it.tags.contains(value)) {
                it.copy(tags = it.tags + value)
            } else it
        }
    }

    fun removeTag(value: String) {
        _uiState.update {
            it.copy(tags = it.tags - value)
        }
    }

    fun keepEditing() {
        _uiState.update { it.copy(currentStepIndex = -1) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    private suspend fun uploadIfLocal(value: String?, path: String): String? {
        if (value.isNullOrBlank()) return null
        if (value.startsWith("http")) return value
        return storageRepo.uploadImage(Uri.parse(value), path)
    }

    fun saveRecipe() {
        val state = _uiState.value

        // validation chekcs:
        when {
            state.title.isBlank() -> {
                _uiState.update { it.copy(errorMessage = "You can't publish a recipe without a title!") }
                return
            }

            state.cookingTime <= 0 -> {
                _uiState.update { it.copy(errorMessage = "You can't publish a recipe that takes no time to cook!") }
                return
            }

            state.ingredients.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "You can't publish a recipe without ingredients!") }
                return
            }

            state.steps.isEmpty() -> {
                _uiState.update { it.copy(errorMessage = "You can't publish a recipe without steps!") }
                return
            }

            else -> {
                val invalidStepIndex = state.steps.indexOfFirst {
                    it.title.isBlank() || it.description.isBlank()
                }

                if (invalidStepIndex != -1) {
                    _uiState.update {
                        it.copy(errorMessage = "Step ${invalidStepIndex + 1} is incomplete!")
                    }
                    return
                }
            }
        }

        viewModelScope.launch {
            try {
                val heroUrl = uploadIfLocal(state.imageUrl, "recipe_images/${UUID.randomUUID()}.jpg")
                val uploadedSteps = state.steps.map { step ->
                    step.copy(photo = uploadIfLocal(step.photo, "recipe_steps/${UUID.randomUUID()}.jpg"))
                }
                val recipe = state.copy(imageUrl = heroUrl ?: "", steps = uploadedSteps)
                    .toRecipe()
                    .copy(ownerId = SessionManagerFacade.currentUser.value?.uid.orEmpty())
                repo.addRecipe(recipe)

                if (recipe.inspiredByRecipeId != "") {
                    val inspiredByRecipe = repo.getRecipeById(recipe.inspiredByRecipeId)
                    val user = userRepository.getUserById(SessionManagerFacade.currentUser.value?.uid ?: "").first()
                    notificationRepository.sendNotification(
                        Notification(
                            userId = inspiredByRecipe.ownerId,
                            title = "You inspired a new recipe",
                            message = "${user?.fullName} created a new recipe inspired by your ${inspiredByRecipe.title}",
                            type = NotificationType.RECIPE_DUPLICATED,
                            relatedId = recipe.id
                        )
                    )
                }

                if (recipe.tags.isNotEmpty()) {
                    val users = userRepository.getAllUsers().first()
                    val matchingUsers = users.filter { user ->
                        val prefs = user?.dietaryPreferences?.map { dp -> dp.lowercase(getDefault()) }
                        prefs?.any { it in recipe.tags } ?: false
                    }
                    matchingUsers.forEach { user ->
                        notificationRepository.sendNotification(
                            Notification(
                                userId = user?.id ?: "",
                                title = "New recipe for you",
                                message = "A new recipe matching your preferences has just been published: ${recipe.title}",
                                type = NotificationType.RECOMMENDATION,
                                relatedId = recipe.id
                            )
                        )
                    }
                }

                _uiState.update { it.copy(saveSuccess = true, errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Upload failed: ${e.message}") }
            }
        }
    }

    fun resetSaveState() {
        _uiState.update { it.copy(saveSuccess = false) }
    }

    fun reset() {
        _uiState.value = NewRecipeState()
    }

    fun initFromRecipeId(recipeId: String) {
        if (_uiState.value.sourceRecipeId == recipeId) return
        viewModelScope.launch {
            val recipe = repo.getRecipeById(recipeId) ?: return@launch
            _uiState.update {
                NewRecipeState(
                    title = recipe.title,
                    imageUrl = recipe.imageUrl,
                    difficulty = Difficulty.valueOf(recipe.difficulty),
                    cookingTime = recipe.cookingTime,
                    servings = recipe.servings,
                    ingredients = recipe.ingredients,
                    estimatedCost = recipe.cost,
                    steps = recipe.steps,
                    tags = recipe.tags,
                    sourceRecipeId = recipeId,
                    availableIngredients = it.availableIngredients
                )
            }
        }
    }

    fun initForEdit(recipeId: String) {
        if (_uiState.value.editingRecipeId == recipeId) return
        viewModelScope.launch {
            val recipe = repo.getRecipeById(recipeId) ?: return@launch
            originalRecipe = recipe
            _uiState.update {
                NewRecipeState(
                    title = recipe.title,
                    imageUrl = recipe.imageUrl,
                    difficulty = Difficulty.valueOf(recipe.difficulty),
                    cookingTime = recipe.cookingTime,
                    servings = recipe.servings,
                    ingredients = recipe.ingredients,
                    estimatedCost = recipe.cost,
                    steps = recipe.steps,
                    tags = recipe.tags,
                    editingRecipeId = recipeId,
                    availableIngredients = it.availableIngredients
                )
            }
        }
    }

    fun updateRecipe() {
        val orig = originalRecipe ?: return
        viewModelScope.launch {
            try {
                val s = _uiState.value
                val heroUrl = uploadIfLocal(s.imageUrl, "recipe_images/${UUID.randomUUID()}.jpg")
                val uploadedSteps = s.steps.map { step ->
                    step.copy(photo = uploadIfLocal(step.photo, "recipe_steps/${UUID.randomUUID()}.jpg"))
                }
                val updated = s.copy(imageUrl = heroUrl ?: "", steps = uploadedSteps)
                    .toRecipe()
                    .copy(
                        id = orig.id,
                        ownerId = orig.ownerId,
                        averageRating = orig.averageRating,
                        reviews = orig.reviews
                    )
                repo.updateRecipe(updated)
                _uiState.update { it.copy(updatedRecipe = updated) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Upload failed: ${e.message}") }
            }
        }
    }

    fun resetUpdateState() {
        _uiState.update { it.copy(updatedRecipe = null) }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val recipeRepository = application.container.recipeRepository
                val ingredientRepository = application.container.ingredientRepository
                val storageRepository = application.container.storageRepository
                val notificationRepository = application.container.notificationRepository
                val userRepository = application.container.userRepository
                NewRecipeViewModel(recipeRepository, ingredientRepository, storageRepository, notificationRepository, userRepository)
            }
        }
    }
}


data class NewRecipeState(
    val title: String = "",
    val imageUrl: String = "",
    val difficulty: Difficulty = Difficulty.Medium,
    val cookingTime: Int = 0,
    val servings: Int = 1,
    val estimatedCost: Double = 0.0,
    val calories: Int = 0,
    val ingredients: List<Ingredient> = emptyList(),
    val steps: List<Step> = emptyList(),
    val tags: List<String> = emptyList(),
    val availableIngredients: List<Ingredient> = emptyList(),
    val saveSuccess: Boolean = false,
    val currentStepIndex: Int = -1,
    val errorMessage: String? = null,
    val editingRecipeId: String? = null,
    val updatedRecipe: Recipe? = null,
    val sourceRecipeId: String? = null
)