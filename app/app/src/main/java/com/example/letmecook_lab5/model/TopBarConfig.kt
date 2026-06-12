package com.example.letmecook_lab5.model

data class TopBarConfig(
    val title: String,
    val showBack: Boolean,
    val showNotifications: Boolean = false,
    val showProfile: Boolean = false,
    val showOptions: Boolean = false,
    val unreadNotifications: Int = 0
)