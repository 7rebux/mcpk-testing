package de.nosswald.server.commands

import de.nosswald.api.Parkour
import de.nosswald.api.events.ParkourLeaveEvent
import de.nosswald.api.events.ParkourStartEvent
import de.nosswald.server.Instance
import de.nosswald.server.ParkourManager
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

object ParkourCommand : ICommand {
    override val name = "parkour"

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

                    if (sender !is Player) return sender.onlyPlayers()

                    parkour?.let {
                        Bukkit.getServer().pluginManager.callEvent(ParkourStartEvent(it, sender))
                    } ?: sender.sendTemplate(MessageTemplate("commands.parkour.badId", mapOf("id" to id)))
                } ?: sender.sendTemplate(MessageTemplate("commands.parkour.noId"))
            }
            "leave" -> { // parkour leave
                if (sender !is Player) return sender.onlyPlayers()

                if (Instance.plugin.getPlayerData(sender.uniqueId).parkourData.enabled)
                    Bukkit.getServer().pluginManager.callEvent(ParkourLeaveEvent(sender))
                else
                    sender.sendTemplate(MessageTemplate("commands.parkour.noParkour"))
            }
            "list" -> { // parkour list
                if (ParkourManager.parkours.isEmpty())
                    sender.sendTemplate(MessageTemplate("commands.parkour.list.empty"))

                ParkourManager.parkours.forEach {
                    sender.sendTemplate(MessageTemplate("commands.parkour.list.entry", mapOf(
                        "id" to it.id,
                        "name" to it.name,
                        "difficulty" to it.difficulty,
                        "builder" to it.builder.joinToString(", ")
                    )))
                }
            }
            "add" -> { // parkour add <name> <builder> <difficulty> <resetHeight>
                val id = ParkourManager.parkours.maxOfOrNull(Parkour::id)?.plus(1) ?: 0
                val name = args.getOrNull(1)
                val builder = args.getOrNull(2)?.split(",")
                val difficulty = args.getOrNull(3)?.let(Parkour.Difficulty::valueOf)
                val resetHeight = args.getOrNull(4)?.let(String::toInt)

                if (sender !is Player) return sender.onlyPlayers()

                val parkour = Parkour(
                    id,
                    name ?: "Unnamed",
                    builder ?: listOf("Unknown"),
                    difficulty ?: Parkour.Difficulty.MEDIUM,
                    sender.location,
                    resetHeight ?: (sender.location.blockY - 10)
                )
                ParkourManager.parkours.add(parkour)

                sender.sendTemplate(MessageTemplate("commands.parkour.add.success", mapOf(
                    "name" to parkour.name,
                    "id" to parkour.id
                )))
            }
            "remove" -> { // parkour remove <id>
                args.getOrNull(1)?.let { id ->
                    val parkour = ParkourManager.parkours.firstOrNull { it.id == id.toInt() }

                    parkour?.let {
                        ParkourManager.parkours.remove(it)
                        sender.sendTemplate(MessageTemplate("commands.parkour.remove.success", mapOf(
                            "name" to it.name,
                            "id" to it.id
                        )))
                    } ?: sender.sendTemplate(MessageTemplate("commands.parkour.badId"))
                } ?: sender.sendTemplate(MessageTemplate("commands.parkour.noId"))
            }
            else -> return false
        }

        return true
    }
}
