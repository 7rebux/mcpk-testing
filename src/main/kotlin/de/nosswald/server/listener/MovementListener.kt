package de.nosswald.server.listener

import de.nosswald.api.PlayerData
import de.nosswald.api.events.ParkourFinishEvent
import de.nosswald.api.utils.getBlocksStandingOn
import de.nosswald.server.ServerState
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

/**
 * !!! IMPORTANT !!!
 * This requires a modified spigot.jar file to work properly
 */
object MovementListener : Listener {
    @EventHandler
    fun onPlayerMove(event: PlayerMoveEvent) {
        val playerData = ServerState.getPlayerData(event.player.uniqueId)

        if (playerData.parkourData.enabled) handleParkourMode(event, playerData.parkourData)
        if (playerData.practiceData.enabled) handlePracticeMode(event, playerData.practiceData)
    }

    private fun handlePracticeMode(
        event: PlayerMoveEvent,
        data: PlayerData.PracticeData
    ) {
        if (event.hasMoved() && !data.timer.started) data.timer.start()
        if (data.timer.started) data.timer.tick()
    }

    private fun handleParkourMode(
        event: PlayerMoveEvent,
        data: PlayerData.ParkourData
    ) {
        if (event.player.getBlocksStandingOn().map(Block::getType).any { it == Material.EMERALD_BLOCK })
            Bukkit.getServer().pluginManager.callEvent(ParkourFinishEvent(event.player, data.timer.stop()))

        if (event.hasMoved() && !data.timer.started) data.timer.start()
        if (data.timer.started) data.timer.tick()
    }

    /**
     * @return whether the players location changed (ignoring mouse movements)
     */
    private fun PlayerMoveEvent.hasMoved() =
        this.from.x != this.to.x || this.from.y != this.to.y || this.from.z != this.to.z
}
