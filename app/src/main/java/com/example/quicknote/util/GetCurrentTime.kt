package com.example.quicknote.util

import java.time.LocalDateTime

fun getCurrentTime(): String {
    val time = LocalDateTime.now()
    return "${time.dayOfMonth}.${time.monthValue}.${time.year} ${time.hour}:${time.minute}"
}