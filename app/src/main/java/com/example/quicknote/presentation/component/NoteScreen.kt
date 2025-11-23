package com.example.quicknote.presentation.component

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.PickMultipleVisualMedia
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.quicknote.R
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.theme.NoteTheme
import com.example.quicknote.util.formatter
import kotlinx.datetime.format
import timber.log.Timber

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun NoteScreen(
    note: Note,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    onHeadlineChanged: (String) -> Unit,
    onValueChanged: (String) -> Unit,
    onAddPhotos: (List<String>) -> Unit,
    onDeletePhoto: (String) -> Unit,
) {
    val localFocusManager = LocalFocusManager.current
    val context = LocalContext.current
    val pickMultipleMedia = rememberLauncherForActivityResult(PickMultipleVisualMedia(5)) { uris ->
        if (uris.isNotEmpty()) {
            onAddPhotos(uris.map { it.toString() })
            Timber.tag("PhotoPicker").d("Number of items selected: ${uris.size}")
        } else {
            Timber.tag("PhotoPicker").d("No media selected")
        }
    }
    val requestPermissions =
        rememberLauncherForActivityResult(RequestMultiplePermissions()) { results ->
            Timber.tag("PhotoPicker").d(results.toString())
        }


    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        bottomBar = {
            if (WindowInsets.isImeVisible) {
                BottomAppBar(
                    contentPadding = PaddingValues(horizontal = 10.dp),
                    actions = {
                        IconButton(onClick = {
                            if (checkPermissionGranted(context)) {
                                pickMultipleMedia.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            } else {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
                                } else {
                                    requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
                                }
                            }
                        }) {
                            Icon(
                                painterResource(R.drawable.photo_24dp),
                                modifier = Modifier.size(30.dp),
                                contentDescription = null
                            )
                        }
                    },
                    containerColor = NoteTheme.colors.noteBackground,
                    contentColor = NoteTheme.colors.textPrimary,
                    modifier = Modifier
                        .imePadding()
                        .height(60.dp)
                )
            }
        },
        topBar = {
            TopBarWithNavigation(
                onBackClick = onBackClick,
                onSaveClick = onSaveClick
            )
        },
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
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Next
                ),
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
                text = note.timeOfChange.format(formatter),
                color = NoteTheme.colors.textLight,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(start = 15.dp)
            )

            BrandTextField(
                value = note.value,
                onValueChanged = onValueChanged,
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                hint = stringResource(R.string.enter_your_note),
                modifier = Modifier
                    .fillMaxWidth()
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                userScrollEnabled = true,
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(items = note.images) { item ->
                    ImageWithDelete(
                        uri = item,
                        onDeleteClick = { onDeletePhoto(item) }
                    )
                }
            }
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
                    painter = painterResource(R.drawable.arrow_back_24dp),
                    contentDescription = stringResource(R.string.back_to_note_list)
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(
                    painter = painterResource(R.drawable.check_24dp),
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
        modifier = Modifier.fillMaxWidth()
    )
}

private fun checkPermissionGranted(context: Context): Boolean {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && ContextCompat.checkSelfPermission(
            context,
            READ_MEDIA_IMAGES
        ) == PERMISSION_GRANTED
    ) {
        true
    } else if (ContextCompat.checkSelfPermission(
            context,
            READ_EXTERNAL_STORAGE
        ) == PERMISSION_GRANTED
    ) {
        true
    } else false
}
