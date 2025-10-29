package com.example.quicknote.data.repository

import com.example.quicknote.data.datasource.DeletedNotesDataSource
import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.DeletedNotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeletedNotesRepositoryImpl @Inject constructor(private val deletedNotesDataSource: DeletedNotesDataSource) :
    DeletedNotesRepository {

    override fun getNotes(): Flow<List<Note>> = deletedNotesDataSource.notes

    override suspend fun saveNote(note: Note) {
        deletedNotesDataSource.saveNote(note)
    }

    override suspend fun deleteNote(id: String) {
        deletedNotesDataSource.deleteNote(id)
    }
}