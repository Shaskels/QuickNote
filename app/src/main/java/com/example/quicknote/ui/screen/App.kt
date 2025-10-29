package com.example.quicknote.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.List
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.NoteViewModel
import com.example.quicknote.ui.theme.NoteTheme

@Composable
fun App(noteViewModel: NoteViewModel = hiltViewModel()) {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = NavigationOptions.entries.firstOrNull {
        backStackEntry?.destination?.hasRoute(it.route) ?: false
    }

    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        bottomBar = {
            if (selectedTab != null) {
                NavigationBar(
                    containerColor = NoteTheme.colors.backgroundColor,
                    contentColor = NoteTheme.colors.textSecondary,
                ) {
                    NavigationBarItem(
                        selected = selectedTab == NavigationOptions.NOTE_LIST,
                        onClick = { navController.openPoppingAllPrevious(Route.NoteList) },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.List,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Notes") },
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
                    NavigationBarItem(
                        selected = selectedTab == NavigationOptions.DELETED_NOTE_LIST,
                        onClick = { navController.openPoppingAllPrevious(Route.DeletedNoteList) },
                        icon = {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp)
                            )
                        },
                        label = { Text("Trash") },
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
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            NavHost(
                navController = navController,
                startDestination = Route.NoteList,
            ) {
                composable<Route.NoteList> {
                    NoteListScreen(
                        onNoteClick = { noteId ->
                            navController.navigate(Route.ExistingNote(noteId))
                        },
                        onAddNoteClick = {
                            navController.navigate(Route.NewNote)
                        }
                    )
                }
                composable<Route.NewNote> {
                    NoteScreen(
                        note = Note(value = "", headline = ""),
                        onBackClick = { navController.navigateUp() },
                        onSaveClick = { note ->
                            noteViewModel.addNote(note)
                            navController.navigateUp()
                        })
                }
                composable<Route.ExistingNote> { backStackEntry ->
                    val existingNote = backStackEntry.toRoute<Route.ExistingNote>()
                    val note by noteViewModel.getNoteById(existingNote.id)
                        .collectAsState(Note(value = "", headline = ""))
                    Log.d("note", "${note.id} ${note.value} ${note.headline}")
                    NoteScreen(
                        note = note,
                        onBackClick = { navController.navigateUp() },
                        onSaveClick = { note ->
                            noteViewModel.updateNote(note)
                            navController.navigateUp()
                        }
                    )
                }
                composable<Route.DeletedNoteList> {
                    DeletedNoteListScreen()
                }
            }

        }
    }

}

fun NavController.openPoppingAllPrevious(route: Any) {
    this.navigate(route) {
        popUpTo(graph.startDestinationId)
        launchSingleTop = true
    }
}

