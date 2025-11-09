package com.example.quicknote.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import com.example.quicknote.domain.usecase.AddToDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteNoteUseCase
import com.example.quicknote.domain.usecase.GetDeletedNotesUseCase
import com.example.quicknote.domain.usecase.GetNotesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListViewModel @Inject constructor(
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val addToDeletedNotesUseCase: AddToDeletedNotesUseCase,
    private val deleteDeletedNotesUseCase: DeleteDeletedNotesUseCase,
    getDeletedNotesUseCase: GetDeletedNotesUseCase,
    private val getNotesUseCase: GetNotesUseCase
) : ViewModel() {

    private val _screenState = MutableStateFlow<NoteListScreenState>(NoteListScreenState.Initial)
    val screenState: StateFlow<NoteListScreenState> = _screenState

    private val sortState = MutableStateFlow(Sorts(sortByHeadline = false, sortByDate = false))
    private val queryState = MutableStateFlow("")
    val deletedNotesFlow = getDeletedNotesUseCase()
    val filtersFlow = combine(
        sortState,
        queryState.debounce(300),
        deletedNotesFlow
    ) { sorts, query, deletedNotes ->
        getNotesByQuerySorted(query, sorts, deletedNotes)
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _screenState.value = NoteListScreenState.Error
        Log.d("NoteListViewModel", throwable.message.toString())
    }

    init {
        _screenState.value = NoteListScreenState.Content(
            emptyList(), NoteListScreenState.ContentState.NoteList,
            NoteListScreenState.SortState.NotShown
        )
        getNotesByQuerySorted("", Sorts(sortByHeadline = false, sortByDate = false), emptyList())
        filtersFlow.setUpFiltersFlow()
    }

    fun getNotesByQuerySorted(query: String, sorts: Sorts, deletedNotes: List<Note>) {
        viewModelScope.launch(exceptionHandler) {
            getNotesUseCase(query, sorts).map { notes ->
                notes.filter { note -> deletedNotes.firstOrNull { note.id == it.id } == null }
            }
                .flowOn(Dispatchers.Default)
                .onEach {
                    _screenState.update { currentState ->
                        (currentState as NoteListScreenState.Content).copy(
                            notes = it
                        )
                    }
                }
                .collect()
        }
    }

    @OptIn(FlowPreview::class)
    private fun Flow<Unit>.setUpFiltersFlow() {
        viewModelScope.launch(exceptionHandler) {
            this@setUpFiltersFlow.collect()
        }
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

    fun showSortOptions() {
        _screenState.update { currentState ->
            (currentState as NoteListScreenState.Content).copy(
                sortState = NoteListScreenState.SortState.Sorted(
                    sortState.value.sortByHeadline,
                    sortState.value.sortByDate
                )
            )
        }
    }

    fun hideSortOptions() {
        _screenState.update { currentState ->
            (currentState as NoteListScreenState.Content).copy(
                sortState = NoteListScreenState.SortState.NotShown
            )
        }
    }

    fun sortByHeadline() {
        viewModelScope.launch {
            sortState.emit(Sorts(sortByHeadline = true, sortByDate = false))

            _screenState.update { currentState ->
                (currentState as NoteListScreenState.Content).copy(
                    sortState = NoteListScreenState.SortState.Sorted(
                        sortByHeadline = true,
                        sortByDate = false
                    )
                )
            }
        }
    }

    fun sortByDate() {
        viewModelScope.launch {
            sortState.emit(Sorts(sortByHeadline = false, sortByDate = true))

            _screenState.update { currentState ->
                (currentState as NoteListScreenState.Content).copy(
                    sortState = NoteListScreenState.SortState.Sorted(
                        sortByHeadline = false,
                        sortByDate = true
                    )
                )
            }
        }
    }

    fun noneSort() {
        viewModelScope.launch {
            sortState.emit(Sorts(sortByHeadline = false, sortByDate = false))

            _screenState.update { currentState ->
                (currentState as NoteListScreenState.Content).copy(
                    sortState = NoteListScreenState.SortState.Sorted(
                        sortByHeadline = false,
                        sortByDate = false
                    )
                )
            }
        }
    }

    fun onQueryChange(query: String) {
        viewModelScope.launch {
            queryState.emit(query)

            if (query.isNotEmpty()) {
                _screenState.update { currentState ->
                    (currentState as NoteListScreenState.Content).copy(
                        contentState = NoteListScreenState.ContentState.SearchNoteList
                    )
                }
            }
        }
    }

    fun onClearQuery() {
        viewModelScope.launch {
            queryState.emit("")

            _screenState.update { currentState ->
                (currentState as NoteListScreenState.Content).copy(
                    contentState = NoteListScreenState.ContentState.NoteList
                )
            }
        }
    }
}
