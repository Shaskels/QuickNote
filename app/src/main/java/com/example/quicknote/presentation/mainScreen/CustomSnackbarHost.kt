package com.example.quicknote.presentation.mainScreen

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

data class CustomSnackbarHost(val scope: CoroutineScope, val snackbarHostState: SnackbarHostState)

fun CustomSnackbarHost.showSnackbar(
    message: String,
    actionLabel: String,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> {
                onDismiss()
            }
        }
    }
}
