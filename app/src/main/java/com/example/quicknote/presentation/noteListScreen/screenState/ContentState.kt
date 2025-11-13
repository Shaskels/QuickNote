package com.example.quicknote.presentation.noteListScreen.screenState

sealed interface ContentState {
    object NoteList : ContentState
    object SearchNoteList : ContentState
}