package com.example.quicknote.screen

import android.util.Log
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.quicknote.R

@Composable
fun ListScreen() {
    val note = rememberTextFieldState()
    val noteList = remember { mutableStateListOf<String>() }
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SaveNoteBox(
                note = note,
                onSaveClick = {
                    if (note.text.isNotEmpty()) {
                        noteList.add(note.text.toString())
                        note.clearText()
                    }
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(items = noteList) { index, item ->
                    ListItem(
                        note = item,
                        onClick = {},
                        onLongClick = {
                            noteList.removeRange(index, index + 1)
                            Log.d("Grid", "$index")
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
            lineLimits = TextFieldLineLimits.MultiLine(maxHeightInLines = 10),
            label = { Text(stringResource(R.string.enter_your_note)) },
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
        Text(note, modifier = Modifier.align(Alignment.CenterVertically))
    }
}

@Preview
@Composable
private fun ListScreenPreview() {
    ListScreen()
}