package com.example.quicknote.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.presentation.NoteListViewModel
import com.example.quicknote.ui.component.AddButton
import com.example.quicknote.ui.component.NoteItemInList
import com.example.quicknote.ui.theme.NoteTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NoteListScreen(
    noteListViewModel: NoteListViewModel = hiltViewModel(),
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
) {
    val noteList by noteListViewModel.notesFlow.collectAsState(emptyList())
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

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
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = NoteTheme.colors.textLight,
                    contentColor = NoteTheme.colors.textPrimary,
                    actionColor = NoteTheme.colors.backgroundBrand,
                )
            }
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Text(
                text = stringResource(R.string.notes),
                modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
                style = MaterialTheme.typography.titleLarge
            )

            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                items(items = noteList, key = { it.id }) { item ->
                    NoteItemInList(
                        note = item,
                        onClick = {
                            onNoteClick(item.id)
                        },
                        onLongClick = {
                            noteListViewModel.addNoteToTrash(item)
                            showSnackbar(
                                scope = scope,
                                snackbarHostState = snackbarHostState,
                                onActionPerformed = { noteListViewModel.removeNoteFromTrash(item.id) },
                                onDismiss = { noteListViewModel.deleteNote(item.id) }
                            )
                        }
                    )
                }
            }
        }
    }
}

private fun showSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = "Are you sure you want to delete?",
                actionLabel = "Undo",
                duration = SnackbarDuration.Short
            )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> onDismiss()
        }
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    NoteListScreen(noteListViewModel = hiltViewModel(), {}, {})
}