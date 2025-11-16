package com.example.quicknote.presentation.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.quicknote.R
import com.example.quicknote.presentation.theme.NoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithCancel(title: String, onCancelClick: () -> Unit, onDeleteClick: () -> Unit) {
    TopAppBar(
        title = { Text(title, style = MaterialTheme.typography.titleLarge) },
        navigationIcon = {
            IconButton(onClick = onCancelClick) {
                Icon(
                    painter = painterResource(R.drawable.close_24dp),
                    contentDescription = stringResource(R.string.back_to_note_list)
                )
            }
        },
        actions = {
            IconButton(onClick = onDeleteClick) {
                Icon(
                    painter = painterResource(R.drawable.delete_24dp),
                    contentDescription = stringResource(R.string.delete_all)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NoteTheme.colors.backgroundColor,
            navigationIconContentColor = NoteTheme.colors.textPrimary,
            actionIconContentColor = NoteTheme.colors.textPrimary,
            titleContentColor = NoteTheme.colors.textPrimary
        ),
        modifier = Modifier.fillMaxWidth()
    )
}