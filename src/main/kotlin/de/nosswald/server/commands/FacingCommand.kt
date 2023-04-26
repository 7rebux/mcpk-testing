package de.nosswald.server.commands

import de.nosswald.api.utils.facing
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object FacingCommand : ICommand {
    override val name = "facing"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        val yaw = args.getOrNull(0)?.toFloat()
        val pitch = args.getOrNull(1)?.toFloat()

        if (args.isEmpty()) return false
        if (sender !is Player) return sender.onlyPlayers()

        yaw?.let { sender.teleport(sender.location.apply { this.yaw = it }) }
        pitch?.let { sender.teleport(sender.location.apply { this.pitch = it }) }

        sender.sendTemplate(MessageTemplate("commands.facing.updated", mapOf(
            "direction" to sender.location.facing(),
            "yaw" to "%.3f".format(sender.location.yaw),
            "pitch" to "%.3f".format(sender.location.pitch),
        )))

        return true
    }
}
