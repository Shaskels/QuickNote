package com.example.quicknote.presentation.noteListScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.presentation.component.AddButton
import com.example.quicknote.presentation.component.CustomChip
import com.example.quicknote.presentation.component.NoteItemInList
import com.example.quicknote.presentation.component.SearchField
import com.example.quicknote.presentation.component.TopBarWithAction
import com.example.quicknote.presentation.component.TopBarWithCancel
import com.example.quicknote.presentation.mainScreen.CustomSnackbarHost
import com.example.quicknote.presentation.mainScreen.LocalSnackbarHostState
import com.example.quicknote.presentation.mainScreen.showSnackbar
import com.example.quicknote.presentation.noteListScreen.screenState.ContentState
import com.example.quicknote.presentation.noteListScreen.screenState.NoteListScreenState
import com.example.quicknote.presentation.noteListScreen.screenState.SelectionState
import com.example.quicknote.presentation.noteListScreen.screenState.SortState
import com.example.quicknote.presentation.theme.NoteTheme

@Composable
fun NoteListScreen(
    noteListViewModel: NoteListViewModel = hiltViewModel(),
    onNoteClick: (String, Float, Float) -> Unit,
    onAddNoteClick: () -> Unit,
) {
    val screenState = noteListViewModel.screenState.collectAsState()
    val snackbarHost = LocalSnackbarHostState.current

    DisposableEffect(Unit) {
        onDispose {
            noteListViewModel.clearSelection()
            snackbarHost.snackbarHostState.currentSnackbarData?.dismiss()
        }
    }

    when (val currentState = screenState.value) {
        is NoteListScreenState.Initial -> {}
        is NoteListScreenState.Error -> {}
        is NoteListScreenState.Content -> Screen(
            screenState = currentState,
            noteListViewModel = noteListViewModel,
            snackbarHost = snackbarHost,
            onNoteClick = onNoteClick,
            onAddNoteClick = onAddNoteClick,
        )
    }

}

@Composable
fun Screen(
    screenState: NoteListScreenState.Content,
    noteListViewModel: NoteListViewModel,
    snackbarHost: CustomSnackbarHost,
    onNoteClick: (String, Float, Float) -> Unit,
    onAddNoteClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var touchedPoint by remember { mutableStateOf(Offset.Zero) }
    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        topBar = {
            TopBar(
                screenState.selectionState,
                screenState.sortState,
                snackbarHost,
                noteListViewModel
            )
        },
        floatingActionButton = {
            if (screenState.contentState is ContentState.NoteList) {
                AddButton(
                    onClick = onAddNoteClick,
                    description = stringResource(R.string.add_note),
                    modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        modifier = modifier,
        contentWindowInsets = WindowInsets(bottom = 0)
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    awaitPointerEventScope {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        event.changes.forEach {
                            if (it.pressed) {
                                touchedPoint = it.position
                            }
                        }
                    }
                }
                .padding(innerPadding)
        ) {

            SearchField(
                query = query,
                onQueryChange = {
                    query = it
                    noteListViewModel.onQueryChange(it)
                },
                onClearQueryClick = {
                    query = ""
                    noteListViewModel.onClearQuery()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            )

            if (screenState.sortState !is SortState.NotShown) {
                SortBox(
                    screenState.sortState,
                    noteListViewModel::sortByHeadline,
                    noteListViewModel::sortByDate,
                    noteListViewModel::noneSort,
                    modifier = Modifier.padding(horizontal = 10.dp)
                )
            }

            if (screenState.notes.isEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = painterResource(R.drawable.no_notes),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(70.dp)
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(modifier = Modifier.weight(1f))
            }

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
            ) {
                items(items = screenState.notes, key = { it.id }) { item ->
                    val selected = screenState.selectionState is SelectionState.Selection &&
                            screenState.selectionState.notes.find { item.id == it.id } != null
                    NoteItemInList(
                        note = item,
                        onClick = {
                            if (screenState.selectionState is SelectionState.NoSelection) {
                                onNoteClick(item.id, touchedPoint.x, touchedPoint.y)
                            } else {
                                if (selected) {
                                    noteListViewModel.onUnselectNote(item)
                                } else {
                                    noteListViewModel.onSelectNote(item)
                                }
                            }
                        },
                        onLongClick = {
                            if (screenState.selectionState is SelectionState.NoSelection) {
                                noteListViewModel.onSelectNote(item)
                            }
                        },
                        withSelection = screenState.selectionState is SelectionState.Selection,
                        selected = selected
                    )
                }
            }
        }
    }
}

@Composable
fun SortBox(
    sortState: SortState,
    onSortByHeadline: () -> Unit,
    onSortByDate: () -> Unit,
    onNoteSort: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            stringResource(R.string.sort_by),
            color = NoteTheme.colors.textPrimary,
            modifier = Modifier.padding(start = 5.dp)
        )

        ChipSortGroup(sortState, onSortByHeadline, onSortByDate, onNoteSort)
    }
}

@Composable
fun ChipSortGroup(
    sortState: SortState,
    onSortByHeadline: () -> Unit,
    onSortByDate: () -> Unit,
    onNoteSort: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        CustomChip(
            selected = (sortState as SortState.Sorted).sortByHeadline,
            onClick = {
                if (sortState.sortByHeadline) {
                    onNoteSort()
                } else {
                    onSortByHeadline()
                }
            },
            label = stringResource(R.string.headline_label),
            modifier = Modifier.weight(1f),
        )

        CustomChip(
            selected = sortState.sortByDate,
            onClick = {
                if (sortState.sortByDate) {
                    onNoteSort()
                } else {
                    onSortByDate()
                }
            },
            label = stringResource(R.string.date_label),
            modifier = Modifier.weight(1f)
        )
    }
}

private fun deleteNotes(
    noteListViewModel: NoteListViewModel,
    snackbarHost: CustomSnackbarHost
) {
    noteListViewModel.onRemoveSelection()
    noteListViewModel.addSelectedNotesToTrash()
    snackbarHost.showSnackbar(
        message = "Are you sure you want to delete?",
        actionLabel = "Undo",
        onActionPerformed = {
            noteListViewModel.removeSelectedNotesFromTrash()
        },
        onDismiss = {
            noteListViewModel.deleteSelectedNotes()
        }
    )
}

@Composable
private fun TopBar(
    selectionState: SelectionState,
    sortState: SortState,
    snackbarHost: CustomSnackbarHost,
    noteListViewModel: NoteListViewModel
) {
    if (selectionState is SelectionState.Selection) {
        TopBarWithCancel(
            title = "${stringResource(R.string.selected)}: ${selectionState.notes.size}",
            onCancelClick = noteListViewModel::clearSelection,
            actions = {
                IconButton(onClick = {
                    deleteNotes(
                        noteListViewModel,
                        snackbarHost
                    )
                }) {
                    Icon(
                        painter = painterResource(R.drawable.delete_24dp),
                        contentDescription = stringResource(R.string.delete_all)
                    )
                }
            },
        )
    } else {
        TopBarWithAction(
            title = stringResource(R.string.notes),
            onActionClick = {
                if (sortState is SortState.NotShown) {
                    noteListViewModel.showSortOptions()
                } else {
                    noteListViewModel.hideSortOptions()
                }
            },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.filter_list_24dp),
                    contentDescription = stringResource(R.string.sort_by)
                )
            }
        )
    }
}

