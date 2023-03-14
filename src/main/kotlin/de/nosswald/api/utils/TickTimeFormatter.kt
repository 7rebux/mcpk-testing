package de.nosswald.api.utils

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

object TickTimeFormatter {
    fun format(ticks: Long): Pair<String, TimeUnit> {
        val millis = ticks.toMilliseconds()

        return when {
            ticks < 1200 -> SimpleDateFormat("ss.SSS").format(millis) to TimeUnit.SECONDS
            ticks < 72000 -> SimpleDateFormat("mm.ss.SSS").format(millis) to TimeUnit.MINUTES
            else -> SimpleDateFormat("HH.mm.ss.SSS")
                .also { it.timeZone = TimeZone.getTimeZone("GMT") }
                .format(millis) to TimeUnit.HOURS
        }
    }

    private fun Long.toMilliseconds() = this * 50
}
