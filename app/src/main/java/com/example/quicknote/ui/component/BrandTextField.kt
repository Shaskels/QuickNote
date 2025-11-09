package com.example.quicknote.ui.component

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun BrandTextField(
    value: String,
    onValueChanged: (String) -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    textStyle: TextStyle = LocalTextStyle.current,
) {
    TextField(
        value = value,
        onValueChanged,
        placeholder = { Text(text = hint, style = textStyle) },
        keyboardOptions = keyboardOptions,
        textStyle = textStyle,
        colors = TextFieldDefaults.colors(
            cursorColor = NoteTheme.colors.backgroundBrand,
            focusedTextColor = NoteTheme.colors.textPrimary,
            unfocusedTextColor = NoteTheme.colors.textPrimary,
            focusedContainerColor = NoteTheme.colors.backgroundColor,
            unfocusedContainerColor = NoteTheme.colors.backgroundColor,
            focusedPlaceholderColor = NoteTheme.colors.textLight,
            unfocusedPlaceholderColor = NoteTheme.colors.textLight,
            focusedIndicatorColor = NoteTheme.colors.backgroundColor,
            unfocusedIndicatorColor = NoteTheme.colors.backgroundColor,
            selectionColors = TextSelectionColors(
                handleColor = NoteTheme.colors.backgroundBrand,
                backgroundColor = NoteTheme.colors.selectionColor
            )
        ),
        keyboardActions = keyboardActions,
        modifier = modifier
    )
}