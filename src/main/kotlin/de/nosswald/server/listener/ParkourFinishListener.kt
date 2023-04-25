package de.nosswald.server.listener

import de.nosswald.api.events.ParkourFinishEvent
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

object ParkourFinishListener : Listener {
    @EventHandler
    fun onParkourFinish(event: ParkourFinishEvent) {
        val (time, unit) = TickTimeFormatter.format(event.ticks)

        event.player.sendTemplate(MessageTemplate("parkour.finish", mapOf(
            "name" to event.parkour.name,
            "time" to time,
            "unit" to unit.toString().lowercase()
        )))
    }
}
