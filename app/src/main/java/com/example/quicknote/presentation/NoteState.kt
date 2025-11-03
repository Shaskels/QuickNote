package com.example.quicknote.presentation

import com.example.quicknote.domain.Note

sealed interface NoteState {

    object Initial : NoteState
    data class Content(val note: Note) : NoteState
}