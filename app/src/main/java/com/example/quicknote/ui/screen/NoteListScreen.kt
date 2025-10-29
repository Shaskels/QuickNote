package com.example.quicknote.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.NoteListViewModel
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun NoteListScreen(
    noteListViewModel: NoteListViewModel = hiltViewModel(),
    onNoteClick: (String) -> Unit,
    onAddNoteClick: () -> Unit,
) {
    val noteList by noteListViewModel.notesFlow.collectAsState(emptyList())

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        floatingActionButton = {
            FloatingActionButton(
                shape = CircleShape,
                onClick = onAddNoteClick,
                containerColor = NoteTheme.colors.backgroundBrand,
                contentColor = NoteTheme.colors.noteBackground,
                elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp),
                modifier = Modifier.padding(bottom = 15.dp, end = 15.dp)
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "add note")
            }
        },
        floatingActionButtonPosition = FabPosition.EndOverlay
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
                    ListItem(
                        note = item,
                        onClick = {
                            onNoteClick(item.id)
                        },
                        onLongClick = {
                            noteListViewModel.deleteNote(item.id)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun ListItem(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
    Column(
        modifier = Modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick,
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(color = NoteTheme.colors.noteBackground)
            .padding(12.dp)
    ) {
        Text(
            note.headline,
            color = NoteTheme.colors.textPrimary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            modifier = Modifier
                .padding(bottom = 10.dp),
        )

        Text(
            note.value,
            color = NoteTheme.colors.textSecondary,
            overflow = TextOverflow.Ellipsis,
            maxLines = 4,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(),
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    NoteListScreen(noteListViewModel = hiltViewModel(), {}, {})
}