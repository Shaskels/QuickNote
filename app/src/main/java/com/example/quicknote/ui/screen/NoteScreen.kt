package com.example.quicknote.ui.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.ui.component.BrandTextField
import com.example.quicknote.ui.theme.NoteTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(note: Note) {
    val headline = rememberTextFieldState()
    val note = rememberTextFieldState(note.value)
    Column(
        modifier = Modifier
            .scrollable(
                rememberScrollState(), orientation = Orientation.Vertical
            )
            .padding(10.dp)
    ) {
        TopAppBar(
            title = {},
            navigationIcon = {
                IconButton({}) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = stringResource(R.string.back_to_note_list)
                    )
                }
            },
            actions = {
                IconButton({}) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = stringResource(R.string.note_done)
                    )
                }
            },
            expandedHeight = 40.dp,
            windowInsets = WindowInsets(top = 0.dp),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = NoteTheme.colors.backgroundColor,
                navigationIconContentColor = NoteTheme.colors.textPrimary,
                actionIconContentColor = NoteTheme.colors.textPrimary
            ),
            modifier = Modifier.fillMaxWidth()
        )

        BrandTextField(
            state = headline,
            hint = stringResource(R.string.headline),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onKeyboardAction = { },
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = MaterialTheme.typography.titleMedium
        )

        BrandTextField(
            state = note,
            hint = stringResource(R.string.enter_your_note),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { },
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@Preview
@Composable
private fun NoteScreenPrew() {
    NoteScreen(Note(value = "lkflf"))
}