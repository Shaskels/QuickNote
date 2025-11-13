package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.DeletedNotesRepository
import javax.inject.Inject

class AddToDeletedNotesUseCase @Inject constructor(private val deletedNotesRepository: DeletedNotesRepository) {
    suspend operator fun invoke(note: Note) {
        deletedNotesRepository.saveNote(note)
    }
}