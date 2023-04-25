package de.nosswald.server.commands

import de.nosswald.api.utils.TickTimeFormatter
import de.nosswald.api.utils.facing
import de.nosswald.server.Instance
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate

import org.bukkit.GameMode
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PracticeCommand : CommandExecutor {
    private fun enablePracticeMode(player: Player) {
        Instance.plugin.getPlayerData(player.uniqueId).practiceData.apply {
            enabled = true
            location = player.location
        }

        player.gameMode = GameMode.ADVENTURE

        player.sendTemplate(MessageTemplate("commands.practice.enabled", mapOf(
            "x" to "%.3f".format(player.location.x),
            "y" to "%.3f".format(player.location.y),
            "z" to "%.3f".format(player.location.z),
            "direction" to player.location.facing(),
            "yaw" to "%.3f".format(player.location.yaw),
            "pitch" to "%.3f".format(player.location.pitch)
        )))
    }

    private fun disablePracticeMode(player: Player) {
        val practiceData = Instance.plugin.getPlayerData(player.uniqueId).practiceData
        val (time, unit) = TickTimeFormatter.format(practiceData.timer.stop())

        player.teleport(practiceData.location)

        practiceData.apply {
            enabled = false
            location = null
        }

        player.gameMode = GameMode.CREATIVE

        player.sendTemplate(MessageTemplate("commands.practice.disabled", mapOf(
            "time" to time,
            "unit" to unit.toString().lowercase()
        )))
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (sender !is Player) {
            sender.sendTemplate(MessageTemplate("commands.errors.playersOnly"))
            return true
        }

        if (Instance.plugin.getPlayerData(sender.uniqueId).practiceData.enabled)
            disablePracticeMode(sender)
        else
            enablePracticeMode(sender)

        return true
    }
}
