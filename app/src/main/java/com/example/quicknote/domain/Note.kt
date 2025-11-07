package com.example.quicknote.domain

import java.time.LocalDateTime

data class Note(
    val id: String,
    val headline: String,
    val value: String,
    val timeOfChange: LocalDateTime
)


