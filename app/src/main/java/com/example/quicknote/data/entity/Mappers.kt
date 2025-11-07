package com.example.quicknote.data.entity

import com.example.quicknote.domain.Note
import com.example.quicknote.util.formatter
import java.time.LocalDateTime

fun NoteModel.toNote(): Note = Note(
    this.id,
    this.headline,
    this.value,
    LocalDateTime.parse(this.timeOfChange, formatter)
)

fun Note.toNoteModel(): NoteModel = NoteModel(
    headline = this.headline,
    value = this.value,
    timeOfChange = LocalDateTime.now().format(formatter)
)