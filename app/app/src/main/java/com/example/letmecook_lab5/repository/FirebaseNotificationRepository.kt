package com.example.letmecook_lab5.repository

import android.util.Log
import com.example.letmecook_lab5.auth.SessionManagerFacade
import com.example.letmecook_lab5.domain.Collections
import com.example.letmecook_lab5.domain.NotificationRepository
import com.example.letmecook_lab5.model.Notification
import com.example.letmecook_lab5.model.NotificationType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class FirebaseNotificationRepository(
    private val firestore: FirebaseFirestore
) : NotificationRepository {

    private val collection = firestore.collection(Collections.NOTIFICATIONS)

    override fun getNotifications(userId: String): Flow<List<Notification>> =
        callbackFlow {
            val listener = collection
                .whereEqualTo("userId", userId)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        Log.e("NOTIF", "Firestore error:", error)
                        return@addSnapshotListener
                    }

                    if (snapshot == null) return@addSnapshotListener


                    val notifications = snapshot.documents.map { doc ->
                        Notification(
                            id = doc.id,
                            userId = doc.getString("userId") ?: "",
                            title = doc.getString("title") ?: "",
                            message = doc.getString("message") ?: "",
                            type = NotificationType.valueOf(
                                doc.getString("type") ?: NotificationType.TEST.name
                            ),
                            isRead = doc.getBoolean("isRead") ?: false,
                            timestamp = doc.getLong("timestamp") ?: 0L,
                            relatedId = doc.getString("relatedId") ?: ""
                        )
                    }

                    trySend(notifications)
                }
            awaitClose { listener.remove() }
        }

    override suspend fun sendNotification(notification: Notification) {
        Log.d("MYUID", SessionManagerFacade.currentUser.value?.uid ?: "") //baIhsE6qFzMuvrwiqyA83NSnzwE2
        val data = mapOf(
            "id" to notification.id,
            "userId" to notification.userId,
            "title" to notification.title,
            "message" to notification.message,
            "isRead" to notification.isRead,
            "timestamp" to notification.timestamp,
            "type" to notification.type.name,
            "relatedId" to notification.relatedId
        )
        collection.document(notification.id).set(data)
    }

    override suspend fun markAsRead(notificationId: String, userId: String) {
        collection.document(notificationId)
            .update("isRead", true)
    }

    override suspend fun deleteNotification(notificationId: String, userId: String) {
        collection.document(notificationId).delete()
    }
}