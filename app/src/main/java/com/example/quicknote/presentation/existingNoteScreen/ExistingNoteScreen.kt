package com.example.quicknote.presentation.existingNoteScreen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.quicknote.presentation.component.NoteScreen

@Composable
fun ExistingNoteScreen(
    existingNoteViewModel: ExistingNoteViewModel,
    onBackClick: () -> Unit,
) {
    val noteState by existingNoteViewModel.noteState.collectAsState(existingNoteViewModel.getEmptyNote())

    when (val currentState = noteState) {
        is NoteState.Initial -> {}
        is NoteState.Content -> NoteScreen(
            note = currentState.note,
            onBackClick = onBackClick,
            onSaveClick = {
                existingNoteViewModel.updateNote()
                onBackClick()
            },
            onHeadlineChanged = { headline -> existingNoteViewModel.onHeadlineChanged(headline) },
            onValueChanged = { value -> existingNoteViewModel.onValueChanged(value) }
        )
    }
}