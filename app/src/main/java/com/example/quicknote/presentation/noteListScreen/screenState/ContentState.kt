package com.example.quicknote.presentation.noteListScreen.screenState

sealed interface ContentState {
    data object NoteList : ContentState
    data object SearchNoteList : ContentState
}