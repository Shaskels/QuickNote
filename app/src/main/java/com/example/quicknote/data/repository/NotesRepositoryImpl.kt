package com.example.quicknote.data.repository

import com.example.quicknote.data.datasource.NotesDataSource
import com.example.quicknote.data.entity.toNote
import com.example.quicknote.data.entity.toNoteModel
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import com.example.quicknote.domain.repository.NotesRepository
import jakarta.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class NotesRepositoryImpl @Inject constructor(private val notesDataSource: NotesDataSource) :
    NotesRepository {

    override fun getNotesByQuery(
        query: String,
        sorts: Sorts
    ): Flow<List<Note>> {
        return notesDataSource.getNotesByQuery(query, sorts)
    }

    override suspend fun saveNote(note: Note) {
        notesDataSource.saveNote(note.toNoteModel())
    }

    override suspend fun updateNote(note: Note) {
        notesDataSource.saveNote(note.toNoteModel())
    }

    override suspend fun deleteNote(id: String) {
        notesDataSource.deleteNote(id)
    }

    override fun getNoteById(id: String): Flow<Note> {
        return notesDataSource.getNoteByKey(id).map { it.toNote() }
    }

}