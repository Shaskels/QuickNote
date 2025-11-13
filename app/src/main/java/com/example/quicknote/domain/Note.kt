package com.example.quicknote.domain

import kotlinx.datetime.LocalDateTime

data class Note(
    val id: String,
    val headline: String,
    val value: String,
    val timeOfChange: LocalDateTime
)


