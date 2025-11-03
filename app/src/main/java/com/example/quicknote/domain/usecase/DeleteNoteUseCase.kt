package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.repository.NotesRepository
import javax.inject.Inject

class DeleteNoteUseCase @Inject constructor(notesRepository: NotesRepository) :
    suspend (String) -> Unit by notesRepository::deleteNote