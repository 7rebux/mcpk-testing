package de.nosswald.server.commands

import de.nosswald.server.ServerState
import de.nosswald.api.utils.TickTimeFormatter

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PracticeCommand : CommandExecutor {
    private fun enablePracticeMode(player: Player) {
        ServerState.getPlayerData(player.uniqueId).practiceData.apply {
            enabled = true
            location = player.location
        }

        player.gameMode = GameMode.ADVENTURE

        player.sendMessage("${ChatColor.GREEN}Enabled practice mode")
    }

    fun disablePracticeMode(player: Player) {
        val practiceData = ServerState.getPlayerData(player.uniqueId).practiceData
        val ticks = practiceData.timer.stop()

        player.teleport(practiceData.location)

        practiceData.apply {
            enabled = false
            location = null
        }

        player.gameMode = GameMode.CREATIVE

        player.sendMessage("${ChatColor.RED}Disabled practice mode after " +
                "${ChatColor.DARK_RED}${TickTimeFormatter(ticks).format("ss.SSS")} " +
                "${ChatColor.RED}seconds")
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
            return false
        }

        if (ServerState.getPlayerData(sender.uniqueId).practiceData.enabled)
            disablePracticeMode(sender)
        else
            enablePracticeMode(sender)

        return true
    }
}
