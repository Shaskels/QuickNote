package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.DeletedNotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetDeletedNotesUseCase @Inject constructor(private val deletedNotesRepository: DeletedNotesRepository) {
    operator fun invoke(): Flow<List<Note>> {
        return deletedNotesRepository.getNotes()
    }
}