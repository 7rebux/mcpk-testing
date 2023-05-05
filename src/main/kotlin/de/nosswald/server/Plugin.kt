package de.nosswald.server

import de.nosswald.api.PlayerData
import de.nosswald.server.commands.*
import de.nosswald.server.listener.*
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

// Sadly this can't be an object due to bukkit implementation
// Important: This plugin requires a modified spigot.jar file to work properly
@Suppress("unused")
class Plugin : JavaPlugin() {
    private val instance = Instance(this)
    val activePlayers = mutableListOf<PlayerData>()

    fun getPlayerData(uuid: UUID) =
        activePlayers.find { it.playerId == uuid } ?: error("Player data not found for $uuid")

    override fun onEnable() {
        registerListeners(
            ConnectionListener,
            ParkourListener,
            PracticeListener,
        )

        registerCommands(
            PracticeCommand,
            FacingCommand,
            ParkourCommand,
        )
    }

    private fun registerListeners(vararg listeners: Listener) =
        listeners.forEach { server.pluginManager.registerEvents(it, this) }

    private fun registerCommands(vararg commands: ICommand) =
        commands.forEach { this.getCommand(it.name).executor = it }
}
