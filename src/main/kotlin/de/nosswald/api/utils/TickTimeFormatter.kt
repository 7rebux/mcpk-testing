package de.nosswald.api.utils

import java.text.SimpleDateFormat
import java.util.*

class TickTimeFormatter(var ticks: Long) {
    private fun toMillis(): Long = ticks * 50L

    fun format(pattern: String): String = SimpleDateFormat(pattern).format(Date(toMillis()))
}
