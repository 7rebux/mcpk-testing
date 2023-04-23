package de.nosswald.server.commands

import de.nosswald.server.ServerState
import de.nosswald.api.utils.TickTimeFormatter

import org.bukkit.ChatColor
import org.bukkit.GameMode
import org.bukkit.Location
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

        player.sendMessage("${ChatColor.GREEN}Enabled practice mode at ${ChatColor.DARK_GREEN}${player.location.format()}")
    }

    private fun disablePracticeMode(player: Player) {
        val practiceData = ServerState.getPlayerData(player.uniqueId).practiceData
        val ticks = practiceData.timer.stop()

        player.teleport(practiceData.location)

        practiceData.apply {
            enabled = false
            location = null
        }

        player.gameMode = GameMode.CREATIVE

        player.sendMessage("${ChatColor.RED}Disabled practice mode after ${ChatColor.DARK_RED}${TickTimeFormatter.format(ticks)}")
    }

    private fun Location.format() = "(X=${"%.3f".format(this.x)}, Y=${"%.3f".format(this.y)}, Z=${"%.3f".format(this.z)}, F=(${"%.3f".format(this.yaw)}, ${"%.3f".format(this.pitch)}))"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
            return true
        }

        if (ServerState.getPlayerData(sender.uniqueId).practiceData.enabled)
            disablePracticeMode(sender)
        else
            enablePracticeMode(sender)

        return true
    }
}
