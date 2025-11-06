package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.Sorts
import com.example.quicknote.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(private val notesRepository: NotesRepository) :
        (String, Sorts) -> Flow<List<Note>> by notesRepository::getNotesByQuery