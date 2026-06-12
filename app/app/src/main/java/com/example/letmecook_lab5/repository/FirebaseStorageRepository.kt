package com.example.letmecook_lab5.repository

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import androidx.core.net.toUri

class FirebaseStorageRepository {
    private val storage = FirebaseStorage.getInstance()

    suspend fun uploadImage(uri: Uri, path: String): String {
        val ref = storage.reference.child(path)
        ref.putFile(uri).await()
        return ref.downloadUrl.await().toString()
    }

    suspend fun uploadIfLocal(value: String?, path: String): String? {
        if (value.isNullOrBlank()) return null
        if (value.startsWith("http")) return value   // already uploaded
        return uploadImage(value.toUri(), path)
    }
}