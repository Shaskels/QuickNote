package com.example.quicknote.presentation.mainScreen

import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.quicknote.presentation.component.BottomNavigation
import com.example.quicknote.presentation.existingNoteScreen.ExistingNoteScreen
import com.example.quicknote.presentation.existingNoteScreen.ExistingNoteViewModel
import com.example.quicknote.presentation.newNoteScreen.NewNoteScreen
import com.example.quicknote.presentation.noteListScreen.NoteListScreen
import com.example.quicknote.presentation.theme.NoteTheme
import com.example.quicknote.presentation.trashScreen.TrashScreen

val LocalSnackbarHost = compositionLocalOf<CustomSnackbarHost> {
    error("No Snackbar Host State")
}
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val selectedTab = NavigationOptions.entries.firstOrNull {
        backStackEntry?.destination?.hasRoute(it.route) ?: false
    }
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val snackbarHost = remember { CustomSnackbarHost(scope, SnackbarHostState()) }

    val window = LocalWindowInfo.current
    val screenHeight = window.containerSize.height
    val screenWidth = window.containerSize.width

    CompositionLocalProvider(
        LocalSnackbarHost provides snackbarHost
    ) {
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
            },
            contentWindowInsets = WindowInsets(0, 0, 0, 0)
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Route.NoteList,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable<Route.NoteList>(
                    enterTransition = { slideInHorizontally(initialOffsetX = { -it }) },
                    exitTransition = { slideOutHorizontally(targetOffsetX = { -it }) }
                ) {
                    NoteListScreen(
                        onNoteClick = { noteId, touchX, touchY ->
                            navController.navigate(Route.ExistingNote(noteId, touchX, touchY))
                        },
                        onAddNoteClick = {
                            navController.navigate(Route.NewNote)
                        },
                    )
                }
                composable<Route.NewNote>(
                    enterTransition = {
                        scaleIn(
                            tween(500),
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
                            tween(500),
                            transformOrigin = TransformOrigin(
                                note.touchX / screenWidth,
                                note.touchY / screenHeight
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
                composable<Route.DeletedNoteList>(
                    enterTransition = { slideInHorizontally { it } },
                    exitTransition = { slideOutHorizontally { it } }
                ) {
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

