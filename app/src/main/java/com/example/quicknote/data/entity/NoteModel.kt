package com.example.quicknote.data.entity

import kotlinx.serialization.Serializable

@Serializable
data class NoteModel(
    val id: String,
    val headline: String,
    val value: String,
    val timeOfChange: String,
    val images: List<String>
)