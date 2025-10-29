package com.example.quicknote.ui.component

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun AddButton(onClick: () -> Unit, description: String, modifier: Modifier = Modifier) {
    FloatingActionButton(
        shape = CircleShape,
        onClick = onClick,
        containerColor = NoteTheme.colors.backgroundBrand,
        contentColor = NoteTheme.colors.noteBackground,
        elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
        modifier = modifier
    ) {
        Icon(imageVector = Icons.Filled.Add, contentDescription = description)
    }
}