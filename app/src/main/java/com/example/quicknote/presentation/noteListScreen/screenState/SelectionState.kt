package com.example.quicknote.presentation.noteListScreen.screenState

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.quicknote.domain.Note

sealed interface SelectionState {

    data object NoSelection : SelectionState
    data class Selection(val notes: SnapshotStateList<Note>) : SelectionState
}