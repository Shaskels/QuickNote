package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.repository.DeletedNotesRepository
import javax.inject.Inject

class ClearDeletedNotesUseCase @Inject constructor(deletedNotesRepository: DeletedNotesRepository) :
    suspend () -> Unit by deletedNotesRepository::deleteAllNotes