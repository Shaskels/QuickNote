package com.example.quicknote.ui.screen

import kotlin.reflect.KClass

enum class NavigationOptions(val route: KClass<*>) {
    NOTE_LIST(Route.NoteList::class),
    DELETED_NOTE_LIST(Route.DeletedNoteList::class),

}