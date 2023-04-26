package de.nosswald.server.listener

import de.nosswald.api.events.ParkourStartEvent
import de.nosswald.server.Instance
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ParkourStartListener : Listener {
    @EventHandler
    fun onParkourStart(event: ParkourStartEvent) {
        val parkour = event.parkour
        val player = event.player
        val data = Instance.plugin.getPlayerData(player.uniqueId).parkourData

        data.apply {
            this.enabled = true
            this.parkour = parkour
        }

        player.teleport(parkour.location)
        player.gameMode = GameMode.ADVENTURE

        player.sendTemplate(MessageTemplate("parkour.start", mapOf(
            "name" to parkour.name,
            "difficulty" to parkour.difficulty,
            "builder" to parkour.builder.joinToString(", ")
        )))
    }
}
