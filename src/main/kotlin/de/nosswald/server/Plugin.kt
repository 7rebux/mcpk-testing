package de.nosswald.server

import de.nosswald.server.commands.PracticeCommand
import de.nosswald.server.listener.ConnectionListener
import de.nosswald.server.listener.PlayerMovementListener

import org.bukkit.command.CommandExecutor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

import java.util.*

// Sadly this can't be an object due to bukkit implementation
@Suppress("unused")
class Plugin : JavaPlugin() {
    override fun onEnable() {
        registerListeners(
            ConnectionListener,
            PlayerMovementListener,
        )

        registerCommands(mapOf(
            "practice" to PracticeCommand,
        ))
    }

    private fun registerListeners(vararg listeners: Listener) =
        listeners.forEach { server.pluginManager.registerEvents(it, this) }

    private fun registerCommands(commands: Map<String, CommandExecutor>) =
        commands.forEach { this.getCommand(it.key).executor = it.value }
}
