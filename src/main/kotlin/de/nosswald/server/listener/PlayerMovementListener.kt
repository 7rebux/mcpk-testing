package de.nosswald.server.listener

import de.nosswald.api.PlayerData
import de.nosswald.server.ServerState

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * !!! IMPORTANT !!!
 * This requires a modified spigot.jar file to work properly
 */
object PlayerMovementListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val playerData = ServerState.getPlayerData(event.player.uniqueId)

        if (playerData.practiceData.enabled) handlePracticeMode(event, playerData.practiceData)
    }

    private fun handlePracticeMode(
        event: PlayerMoveEvent,
        data: PlayerData.PracticeData
    ) {
        if (event.hasMoved() && !data.timer.started) data.timer.start()
        if (data.timer.started) data.timer.tick()
    }

    /**
     * @return whether the players location changed (ignoring mouse movements)
     */
    private fun PlayerMoveEvent.hasMoved() =
        this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z
}
