package com.example.quicknote.presentation.noteListScreen.screenState

sealed interface SortState {
    object NotShown : SortState
    data class Sorted(
        val sortByHeadline: Boolean,
        val sortByDate: Boolean
    ) : SortState
}