package com.example.quicknote.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.quicknote.presentation.ListViewModel

@Composable
fun App(listViewModel: ListViewModel = hiltViewModel()) {
    val navController = rememberNavController()

    Scaffold { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = NoteList,
            modifier = Modifier.padding(paddingValues)
        ) {
            composable<NoteList> {
                ListScreen(listViewModel)
            }
        }
    }

}