package de.nosswald.api

import de.nosswald.api.utils.TickCounter
import org.bukkit.Location

import java.util.UUID

data class PlayerData(val playerId: UUID) {
    val practiceData = PracticeData(playerId)

    data class PracticeData(
        val playerId: UUID,
        var enabled: Boolean = false,
        var location: Location? = null,
        var timer: TickCounter = TickCounter(playerId),
    )
}
