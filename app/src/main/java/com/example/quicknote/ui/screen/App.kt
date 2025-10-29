package com.example.quicknote.ui.screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.example.quicknote.ui.component.BottomNavigation
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
                BottomNavigation(
                    navigationOptions = NavigationOptions.entries,
                    selectedNavigationOption = selectedTab,
                    onItemClicked = { navOption ->
                        when (navOption) {
                            NavigationOptions.NOTE_LIST -> navController.openPoppingAllPrevious(
                                Route.NoteList
                            )

                            NavigationOptions.DELETED_NOTE_LIST -> navController.openPoppingAllPrevious(
                                Route.DeletedNoteList
                            )
                        }
                    }
                )
            }
        }
    ) { paddingValues ->
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
                    TrashScreen()
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

