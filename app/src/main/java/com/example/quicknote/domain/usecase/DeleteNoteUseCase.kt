package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) {
    suspend operator fun invoke(id: String) {
        notesRepository.deleteNote(id)
    }
}