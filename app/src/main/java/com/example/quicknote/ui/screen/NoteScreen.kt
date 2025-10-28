package com.example.quicknote.ui.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.domain.Note

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteScreen(note: Note) {
    val headline = rememberTextFieldState()
    val note = rememberTextFieldState()
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
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            state = headline,
            placeholder = { Text(stringResource(R.string.headline)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            onKeyboardAction = { },
            modifier = Modifier
                .fillMaxWidth()
        )

        TextField(
            state = note,
            placeholder = { Text(stringResource(R.string.enter_your_note)) },
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