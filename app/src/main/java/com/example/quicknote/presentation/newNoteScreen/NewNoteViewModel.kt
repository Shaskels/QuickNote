package com.example.quicknote.presentation.newNoteScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.usecase.SaveNoteUseCase
import com.example.quicknote.presentation.existingNoteScreen.NoteState
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
            if (_noteState.value is NoteState.Content && ((_noteState.value as NoteState.Content).note.headline.isNotEmpty()
                        || (_noteState.value as NoteState.Content).note.value.isNotEmpty()
                        || (_noteState.value as NoteState.Content).note.images.isNotEmpty())
            ) {
                saveNoteUseCase((_noteState.value as NoteState.Content).note)
            }
        }
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

}