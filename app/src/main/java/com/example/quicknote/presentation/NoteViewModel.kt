package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.GetNoteByIdUseCase
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import com.example.quicknote.domain.usecase.UpdateNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
) : ViewModel() {

    fun addNote(note: Note) {
        viewModelScope.launch {
            saveNoteUseCase(note)
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            updateNoteUseCase(note)
        }
    }

    fun getNoteById(id: String): Flow<Note> {
        return getNoteByIdUseCase(id)
    }

}