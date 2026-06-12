package com.example.letmecook_lab5.navigation

import android.app.AlertDialog
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
//import com.example.letmecook_lab5.auth.SessionManagerFacade
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable

/*
@Serializable
object LoginToProceedDialogRoute

@Composable
fun LoginToProceedDialogRoute (
    onDismiss: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Login required") },
        text = { Text("You need to be logged in to save favorites.")},
        confirmButton = {
            Button(onClick = {
                scope.launch {
                    val result = SessionManagerFacade.signInAnonymouse()
                    if(result.isSuccess) onLoginSuccess()
                }
            }) {
                Text("Sign in with Google")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text ("Cancel")}
        }
    )
}
 */