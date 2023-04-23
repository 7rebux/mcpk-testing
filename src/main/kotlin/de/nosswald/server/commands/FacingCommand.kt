package de.nosswald.server.commands

import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FacingCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val yaw = args.getOrNull(0)?.toFloat()
        val pitch = args.getOrNull(1)?.toFloat()

        if (sender !is Player) {
            sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
            return true
        }

        if (args.isEmpty()) return false

        yaw?.let { sender.teleport(sender.location.apply { this.yaw = it }) }
        pitch?.let { sender.teleport(sender.location.apply { this.pitch = it }) }

        sender.sendMessage("${ChatColor.GREEN}Updated facing!")

        return true
    }
}
