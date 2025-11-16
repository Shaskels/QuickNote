package com.example.quicknote.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.theme.NoteTheme
import com.example.quicknote.util.formatter
import kotlinx.datetime.format

@Composable
fun NoteItemInList(
    note: Note,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    withSelection: Boolean,
    selected: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(
                onClick = onClick
            )
            .combinedClickable(
                onClick = onClick,
                onLongClick = onLongClick,
            )
            .clip(
                RoundedCornerShape(8.dp)
            )
            .background(color = NoteTheme.colors.noteBackground)
            .padding(12.dp)
    ) {
        if (note.headline.isNotEmpty()) {
            Text(
                note.headline,
                color = NoteTheme.colors.textPrimary,
                style = MaterialTheme.typography.titleSmall,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .padding(bottom = 10.dp),
            )
        }

        if (note.value.isNotEmpty()) {
            Text(
                note.value,
                color = NoteTheme.colors.textSecondary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 4,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        if (withSelection) {
            Row(
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    note.timeOfChange.format(formatter),
                    color = NoteTheme.colors.textLight,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 10.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                if (selected) {
                    Box(
                        modifier = Modifier
                            .padding(end = 3.dp)
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(NoteTheme.colors.backgroundBrand)
                            .padding(3.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.check_24dp),
                            contentDescription = null,
                            tint = NoteTheme.colors.noteBackground
                        )
                    }
                } else {
                    Box(
                        modifier = Modifier
                            .padding(end = 3.dp)
                            .clip(CircleShape)
                            .size(20.dp)
                            .background(NoteTheme.colors.backgroundColor)
                    )
                }
            }
        } else {
            Text(
                note.timeOfChange.format(formatter),
                color = NoteTheme.colors.textLight,
                maxLines = 1,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(top = 10.dp)
            )
        }
    }
}