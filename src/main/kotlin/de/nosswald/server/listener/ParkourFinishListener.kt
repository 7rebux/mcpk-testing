package de.nosswald.server.listener

import de.nosswald.api.events.ParkourFinishEvent
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.server.ServerState
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ParkourFinishListener : Listener {
    @EventHandler
    fun onParkourFinish(event: ParkourFinishEvent) {
        val player = event.player
        val data = ServerState.getPlayerData(player.uniqueId).parkourData
        val parkour = data.parkour!!

        player.teleport(parkour.location)
        player.sendMessage("Finished ${parkour.name} in ${TickTimeFormatter.format(event.ticks)}")
    }
}
