package de.nosswald.api.utils

class TickCounter {
    var ticks: Long = 0L
    var started = false

    fun start() {
        started = true
    }

    fun tick() {
        ticks++
    }

    fun stop(): Long {
        started = false
        return ticks
            .also { ticks = 0L }
    }
}
