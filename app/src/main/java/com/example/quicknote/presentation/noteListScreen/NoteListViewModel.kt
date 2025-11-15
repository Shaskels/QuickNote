package com.example.quicknote.presentation.noteListScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import com.example.quicknote.domain.usecase.AddToDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteDeletedNotesUseCase
import com.example.quicknote.domain.usecase.DeleteNoteUseCase
import com.example.quicknote.domain.usecase.GetDeletedNotesUseCase
import com.example.quicknote.domain.usecase.GetNotesUseCase
import com.example.quicknote.presentation.noteListScreen.screenState.ContentState
import com.example.quicknote.presentation.noteListScreen.screenState.NoteListScreenState
import com.example.quicknote.presentation.noteListScreen.screenState.SortState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import timber.log.Timber
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
    val screenState: StateFlow<NoteListScreenState> = _screenState.asStateFlow()

    private val sortState = MutableStateFlow(Sorts(sortByHeadline = false, sortByDate = false))
    private val queryState = MutableStateFlow("")
    val deletedNotesFlow = getDeletedNotesUseCase()
    val filtersFlow = combine(
        sortState,
        queryState.debounce(600),
        deletedNotesFlow
    ) { sorts, query, deletedNotes ->
        getNotesByQuerySorted(query, sorts, deletedNotes)
    }

    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        _screenState.value = NoteListScreenState.Error
        Timber.tag("NoteListViewModel").d(throwable.message.toString())
    }

    init {
        _screenState.value = NoteListScreenState.Content(
            emptyList(), ContentState.NoteList,
            SortState.NotShown
        )
        getNotesByQuerySorted("", Sorts(sortByHeadline = false, sortByDate = false), emptyList())
        filtersFlow.setUpFiltersFlow()
    }

    fun getNotesByQuerySorted(query: String, sorts: Sorts, deletedNotes: List<Note>) {
        viewModelScope.launch(exceptionHandler) {
            getNotesUseCase(query, sorts).map { notes ->
                notes.filter { note -> deletedNotes.firstOrNull { note.id == it.id } == null }
            }
                .onEach {
                    _screenState.update { currentState ->
                        (currentState as NoteListScreenState.Content).copy(
                            notes = it
                        )
                    }
                }
                .flowOn(Dispatchers.Default)
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
        _screenState.updateState<NoteListScreenState.Content> { currentState ->
            currentState.copy(
                sortState = SortState.Sorted(
                    sortState.value.sortByHeadline,
                    sortState.value.sortByDate
                )
            )
        }
    }

    fun hideSortOptions() {
        _screenState.updateState<NoteListScreenState.Content> { currentState ->
            currentState.copy(
                sortState = SortState.NotShown
            )
        }
    }

    fun sortByHeadline() {
        viewModelScope.launch {
            sortState.emit(Sorts(sortByHeadline = true, sortByDate = false))

            _screenState.updateState<NoteListScreenState.Content> { currentState ->
                currentState.copy(
                    sortState = SortState.Sorted(
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

            _screenState.updateState<NoteListScreenState.Content> { currentState ->
                currentState.copy(
                    sortState = SortState.Sorted(
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

            _screenState.updateState<NoteListScreenState.Content> { currentState ->
                currentState.copy(
                    sortState = SortState.Sorted(
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
                _screenState.updateState<NoteListScreenState.Content> { currentState ->
                    currentState.copy(
                        contentState = ContentState.SearchNoteList
                    )
                }
            }
        }
    }

    fun onClearQuery() {
        viewModelScope.launch {
            queryState.emit("")

            _screenState.updateState<NoteListScreenState.Content> { currentState ->
                currentState.copy(
                    contentState = ContentState.NoteList
                )
            }
        }
    }

    private inline fun <reified T : NoteListScreenState> MutableStateFlow<NoteListScreenState>.updateState(
        block: (T) -> T
    ) {
        if (this.value is T) {
            this.update {
                block(this.value as T)
            }
        }
    }
}

