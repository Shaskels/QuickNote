package com.example.quicknote.presentation.mainScreen

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object NoteList : Route

    @Serializable
    object DeletedNoteList : Route

    @Serializable
    object NewNote : Route

    @Serializable
    data class ExistingNote(val id: String, val touchX: Float, val touchY: Float) : Route
}