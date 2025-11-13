package com.example.quicknote.data.entity

import com.example.quicknote.domain.Note
import com.example.quicknote.util.formatter
import com.example.quicknote.util.getCurrentTime
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.format
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

fun NoteModel.toNote(): Note = Note(
    this.id,
    this.headline,
    this.value,
    LocalDateTime.parse(this.timeOfChange, formatter)
)

@OptIn(ExperimentalUuidApi::class)
fun Note.toNoteModel(): NoteModel = NoteModel(
    id = if (id == "") Uuid.random().toString() else this.id,
    headline = this.headline,
    value = this.value,
    timeOfChange = getCurrentTime().format(formatter)
)