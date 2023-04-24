package de.nosswald.server.listener

import de.nosswald.api.events.ParkourFinishEvent
import de.nosswald.api.utils.TickTimeFormatter
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ParkourFinishListener : Listener {
    @EventHandler
    fun onParkourFinish(event: ParkourFinishEvent) {
        event.player.teleport(event.parkour.location)
        event.player.sendMessage("Finished ${event.parkour.name} in ${TickTimeFormatter.format(event.ticks)}")
    }
}
