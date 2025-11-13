package com.example.quicknote.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.quicknote.presentation.theme.NoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithAction(title: String, onActionClick: () -> Unit, icon: @Composable (() -> Unit)) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        actions = {
            IconButton(onClick = onActionClick, content = icon)
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NoteTheme.colors.backgroundColor,
            navigationIconContentColor = NoteTheme.colors.textPrimary,
            actionIconContentColor = NoteTheme.colors.textPrimary,
            titleContentColor = NoteTheme.colors.textPrimary,
        ),
        modifier = Modifier.fillMaxWidth()
    )
}