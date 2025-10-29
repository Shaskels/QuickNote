package com.example.quicknote.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.quicknote.domain.Note
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun NoteItemInList(note: Note, onClick: () -> Unit, onLongClick: () -> Unit) {
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