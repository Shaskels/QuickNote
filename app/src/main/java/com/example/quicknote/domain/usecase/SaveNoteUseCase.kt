package com.example.quicknote.domain.usecase

import com.example.quicknote.domain.Note
import com.example.quicknote.domain.repository.NotesRepository
import jakarta.inject.Inject

class SaveNoteUseCase @Inject constructor(private val notesRepository: NotesRepository) :
    suspend (Note) -> Unit by { note: Note -> notesRepository.saveNote(note) }