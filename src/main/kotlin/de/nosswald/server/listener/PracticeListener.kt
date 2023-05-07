package de.nosswald.server.listener

import de.nosswald.api.events.PracticeDisableEvent
import de.nosswald.api.events.PracticeEnableEvent
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.api.utils.facing
import de.nosswald.api.utils.hasMoved
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.getData
import de.nosswald.server.utils.sendActionBar
import de.nosswald.server.utils.sendTemplate
import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

object PracticeListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val data = player.getData().practiceData

        if (!data.enabled) return

        if (event.hasMoved() && !data.timer.started) data.timer.start()
        if (data.timer.started) data.timer.tick()

        // only show timer if not in parkour mode
        if (!player.getData().parkourData.enabled) {
            TickTimeFormatter.format(data.timer.ticks).let { (time, unit) ->
                event.player.sendActionBar("${ChatColor.YELLOW}$time ${unit.toString().lowercase()}")
            }
        }
    }

    @EventHandler
    fun onPracticeEnable(event: PracticeEnableEvent) {
        val player = event.player
        val data = player.getData().practiceData

        data.apply {
            this.enabled = true
            this.location = player.location
        }

        player.gameMode = GameMode.ADVENTURE

        player.sendTemplate(
            MessageTemplate("practice.enabled", mapOf(
            "x" to "%.3f".format(player.location.x),
            "y" to "%.3f".format(player.location.y),
            "z" to "%.3f".format(player.location.z),
            "direction" to player.location.facing(),
            "yaw" to "%.3f".format(player.location.yaw),
            "pitch" to "%.3f".format(player.location.pitch)
        )))
    }

    @EventHandler
    fun onPracticeDisable(event: PracticeDisableEvent) {
        val (time, unit) = TickTimeFormatter.format(event.ticks)
        val player = event.player
        val data = player.getData().practiceData

        player.teleport(data.location)

        data.apply {
            this.enabled = false
            this.location = null
        }

        player.gameMode = GameMode.CREATIVE

        player.sendTemplate(MessageTemplate("practice.disabled", mapOf(
            "time" to time,
            "unit" to unit.toString().lowercase()
        )))
    }
}
