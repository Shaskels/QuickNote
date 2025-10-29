package com.example.quicknote.ui.screen

import kotlinx.serialization.Serializable

sealed interface Route {

    @Serializable
    object NoteList : Route

    @Serializable
    object NewNote : Route

    @Serializable
    data class ExistingNote(val id: String) : Route
}