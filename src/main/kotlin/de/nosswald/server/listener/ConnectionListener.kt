package de.nosswald.server.listener

import de.nosswald.api.PlayerData
import de.nosswald.server.Instance
import org.bukkit.ChatColor

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent

object ConnectionListener : Listener {
    @EventHandler
    fun onConnect(event: PlayerJoinEvent) {
        event.joinMessage = "${ChatColor.GREEN}>> ${ChatColor.GRAY}${event.player.name}"

        Instance.plugin.activePlayers.add(PlayerData(event.player.uniqueId))
    }

    @EventHandler
    fun onDisconnect(event: PlayerQuitEvent) {
        event.quitMessage = "${ChatColor.RED}<< ${ChatColor.GRAY}${event.player.name}"

        Instance.plugin.activePlayers.removeIf { it.playerId == event.player.uniqueId }
    }
}
