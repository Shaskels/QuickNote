package com.example.quicknote.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.presentation.NewNoteViewModel
import com.example.quicknote.presentation.NoteState

@Composable
fun NewNoteScreen(
    newNoteViewModel: NewNoteViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {

    val noteState by newNoteViewModel.noteState.collectAsState(newNoteViewModel.getEmptyNote())

    when (val currentState = noteState) {
        is NoteState.Initial -> {}
        is NoteState.Content -> NoteScreen(
            note = currentState.note,
            onBackClick = onBackClick,
            onSaveClick = {
                newNoteViewModel.addNote()
                onBackClick()
            },
            onHeadlineChanged = { headline -> newNoteViewModel.onHeadlineChanged(headline) },
            onValueChanged = { value -> newNoteViewModel.onValueChanged(value) }
        )
    }

}