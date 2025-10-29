package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.NotesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetNoteByIdUseCase @Inject constructor(private val notesRepository: NotesRepository) :
        (String) -> Flow<Note> by { id: String -> notesRepository.getNoteById(id) }