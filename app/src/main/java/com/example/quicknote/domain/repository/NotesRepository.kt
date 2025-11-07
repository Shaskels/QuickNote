package com.example.quicknote.domain.repository

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesByQuery(
        query: String,
        sorts: Sorts,
    ): Flow<List<Note>>

    suspend fun saveNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(id: String)

    fun getNoteById(id: String): Flow<Note>
}
