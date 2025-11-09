package com.example.quicknote.data.entity

import kotlinx.serialization.Serializable
import kotlin.uuid.ExperimentalUuidApi

@Serializable
data class NoteModel @OptIn(ExperimentalUuidApi::class) constructor(
    val id: String,
    val headline: String,
    val value: String,
    val timeOfChange: String,
)