package de.nosswald.api

import de.nosswald.api.utils.TickCounter
import org.bukkit.Location

import java.util.UUID

data class PlayerData(val playerId: UUID) {
    val practiceData = PracticeData()
    val parkourData = ParkourData()

    data class PracticeData(
        var enabled: Boolean = false,
        var location: Location? = null,
        var timer: TickCounter = TickCounter(),
    )

    data class ParkourData(
        var enabled: Boolean = false,
        var parkour: Parkour? = null,
        var timer: TickCounter = TickCounter(),
    )
}
