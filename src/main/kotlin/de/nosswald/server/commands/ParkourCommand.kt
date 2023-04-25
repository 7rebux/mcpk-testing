package de.nosswald.server.commands

import de.nosswald.api.Parkour
import de.nosswald.server.Instance
import de.nosswald.server.ParkourManager
import de.nosswald.server.utils.MessageTemplate
import de.nosswald.server.utils.sendTemplate
import org.bukkit.GameMode
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
                        sender.sendTemplate(MessageTemplate("commands.errors.playersOnly"))
                        return true
                    }

                    parkour?.let {
                        val data = Instance.plugin.getPlayerData(sender.uniqueId).parkourData

                        data.apply {
                            this.enabled = true
                            this.parkour = parkour
                        }

                        sender.teleport(it.location)
                        sender.gameMode = GameMode.ADVENTURE

                        sender.sendTemplate(MessageTemplate("commands.parkour.start.success", mapOf(
                            "name" to parkour.name,
                            "difficulty" to parkour.difficulty,
                            "builder" to parkour.builder.joinToString(", ")
                        )))
                    } ?: sender.sendTemplate(MessageTemplate("commands.parkour.badId", mapOf("id" to id)))
                } ?: sender.sendTemplate(MessageTemplate("commands.parkour.noId"))
            }
            "leave" -> { // parkour leave
                if (sender !is Player) {
                    sender.sendTemplate(MessageTemplate("commands.errors.playersOnly"))
                    return true
                }

                val data = Instance.plugin.getPlayerData(sender.uniqueId).parkourData

                if (data.enabled) {
                    data.apply {
                        this.enabled = false
                        this.parkour = null
                    }
                    data.timer.stop()

                    sender.gameMode = GameMode.CREATIVE

                    sender.sendTemplate(MessageTemplate("commands.parkour.leave.success"))
                } else sender.sendTemplate(MessageTemplate("commands.parkour.leave.noParkour"))
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

                if (sender !is Player) {
                    sender.sendTemplate(MessageTemplate("commands.errors.playersOnly"))
                    return true
                }

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
