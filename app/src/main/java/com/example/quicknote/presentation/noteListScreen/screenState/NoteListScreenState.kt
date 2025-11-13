package com.example.quicknote.presentation.noteListScreen.screenState

import com.example.quicknote.domain.Note

sealed interface NoteListScreenState {
    object Initial : NoteListScreenState
    object Error : NoteListScreenState
    data class Content(
        val notes: List<Note>,
        val contentState: ContentState,
        val sortState: SortState
    ) : NoteListScreenState
}