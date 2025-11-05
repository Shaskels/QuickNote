package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.DeletedNotesRepository
import javax.inject.Inject

class AddToDeletedNotesUseCase @Inject constructor(private val deletedNotesRepository: DeletedNotesRepository) :
    suspend (Note) -> Unit by deletedNotesRepository::saveNote