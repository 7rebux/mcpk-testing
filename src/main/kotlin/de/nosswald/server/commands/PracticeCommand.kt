package de.nosswald.server.commands

import de.nosswald.api.events.PracticeDisableEvent
import de.nosswald.api.events.PracticeEnableEvent
import de.nosswald.server.Instance
import org.bukkit.Bukkit

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object PracticeCommand : ICommand {
    override val name = "practice"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (sender !is Player) return sender.onlyPlayers()

        val data = Instance.plugin.getPlayerData(sender.uniqueId).practiceData

        if (data.enabled)
            Bukkit.getPluginManager().callEvent(PracticeDisableEvent(sender, data.timer.stop()))
        else
            Bukkit.getPluginManager().callEvent(PracticeEnableEvent(sender))

        return true
    }
}
