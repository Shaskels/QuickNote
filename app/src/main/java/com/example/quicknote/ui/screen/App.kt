package com.example.quicknote.ui.screen

import android.util.Log
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quicknote.presentation.ExistingNoteViewModel
import com.example.quicknote.ui.component.BottomNavigation
import com.example.quicknote.ui.theme.NoteTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {

    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = NavigationOptions.entries.firstOrNull {
        backStackEntry?.destination?.hasRoute(it.route) ?: false
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val context = LocalContext.current
    val displayMetrics = context.resources.displayMetrics
    val screenHeight = displayMetrics.heightPixels
    val screenWidth = displayMetrics.widthPixels


    Scaffold(
        containerColor = NoteTheme.colors.backgroundColor,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) {
                Snackbar(
                    snackbarData = it,
                    containerColor = NoteTheme.colors.textLight,
                    contentColor = NoteTheme.colors.textPrimary,
                    actionColor = NoteTheme.colors.backgroundBrand,
                )
            }
        },
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
                        snackbarHostState = snackbarHostState,
                        onNoteClick = { noteId, x, y ->
                            navController.navigate(Route.ExistingNote(noteId, x, y))
                        },
                        onAddNoteClick = {
                            navController.navigate(Route.NewNote)
                        },
                        onShowSnackbar = { message, action, onAction, onDismiss ->
                            showSnackbar(
                                scope = scope,
                                snackbarHostState = snackbarHostState,
                                message = message,
                                actionLabel = action,
                                onActionPerformed = onAction,
                                onDismiss = onDismiss
                            )
                        }
                    )
                }
                composable<Route.NewNote>(
                    enterTransition = {
                        scaleIn(
                            tween(700),
                            transformOrigin = TransformOrigin(1f, 1f)
                        )
                    },
                    exitTransition = { scaleOut() }
                ) {
                    NewNoteScreen(
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable<Route.ExistingNote>(
                    enterTransition = {
                        val note = this.targetState.toRoute<Route.ExistingNote>()
                        scaleIn(
                            tween(700),
                            transformOrigin = TransformOrigin(
                                note.x / screenWidth,
                                note.y / screenHeight
                            )
                        )
                    },
                    exitTransition = { scaleOut() }
                ) { backStackEntry ->
                    val existingNote = backStackEntry.toRoute<Route.ExistingNote>()
                    val noteViewModel = hiltViewModel(
                        creationCallback = { factory: ExistingNoteViewModel.NoteViewModelFactory ->
                            factory.create(existingNote.id)
                        }
                    )
                    ExistingNoteScreen(
                        existingNoteViewModel = noteViewModel,
                        onBackClick = { navController.navigateUp() }
                    )
                }
                composable<Route.DeletedNoteList>() {
                    TrashScreen()
                }
            }

        }
    }

}

private fun showSnackbar(
    scope: CoroutineScope,
    snackbarHostState: SnackbarHostState,
    message: String,
    actionLabel: String,
    onActionPerformed: () -> Unit,
    onDismiss: () -> Unit
) {
    scope.launch {
        val result = snackbarHostState
            .showSnackbar(
                message = message,
                actionLabel = actionLabel,
                duration = SnackbarDuration.Short
            )
        when (result) {
            SnackbarResult.ActionPerformed -> onActionPerformed()
            SnackbarResult.Dismissed -> {
                Log.d("Snackbar", " Dismiss")
                onDismiss()
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

