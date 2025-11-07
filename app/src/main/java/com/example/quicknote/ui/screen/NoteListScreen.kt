package com.example.quicknote.ui.screen

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import com.example.quicknote.presentation.NoteListScreenState
import com.example.quicknote.presentation.NoteListViewModel
import com.example.quicknote.ui.component.AddButton
import com.example.quicknote.ui.component.CustomChip
import com.example.quicknote.ui.component.NoteItemInList
import com.example.quicknote.ui.component.SearchField
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun NoteListScreen(
    snackbarHostState: SnackbarHostState,
    noteListViewModel: NoteListViewModel = hiltViewModel(),
    onNoteClick: (String, Float, Float) -> Unit,
    onAddNoteClick: () -> Unit,
    onShowSnackbar: (String, String, () -> Unit, () -> Unit) -> Unit,
) {

    val screenState = noteListViewModel.screenState.collectAsState()

    DisposableEffect(Unit) {
        onDispose { snackbarHostState.currentSnackbarData?.dismiss() }
    }

    when (val currentState = screenState.value) {
        is NoteListScreenState.Initial -> {}
        is NoteListScreenState.Error -> {}
        is NoteListScreenState.Content -> Screen(
            screenState = currentState,
            noteListViewModel = noteListViewModel,
            onNoteClick = onNoteClick,
            onShowSnackbar = onShowSnackbar,
            onAddNoteClick = onAddNoteClick,
        )
    }

}

@Composable
fun Screen(
    screenState: NoteListScreenState.Content,
    noteListViewModel: NoteListViewModel,
    onNoteClick: (String, Float, Float) -> Unit,
    onAddNoteClick: () -> Unit,
    onShowSnackbar: (String, String, () -> Unit, () -> Unit) -> Unit,
    modifier: Modifier = Modifier
) {
    var touchedPoint by remember { mutableStateOf(Offset.Zero) }
    var query by remember { mutableStateOf("") }

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        topBar = {
            TopBarWithAction(
                title = stringResource(R.string.notes),
                onSortClick = {
                    if (screenState.sortState is NoteListScreenState.SortState.NotShown) {
                        noteListViewModel.showSortOptions()
                    } else {
                        noteListViewModel.hideSortOptions()
                    }
                }
            )
        },
        floatingActionButton = {
            if (screenState.contentState is NoteListScreenState.ContentState.NoteList) {
                AddButton(
                    onClick = onAddNoteClick,
                    description = stringResource(R.string.add_note),
                    modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        modifier = modifier
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

            if (screenState.sortState !is NoteListScreenState.SortState.NotShown) {
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
                    NoteItemInList(
                        note = item,
                        onClick = {
                            onNoteClick(item.id, touchedPoint.x, touchedPoint.y)
                        },
                        onLongClick = {
                            noteListViewModel.addNoteToTrash(item)
                            onShowSnackbar(
                                "Are you sure you want to delete?",
                                "Undo",
                                { noteListViewModel.removeNoteFromTrash(item.id) },
                                { noteListViewModel.deleteNote(item.id) }
                            )
                        },
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithAction(title: String, onSortClick: () -> Unit) {
    TopAppBar(
        title = {
            Text(text = title, style = MaterialTheme.typography.titleLarge)
        },
        actions = {
            IconButton(onClick = onSortClick) {
                Icon(
                    painter = painterResource(R.drawable.filter_list_24dp),
                    contentDescription = stringResource(R.string.note_done)
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NoteTheme.colors.backgroundColor,
            navigationIconContentColor = NoteTheme.colors.textPrimary,
            actionIconContentColor = NoteTheme.colors.textPrimary,
            titleContentColor = NoteTheme.colors.textPrimary,
        ),
        windowInsets = WindowInsets(top = 0.dp),
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun SortBox(
    sortState: NoteListScreenState.SortState,
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
    sortState: NoteListScreenState.SortState,
    onSortByHeadline: () -> Unit,
    onSortByDate: () -> Unit,
    onNoteSort: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {

        CustomChip(
            selected = (sortState as NoteListScreenState.SortState.Sorted).sortByHeadline,
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


