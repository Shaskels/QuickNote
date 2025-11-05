package com.example.quicknote.ui.component

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Clear
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun SearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClearQueryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        shape = RoundedCornerShape(25.dp),
        placeholder = {
            Text("Notes search")
        },
        leadingIcon = {
            Icon(Icons.Rounded.Search, contentDescription = "")
        },
        trailingIcon = {
            IconButton(onClick = onClearQueryClick) {
                Icon(Icons.Rounded.Clear, contentDescription = "")
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            cursorColor = NoteTheme.colors.backgroundBrand,
            focusedTextColor = NoteTheme.colors.textPrimary,
            unfocusedTextColor = NoteTheme.colors.textPrimary,
            focusedContainerColor = NoteTheme.colors.backgroundSecondary,
            unfocusedContainerColor = NoteTheme.colors.backgroundSecondary,
            focusedPlaceholderColor = NoteTheme.colors.textLight,
            unfocusedPlaceholderColor = NoteTheme.colors.textLight,
            focusedIndicatorColor = NoteTheme.colors.backgroundSecondary,
            unfocusedIndicatorColor = NoteTheme.colors.backgroundSecondary,
            focusedLeadingIconColor = NoteTheme.colors.textLight,
            unfocusedLeadingIconColor = NoteTheme.colors.textLight,
            focusedTrailingIconColor = NoteTheme.colors.textLight,
            unfocusedTrailingIconColor = NoteTheme.colors.textLight,
            selectionColors = TextSelectionColors(
                handleColor = NoteTheme.colors.backgroundBrand,
                backgroundColor = NoteTheme.colors.selectionColor
            )
        ),
        modifier = modifier,
    )
}