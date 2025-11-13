package com.example.quicknote.util

import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

fun getCurrentTime(): LocalDateTime {
    @OptIn(ExperimentalTime::class)
    val currentMoment: Instant = Clock.System.now()

    @OptIn(ExperimentalTime::class)
    return currentMoment.toLocalDateTime(TimeZone.currentSystemDefault())
}

@OptIn(FormatStringsInDatetimeFormats::class)
var formatter = LocalDateTime.Format {
    byUnicodePattern("dd.MM.yyyy HH:mm")
}
