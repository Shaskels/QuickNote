package com.example.quicknote.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.presentation.DeletedNoteListViewModel
import com.example.quicknote.ui.component.NoteItemInList
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun TrashScreen(
    deletedNoteListViewModel: DeletedNoteListViewModel = hiltViewModel()
) {
    val noteList by deletedNoteListViewModel.deletedNotesFlow.collectAsState(emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.trash),
            modifier = Modifier.padding(vertical = 15.dp, horizontal = 15.dp),
            color = NoteTheme.colors.textPrimary,
            style = MaterialTheme.typography.titleLarge,
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
                    onClick = { deletedNoteListViewModel.restoreDeletedNote(item) },
                    onLongClick = { deletedNoteListViewModel.deleteNoteFromTrash(item.id) },
                )
            }
        }
    }
}