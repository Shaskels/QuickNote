package com.example.quicknote.presentation.mainScreen

import kotlin.reflect.KClass

enum class NavigationOptions(val route: KClass<*>) {
    NOTE_LIST(Route.NoteList::class),
    DELETED_NOTE_LIST(Route.DeletedNoteList::class),

}