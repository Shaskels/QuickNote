package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.DeleteDeletedNotesUseCase
import com.example.quicknote.domain.usecase.GetDeletedNotesUseCase
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DeletedNoteListViewModel @Inject constructor(
    getDeletedNotesUseCase: GetDeletedNotesUseCase,
    private val deleteDeletedNotesUseCase: DeleteDeletedNotesUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    val deletedNotesFlow = getDeletedNotesUseCase()

    fun deleteNoteFromTrash(id: String) {
        viewModelScope.launch {
            deleteDeletedNotesUseCase(id)
        }
    }

    fun restoreDeletedNote(note: Note) {
        viewModelScope.launch {
            deleteDeletedNotesUseCase(note.id)
            saveNoteUseCase(note)
        }
    }
}