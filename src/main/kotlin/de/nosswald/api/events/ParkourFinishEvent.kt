package de.nosswald.api.events

import de.nosswald.api.Parkour
import org.bukkit.entity.Player
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class ParkourFinishEvent(
    val parkour: Parkour,
    val player: Player,
    val ticks: Long
) : Event() {
    @Override
    override fun getHandlers(): HandlerList {
        return HANDLERS_LIST
    }

    companion object {
        private val HANDLERS_LIST = HandlerList()

        @Suppress("unused")
        @JvmStatic
        fun getHandlerList(): HandlerList {
            return HANDLERS_LIST
        }
    }
}
