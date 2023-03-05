package de.nosswald.api.utils

import java.util.*

class TickCounter(val playerId: UUID) {
    var ticks: Long = 0L
    var started = false

    init {
        counters += this
    }

    fun start() {
        ticks = 1L
        started = true
    }

    fun stop(): Long {
        started = false
        return ticks
            .also { ticks = 0L }
    }

    companion object {
        val counters = mutableSetOf<TickCounter>()

        fun tick(playerId: UUID) {
            counters
                .filter { it.playerId == playerId && it.started }
                .forEach { it.ticks++ }
        }
    }
}
