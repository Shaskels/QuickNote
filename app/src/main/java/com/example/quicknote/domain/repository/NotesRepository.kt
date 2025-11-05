package com.example.quicknote.domain.repository

import com.example.quicknote.domain.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    fun getNotesByQuery(query: String): Flow<List<Note>>

    suspend fun saveNote(note: Note)

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(id: String)

    fun getNoteById(id: String): Flow<Note>
}
