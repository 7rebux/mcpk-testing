package de.nosswald.server.commands

import de.nosswald.api.Parkour
import de.nosswald.server.ParkourManager
import de.nosswald.server.ServerState
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ParkourCommand : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<String>
    ): Boolean {
        if (args.isEmpty()) return false

        when (args[0]) {
            "start" -> { // parkour start <id>
                args.getOrNull(1)?.let { id ->
                    val parkour = ParkourManager.parkours.firstOrNull { it.id == id.toInt() }

                    if (sender !is Player) {
                        sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
                        return true
                    }

                    parkour?.let {
                        val data = ServerState.getPlayerData(sender.uniqueId).parkourData

                        data.apply {
                            this.enabled = true
                            this.parkour = parkour
                        }

                        sender.teleport(it.location)
                        sender.sendMessage("Started parkour")
                    } ?: sender.sendMessage("Parkour ${id} not found!")
                } ?: sender.sendMessage("Missing id")
            }
            "leave" -> { // parkour leave
                if (sender !is Player) {
                    sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
                    return true
                }

                val data = ServerState.getPlayerData(sender.uniqueId).parkourData

                if (data.enabled) {
                    data.apply {
                        this.enabled = false
                        this.parkour = null
                    }
                    data.timer.stop()

                    sender.sendMessage("Left parkour")
                } else sender.sendMessage("Not in a parkour")
            }
            "list" -> { // parkour list
                if (ParkourManager.parkours.isEmpty())
                    sender.sendMessage("No existing parkours")

                ParkourManager.parkours
                    .map(Parkour::toString)
                    .forEach(sender::sendMessage)
            }
            "add" -> { // parkour add <name> <builder> <difficulty> <resetHeight>
                val id = ParkourManager.parkours.maxOfOrNull(Parkour::id)?.plus(1) ?: 0
                val name = args.getOrNull(1)
                val builder = args.getOrNull(2)?.split(",")
                val difficulty = args.getOrNull(3)?.let(Parkour.Difficulty::valueOf)
                val resetHeight = args.getOrNull(4)?.let(String::toInt)

                if (sender !is Player) {
                    sender.sendMessage("${ChatColor.RED}This command can only be run as a player")
                    return true
                }

                ParkourManager.parkours.add(Parkour(
                    id,
                    name ?: "Unnamed",
                    builder ?: listOf("Unknown"),
                    difficulty ?: Parkour.Difficulty.MEDIUM,
                    sender.location,
                    resetHeight ?: (sender.location.blockY - 10)
                ))
                sender.sendMessage("Added parkour")
            }
            "remove" -> { // parkour remove <id>
                args.getOrNull(1)?.let { id ->
                    val success = ParkourManager.parkours.removeIf { it.id == id.toInt() }
                    sender.sendMessage(if (success) "Removed $id" else "Not found")
                } ?: sender.sendMessage("Missing id")
            }
            else -> return false
        }

        return true
    }
}
