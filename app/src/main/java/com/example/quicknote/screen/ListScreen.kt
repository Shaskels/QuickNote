package com.example.quicknote.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import kotlinx.coroutines.launch

@Composable
fun ListScreen() {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val noteList by dataStoreManager.notes.collectAsState(emptyList())
    val note = rememberTextFieldState()

    fun saveValues() {
        if (note.text.isNotEmpty()) {
            val text = "" + System.currentTimeMillis() + "|${note.text}"
            scope.launch {
                dataStoreManager.saveNotes((noteList + text))
            }
            note.clearText()
        }
    }

    fun deleteValue(index: Int) {
        scope.launch {
            val notes = noteList.toMutableList()
            notes.removeAt(index)
            dataStoreManager.saveNotes(notes)
        }
    }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SaveNoteBox(
                note = note,
                onSaveClick = {
                    saveValues()
                }
            )
            LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalItemSpacing = 8.dp
            ) {
                itemsIndexed(items = noteList) { index, item ->
                    ListItem(
                        note = item,
                        onClick = {},
                        onLongClick = {
                            deleteValue(index)
                        }
                    )
                }
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
        TextField(
            state = note,
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 5),
            label = { Text(stringResource(R.string.enter_your_note)) },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            onKeyboardAction = { onSaveClick() },
            modifier = Modifier
                .fillMaxWidth(0.75f)
                .padding(end = 10.dp)
        )
        Button(
            onClick = onSaveClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterVertically)
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
            .background(color = MaterialTheme.colorScheme.surfaceContainer)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                shape = RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Text(
            note.split("|", limit = 2).last(),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ListScreen()
}