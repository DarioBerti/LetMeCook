package com.example.letmecook_lab5.ui.screens.utils

fun formatTimeAgo(timestamp: Long): String {
    val diffMillis = System.currentTimeMillis() - timestamp
    val seconds = diffMillis / 1000
    val minutes = seconds / 60
    val hours = minutes / 60
    val days = hours / 24

    return when {
        seconds < 60            -> "Just now"
        minutes == 1L           -> "1 minute ago"
        minutes < 60            -> "$minutes minutes ago"
        hours == 1L             -> "1 hour ago"
        hours < 24              -> "$hours hours ago"
        days == 1L              -> "1 day ago"
        else                    -> "$days days ago"
    }
}