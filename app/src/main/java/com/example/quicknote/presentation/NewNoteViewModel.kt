package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import com.example.quicknote.util.getCurrentTime
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class NewNoteViewModel @Inject constructor(
    private val saveNoteUseCase: SaveNoteUseCase,
) : ViewModel() {

    private val _noteState = MutableStateFlow<NoteState>(NoteState.Content(getEmptyNote()))
    val noteState: StateFlow<NoteState> = _noteState

    fun addNote() {
        viewModelScope.launch {
            if (_noteState.value is NoteState.Content) {
                saveNoteUseCase((_noteState.value as NoteState.Content).note.copy(timeOfChange = getCurrentTime()))
            }
        }
    }

    fun onHeadlineChanged(headline: String) {
        if (_noteState.value is NoteState.Content) {
            _noteState.update { currentState ->
                NoteState.Content(
                    (currentState as NoteState.Content).note.copy(headline = headline)
                )
            }
        }
    }

    fun onValueChanged(value: String) {
        if (_noteState.value is NoteState.Content) {
            _noteState.update { currentState ->
                NoteState.Content(
                    (currentState as NoteState.Content).note.copy(value = value)
                )
            }
        }
    }

    fun getEmptyNote(): Note {
        return Note(value = "", headline = "", timeOfChange = getCurrentTime())
    }

}