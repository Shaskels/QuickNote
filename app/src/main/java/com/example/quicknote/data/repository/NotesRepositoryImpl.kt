package com.example.quicknote.data.repository

import com.example.quicknote.data.datastore.NotesDataSource
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.NotesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow

class NotesRepositoryImpl @Inject constructor(private val notesDataSource: NotesDataSource) :
    NotesRepository {

    override fun getNotes(): Flow<List<Note>> = notesDataSource.notes

    override suspend fun saveNote(note: Note) {
        notesDataSource.saveNewNote(note)
    }

    override suspend fun deleteNote(id: String) {
        notesDataSource.deleteNote(id)
    }

}