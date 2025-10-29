package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.DeleteNoteUseCase
import com.example.quicknote.domain.usecase.GetNotesUseCase
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val saveNoteUseCase: SaveNoteUseCase,
    getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    val notesFlow = getNotesUseCase()

    fun addNote(note: Note) {
        viewModelScope.launch {
            saveNoteUseCase(note)
        }
    }

    fun deleteNote(id: String) {
        viewModelScope.launch {
            deleteNoteUseCase(id)
        }
    }
}