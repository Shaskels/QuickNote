package com.example.quicknote.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.ListViewModel
import com.example.quicknote.ui.component.BrandTextField
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun NoteListScreen(listViewModel: ListViewModel, onNoteClick: () -> Unit) {
    val noteList by listViewModel.notesFlow.collectAsState(emptyList())
    val note = rememberTextFieldState()

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        SaveNoteBox(
            note = note,
            onSaveClick = {
                if (note.text.isNotEmpty()) {
                    listViewModel.addNote(Note(value = note.text.toString()))
                    note.clearText()
                }
            }
        )

        Text(
            text = stringResource(R.string.notes),
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 15.dp),
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
                    note = item.value,
                    onClick = onNoteClick,
                    onLongClick = {
                        listViewModel.deleteNote(item.id)
                    }
                )
            }
        }
    }
}

@Composable
fun SaveNoteBox(note: TextFieldState, onSaveClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp)
    ) {
        BrandTextField(
            state = note,
            hint = stringResource(R.string.enter_your_note),
            onKeyboardAction = onSaveClick,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(end = 10.dp)
        )

        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically),
            colors = ButtonDefaults.buttonColors(
                containerColor = NoteTheme.colors.backgroundBrand,
                contentColor = NoteTheme.colors.noteBackground
            )
        ) {
            Text(stringResource(R.string.save))
        }
    }
}

@Composable
fun ListItem(note: String, onClick: () -> Unit, onLongClick: () -> Unit) {
    Row(
        modifier = Modifier
            .combinedClickable(
                onLongClick = onLongClick,
                onClick = onClick,
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(color = NoteTheme.colors.noteBackground)
            .padding(8.dp)
    ) {
        Text(
            note,
            modifier = Modifier.align(Alignment.CenterVertically),
            color = NoteTheme.colors.textPrimary
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    NoteListScreen(listViewModel = hiltViewModel(), {})
}