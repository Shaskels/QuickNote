package com.example.quicknote.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.example.quicknote.R
import com.example.quicknote.presentation.theme.NoteTheme

@Composable
fun ImageWithDelete(uri: String, onDeleteClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {

        AsyncImage(
            model = uri,
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(8.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        IconButton(
            onClick = onDeleteClick,
            colors = IconButtonColors(
                containerColor = NoteTheme.colors.noteBackground,
                contentColor = NoteTheme.colors.noteBackground,
                disabledContentColor = NoteTheme.colors.noteBackground,
                disabledContainerColor = NoteTheme.colors.noteBackground,
            ),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(25.dp)
                .clip(CircleShape)
        ) {
            Icon(
                painterResource(R.drawable.close_24dp),
                contentDescription = null,
                tint = NoteTheme.colors.textPrimary
            )
        }
    }
}