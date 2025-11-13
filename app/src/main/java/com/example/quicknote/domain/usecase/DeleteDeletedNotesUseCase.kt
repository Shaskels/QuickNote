package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.repository.DeletedNotesRepository
import javax.inject.Inject

class DeleteDeletedNotesUseCase @Inject constructor(private val deletedNotesRepository: DeletedNotesRepository) {
    suspend operator fun invoke(id: String) {
        deletedNotesRepository.deleteNote(id)
    }
}