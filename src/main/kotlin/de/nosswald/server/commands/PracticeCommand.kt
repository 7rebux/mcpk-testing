package de.nosswald.server.commands

import de.nosswald.server.ServerState
import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.api.utils.facing

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
        player.sendMessage("  ${ChatColor.DARK_GREEN}➥ X: ${ChatColor.WHITE}${"%.3f".format(player.location.x)}")
        player.sendMessage("  ${ChatColor.DARK_GREEN}➥ Y: ${ChatColor.WHITE}${"%.3f".format(player.location.y)}")
        player.sendMessage("  ${ChatColor.DARK_GREEN}➥ Z: ${ChatColor.WHITE}${"%.3f".format(player.location.z)}")
        player.sendMessage("  ${ChatColor.DARK_GREEN}➥ Direction: ${ChatColor.WHITE}${player.location.facing()}")
        player.sendMessage("  ${ChatColor.DARK_GREEN}➥ Facing: ${ChatColor.WHITE}${"%.3f".format(player.location.yaw)} / ${"%.3f".format(player.location.pitch)}")
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
