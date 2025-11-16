package com.example.quicknote.presentation.trashScreen

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.ClearDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteDeletedNotesUseCase
import com.example.quicknote.domain.usecase.GetDeletedNotesUseCase
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import com.example.quicknote.presentation.noteListScreen.screenState.SelectionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedNoteListViewModel @Inject constructor(
    getDeletedNotesUseCase: GetDeletedNotesUseCase,
    private val deleteDeletedNotesUseCase: DeleteDeletedNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    private val clearDeletedNotesUseCase: ClearDeletedNotesUseCase,
) : ViewModel() {

    private val _selectionState = MutableStateFlow<SelectionState>(SelectionState.NoSelection)
    val selectionState: StateFlow<SelectionState> = _selectionState.asStateFlow()

    private val selectedNotes: SnapshotStateList<Note> = mutableStateListOf()
    val deletedNotesFlow = getDeletedNotesUseCase()

    fun deleteNotesFromTrash() {
        viewModelScope.launch {
            for (note in selectedNotes) {
                deleteDeletedNotesUseCase(note.id)
            }
            clearSelection()
        }
    }

    fun restoreSelectedDeletedNotes() {
        viewModelScope.launch {
            for (note in selectedNotes) {
                deleteDeletedNotesUseCase(note.id)
                saveNoteUseCase(note)
            }
            clearSelection()
        }
    }

    fun clearTrash() {
        viewModelScope.launch {
            clearDeletedNotesUseCase()
        }
    }

    fun onSelectNote(note: Note) {
        selectedNotes.add(note)
        _selectionState.value = SelectionState.Selection(selectedNotes)
    }

    fun onUnselectNote(note: Note) {
        selectedNotes.remove(note)
        _selectionState.value = SelectionState.Selection(selectedNotes)
    }

    fun clearSelection() {
        selectedNotes.clear()
        _selectionState.value = SelectionState.NoSelection
    }

}