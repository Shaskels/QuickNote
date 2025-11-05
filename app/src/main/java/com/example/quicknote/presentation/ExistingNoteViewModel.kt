package com.example.quicknote.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.GetNoteByIdUseCase
import com.example.quicknote.domain.usecase.UpdateNoteUseCase
import com.example.quicknote.util.getCurrentTime
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ExistingNoteViewModel.NoteViewModelFactory::class)
class ExistingNoteViewModel @AssistedInject constructor(
    private val updateNoteUseCase: UpdateNoteUseCase,
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    @Assisted private val noteId: String
) : ViewModel() {

    private val _noteState = MutableStateFlow<NoteState>(NoteState.Initial)
    val noteState: StateFlow<NoteState> = _noteState

    init {
        getNoteById(noteId)
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

    fun updateNote() {
        viewModelScope.launch {
            if (_noteState.value is NoteState.Content) {
                updateNoteUseCase((_noteState.value as NoteState.Content).note.copy(timeOfChange = getCurrentTime()))
            }
        }
    }

    fun getNoteById(noteId: String) {
        viewModelScope.launch {
            getNoteByIdUseCase(noteId)
                .onEach { _noteState.value = NoteState.Content(it) }
                .collect()
        }
    }

    fun getEmptyNote(): Note {
        return Note(value = "", headline = "", timeOfChange = getCurrentTime())
    }

    @AssistedFactory
    interface NoteViewModelFactory {
        fun create(characterId: String): ExistingNoteViewModel
    }
}


