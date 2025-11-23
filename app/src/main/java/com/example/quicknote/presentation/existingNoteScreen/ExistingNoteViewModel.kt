package com.example.quicknote.presentation.existingNoteScreen

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
        _noteState.updateState<NoteState.Content> { currentState ->
            currentState.copy(note = currentState.note.copy(headline = headline))
        }
    }

    fun onValueChanged(value: String) {
        _noteState.updateState<NoteState.Content> { currentState ->
            currentState.copy(note = currentState.note.copy(value = value))
        }
    }

    fun updateNote() {
        viewModelScope.launch {
            if (_noteState.value is NoteState.Content && ((_noteState.value as NoteState.Content).note.headline.isNotEmpty()
                        || (_noteState.value as NoteState.Content).note.value.isNotEmpty()
                        || (_noteState.value as NoteState.Content).note.images.isNotEmpty())
            ) {
                updateNoteUseCase((_noteState.value as NoteState.Content).note)
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

    fun onAddPhotos(images: List<String>) {
        val newImages = (_noteState.value as NoteState.Content).note.images + images
        _noteState.updateState<NoteState.Content> { currentState ->
            currentState.copy(note = currentState.note.copy(images = newImages))
        }
    }

    fun onDeletePhoto(image: String) {
        val newImages = (_noteState.value as NoteState.Content).note.images - image
        _noteState.updateState<NoteState.Content> { currentState ->
            currentState.copy(note = currentState.note.copy(images = newImages))
        }
    }

    fun getEmptyNote(): Note {
        return Note(
            id = "",
            value = "",
            headline = "",
            timeOfChange = getCurrentTime(),
            images = emptyList()
        )
    }

    private inline fun <reified T : NoteState> MutableStateFlow<NoteState>.updateState(
        block: (T) -> T
    ) {
        if (this.value is T) {
            this.update {
                block(this.value as T)
            }
        }
    }

    @AssistedFactory
    interface NoteViewModelFactory {
        fun create(characterId: String): ExistingNoteViewModel
    }
}