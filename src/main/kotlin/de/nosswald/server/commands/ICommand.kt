package de.nosswald.server.commands

import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

interface ICommand : CommandExecutor {
    val name: String

    fun CommandSender.onlyPlayers(): Boolean {
        this.sendTemplate(MessageTemplate("commands.errors.onlyPlayers"))
        return true
    }
}
