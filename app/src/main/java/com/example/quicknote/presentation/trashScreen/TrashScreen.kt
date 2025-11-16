package com.example.quicknote.presentation.trashScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.presentation.component.NoteItemInList
import com.example.quicknote.presentation.component.TopBarWithAction
import com.example.quicknote.presentation.theme.NoteTheme

@Composable
fun TrashScreen(
    deletedNoteListViewModel: DeletedNoteListViewModel = hiltViewModel()
) {
    val noteList by deletedNoteListViewModel.deletedNotesFlow.collectAsState(emptyList())

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        topBar = {
            TopBarWithAction(
                title = stringResource(R.string.trash),
                onActionClick = {
                    deletedNoteListViewModel.clearTrash()
                },
                icon = {
                    Icon(
                        painter = painterResource(R.drawable.delete_24dp),
                        contentDescription = stringResource(R.string.delete_all)
                    )
                }
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {

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
                        onLongClick = {},
                        withSelection = false,
                        selected = false,
                    )
                }
            }
        }
    }
}