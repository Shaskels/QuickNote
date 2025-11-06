package com.example.quicknote.ui.screen

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.ui.component.BrandTextField
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun NoteScreen(
    note: Note,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHeadlineChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        topBar = {
            TopBarWithNavigation(
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .scrollable(
                    rememberScrollState(), orientation = Orientation.Vertical
                )
                .padding(innerPadding)
        ) {
            BrandTextField(
                value = note.headline,
                onValueChanged = onHeadlineChanged,
                hint = stringResource(R.string.headline),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                keyboardActions = KeyboardActions(onNext = {
                    localFocusManager.moveFocus(
                        FocusDirection.Down
                    )
                }),
                modifier = Modifier
                    .fillMaxWidth(),
                textStyle = MaterialTheme.typography.titleMedium
            )

            Text(
                text = note.timeOfChange,
                color = NoteTheme.colors.textLight,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 15.dp)
            )

            BrandTextField(
                value = note.value,
                onValueChanged = onValueChanged,
                hint = stringResource(R.string.enter_your_note),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onSaveClick() }),
                modifier = Modifier
                    .fillMaxWidth()
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBarWithNavigation(onBackClick: () -> Unit, onSaveClick: () -> Unit) {
    TopAppBar(
        title = {},
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Rounded.ArrowBack,
                    contentDescription = stringResource(R.string.back_to_note_list)
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(R.string.note_done)
                )
            }
        },
        expandedHeight = 40.dp,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = NoteTheme.colors.backgroundColor,
            navigationIconContentColor = NoteTheme.colors.textPrimary,
            actionIconContentColor = NoteTheme.colors.textPrimary
        ),
        windowInsets = WindowInsets(top = 0.dp),
        modifier = Modifier.fillMaxWidth()
    )
}
