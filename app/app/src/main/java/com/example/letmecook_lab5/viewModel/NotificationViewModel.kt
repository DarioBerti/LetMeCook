package com.example.letmecook_lab5.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.letmecook_lab5.LetMeCookApplication
import com.example.letmecook_lab5.domain.ReviewRepository
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.model.NotificationType
import com.example.letmecook_lab5.domain.NotificationRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import com.example.letmecook_lab5.auth.SessionManagerFacade

class NotificationViewModel(
    private val repository: NotificationRepository,
    private var userId: String
) : ViewModel() {

    val notifications: StateFlow<List<Notification>> =
        repository.getNotifications(userId = userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    val unreadCount: StateFlow<Int> =
        notifications.map { list ->
            list.count { !it.isRead }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = 0
        )

    fun sendNotification() {
        viewModelScope.launch {
            repository.sendNotification(
                Notification(
                    userId = "baIhsE6qFzMuvrwiqyA83NSnzwE2",
                    title = "Test Notification",
                    message = "Everything works!",
                    type = NotificationType.TEST
                )
            )
        }
    }

    fun markAsRead(notificationId: String) {
        viewModelScope.launch {
            repository.markAsRead(notificationId, userId)
        }
    }

    fun deleteNotification(notificationId: String) {
        viewModelScope.launch {
            repository.deleteNotification(notificationId, userId)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[APPLICATION_KEY] as LetMeCookApplication)
                val notificationRepository = application.container.notificationRepository
                val currentUser = SessionManagerFacade.currentUser.value?.uid ?: ""
                NotificationViewModel(notificationRepository, currentUser)
            }
        }
    }
}