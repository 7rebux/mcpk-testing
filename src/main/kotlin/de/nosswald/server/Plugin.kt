package de.nosswald.server

import de.nosswald.api.PlayerData
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.server.commands.FacingCommand
import de.nosswald.server.commands.PracticeCommand
import de.nosswald.server.listener.ConnectionListener
import de.nosswald.server.listener.PlayerMovementListener
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.Bukkit
import org.bukkit.command.CommandExecutor
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
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
            "facing" to FacingCommand,
        ))

        Timer().scheduleAtFixedRate(object: TimerTask() {
            override fun run() {
                Bukkit.getOnlinePlayers().forEach { player ->
                    if (player.uniqueId !in ServerState.activePlayers.map(PlayerData::playerId))
                        return

                    val playerData = ServerState.getPlayerData(player.uniqueId)

                    if (playerData.practiceData.enabled) {
                        val bar = "${TickTimeFormatter.format(playerData.practiceData.timer.ticks)}"

                        (player as CraftPlayer).handle.playerConnection
                            .sendPacket(PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"$bar\"}"), 2))
                    }
                }
            }
        }, 0, 1)
    }

    private fun registerListeners(vararg listeners: Listener) =
        listeners.forEach { server.pluginManager.registerEvents(it, this) }

    private fun registerCommands(commands: Map<String, CommandExecutor>) =
        commands.forEach { this.getCommand(it.key).executor = it.value }
}
