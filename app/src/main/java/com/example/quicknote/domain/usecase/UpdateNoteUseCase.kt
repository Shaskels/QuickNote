package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.NotesRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) :
    suspend (Note) -> Unit by { note: Note -> notesRepository.updateNote(note) }