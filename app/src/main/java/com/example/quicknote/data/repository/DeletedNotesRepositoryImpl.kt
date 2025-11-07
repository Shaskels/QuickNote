package com.example.quicknote.data.repository

import com.example.quicknote.data.datasource.DeletedNotesDataSource
import com.example.quicknote.data.entity.toNote
import com.example.quicknote.data.entity.toNoteModel
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.DeletedNotesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DeletedNotesRepositoryImpl @Inject constructor(private val deletedNotesDataSource: DeletedNotesDataSource) :
    DeletedNotesRepository {

    override fun getNotes(): Flow<List<Note>> =
        deletedNotesDataSource.notes.map { it.map { it.toNote() } }

    override suspend fun saveNote(note: Note) {
        deletedNotesDataSource.saveNote(note.toNoteModel())
    }

    override suspend fun deleteNote(id: String) {
        deletedNotesDataSource.deleteNote(id)
    }
}