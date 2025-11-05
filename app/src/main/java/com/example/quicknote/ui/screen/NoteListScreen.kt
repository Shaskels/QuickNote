package com.example.quicknote.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.presentation.NoteListViewModel
import com.example.quicknote.ui.component.AddButton
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
    val noteList by noteListViewModel.notesFlow.collectAsState(emptyList())
    var touchedPoint by remember { mutableStateOf(Offset.Zero) }
    val query = noteListViewModel.queryState.collectAsState()

    DisposableEffect(Unit) {
        onDispose { snackbarHostState.currentSnackbarData?.dismiss() }
    }

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        floatingActionButton = {
            AddButton(
                onClick = onAddNoteClick,
                description = "add note",
                modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
            )
        },
        floatingActionButtonPosition = FabPosition.End,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
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
            Text(
                text = stringResource(R.string.notes),
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                style = MaterialTheme.typography.titleLarge
            )

            SearchField(
                query = query.value,
                onQueryChange = noteListViewModel::onQueryChange,
                onClearQueryClick = noteListViewModel::onClearQuery,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp, start = 10.dp, end = 10.dp)
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp,
            ) {
                items(items = noteList, key = { it.id }) { item ->
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


