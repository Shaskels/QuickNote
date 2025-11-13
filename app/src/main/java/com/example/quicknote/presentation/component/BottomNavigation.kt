package com.example.quicknote.presentation.component

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import com.example.quicknote.R
import com.example.quicknote.presentation.mainScreen.NavigationOptions
import com.example.quicknote.presentation.theme.NoteTheme

@Composable
fun BottomNavigation(
    navigationOptions: List<NavigationOptions>,
    selectedNavigationOption: NavigationOptions,
    onItemClicked: (NavigationOptions) -> Unit,
) {
    NavigationBar(
        containerColor = NoteTheme.colors.backgroundColor,
        contentColor = NoteTheme.colors.textSecondary,
    ) {
        for (option in navigationOptions) {
            NavigationBarItem(
                selected = selectedNavigationOption == option,
                onClick = { onItemClicked(option) },
                icon = { Icon(painter = getIcon(option), null) },
                label = {
                    Text(
                        text = getLabel(option),
                    )
                },
                colors = NavigationBarItemColors(
                    selectedIconColor = NoteTheme.colors.textPrimary,
                    selectedTextColor = NoteTheme.colors.textPrimary,
                    selectedIndicatorColor = NoteTheme.colors.backgroundColor,
                    unselectedIconColor = NoteTheme.colors.textLight,
                    unselectedTextColor = NoteTheme.colors.textLight,
                    disabledIconColor = NoteTheme.colors.textSecondary,
                    disabledTextColor = NoteTheme.colors.textSecondary,
                )
            )
        }
    }
}

@Composable
private fun getIcon(option: NavigationOptions): Painter =
    when (option) {
        NavigationOptions.NOTE_LIST -> painterResource(R.drawable.list_24dp)
        NavigationOptions.DELETED_NOTE_LIST -> painterResource(R.drawable.delete_24dp)
    }

@Composable
private fun getLabel(option: NavigationOptions): String = stringResource(
    when (option) {
        NavigationOptions.NOTE_LIST -> R.string.notes
        NavigationOptions.DELETED_NOTE_LIST -> R.string.trash
    }
)