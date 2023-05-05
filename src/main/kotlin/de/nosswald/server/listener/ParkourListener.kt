package de.nosswald.server.listener

import de.nosswald.api.events.ParkourFinishEvent
import de.nosswald.api.events.ParkourLeaveEvent
import de.nosswald.api.events.ParkourStartEvent
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.api.utils.getBlocksStandingOn
import de.nosswald.api.utils.hasMoved
import de.nosswald.server.Instance
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendActionBar
import de.nosswald.server.utils.sendTemplate
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object ParkourListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val data = Instance.plugin.getPlayerData(player.uniqueId).parkourData

        if (!data.enabled) return

        // finish
        if (player.getBlocksStandingOn().map(Block::getType).any { it == Material.EMERALD_BLOCK })
            Bukkit.getServer().pluginManager.callEvent(ParkourFinishEvent(data.parkour!!, player, data.timer.stop()))

        // reset height
        if (player.location.y <= data.parkour!!.resetHeight)
            player.teleport(data.parkour!!.location)

        // timer
        if (event.hasMoved() && !data.timer.started) data.timer.start()
        if (data.timer.started) data.timer.tick()

        TickTimeFormatter.format(data.timer.ticks).let { (time, unit) ->
            event.player.sendActionBar("${ChatColor.YELLOW}$time ${unit.toString().lowercase()}")
        }
    }

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

    @EventHandler
    fun onParkourFinish(event: ParkourFinishEvent) {
        val (time, unit) = TickTimeFormatter.format(event.ticks)

        event.player.sendTemplate(
            MessageTemplate("parkour.finish", mapOf(
            "name" to event.parkour.name,
            "time" to time,
            "unit" to unit.toString().lowercase()
        )))
    }

    @EventHandler
    fun onParkourLeave(event: ParkourLeaveEvent) {
        val player = event.player
        val data = Instance.plugin.getPlayerData(player.uniqueId).parkourData

        data.apply {
            this.enabled = false
            this.parkour = null
        }
        data.timer.stop()

        player.gameMode = GameMode.CREATIVE

        player.sendTemplate(MessageTemplate("parkour.leave"))
    }
}
