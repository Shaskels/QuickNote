package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.AddToDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteNoteUseCase
import com.example.quicknote.domain.usecase.GetDeletedNotesUseCase
import com.example.quicknote.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addToDeletedNotesUseCase: AddToDeletedNotesUseCase,
    private val deleteDeletedNotesUseCase: DeleteDeletedNotesUseCase,
    getDeletedNotesUseCase: GetDeletedNotesUseCase,
    getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    val deletedNotesFlow = getDeletedNotesUseCase()

    val notesFlow = getNotesUseCase().combine(deletedNotesFlow) { notes, deletedNotes ->
        notes.filter { note -> deletedNotes.firstOrNull { note.id == it.id } == null }
    }

    fun addNoteToTrash(note: Note) {
        viewModelScope.launch {
            addToDeletedNotesUseCase(note)
        }
    }

    fun removeNoteFromTrash(id: String) {
        viewModelScope.launch {
            deleteDeletedNotesUseCase(id)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}