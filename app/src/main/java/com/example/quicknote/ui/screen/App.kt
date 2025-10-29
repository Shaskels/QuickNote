package com.example.quicknote.ui.screen

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quicknote.domain.Note
import com.example.quicknote.presentation.NoteViewModel

@Composable
fun App(noteViewModel: NoteViewModel = hiltViewModel()) {
    val navController = rememberNavController()


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
    }
}
