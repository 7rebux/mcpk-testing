package de.nosswald.server.listener

import de.nosswald.server.ServerState
import de.nosswald.server.commands.PracticeCommand
import de.nosswald.api.utils.TickCounter

import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * !!! IMPORTANT !!!
 * This requires a modified spigot.jar file to work properly
 */
object PlayerMovementListener : Listener {
    private val FINISH_MATERIAL = Material.EMERALD_BLOCK

    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val player = event.player
        val playerData = ServerState.getPlayerData(player.uniqueId)

        TickCounter.tick(event.player.uniqueId)

        if (playerData.practiceData.enabled) {
            if (event.hasMoved() && !playerData.practiceData.timer.started)
                playerData.practiceData.timer.start()

            if (player.location.block.getRelative(BlockFace.DOWN).type == FINISH_MATERIAL)
                PracticeCommand.disablePracticeMode(player)
        }
    }

    /**
     * @return whether the players location changed (ignoring mouse movements)
     */
    private fun PlayerMoveEvent.hasMoved() =
        this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z
}
