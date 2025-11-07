package com.example.quicknote.domain.repository

import com.example.quicknote.domain.Note
import kotlinx.coroutines.flow.Flow

interface DeletedNotesRepository {

    fun getNotes(): Flow<List<Note>>

    suspend fun saveNote(note: Note)

    suspend fun deleteNote(id: String)

    suspend fun deleteAllNotes()

}