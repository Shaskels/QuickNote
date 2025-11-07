package com.example.quicknote.presentation

import com.example.quicknote.domain.Note

sealed interface NoteListScreenState {
    object Initial : NoteListScreenState

    object Error : NoteListScreenState
    data class Content(
        val notes: List<Note>,
        val contentState: ContentState,
        val sortState: SortState
    ) : NoteListScreenState

    sealed interface ContentState {
        object NoteList : ContentState
        object SearchNoteList : ContentState
    }

    sealed interface SortState {
        object NotShown : SortState
        data class Sorted(
            val sortByHeadline: Boolean,
            val sortByDate: Boolean
        ) : SortState
    }
}