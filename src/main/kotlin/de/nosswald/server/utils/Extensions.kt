package de.nosswald.server.utils

import de.nosswald.server.config.MessagesConfig
import net.minecraft.server.v1_8_R3.IChatBaseComponent
import net.minecraft.server.v1_8_R3.PacketPlayOutChat
import org.bukkit.command.CommandSender
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player

data class MessageTemplate(
    val path: String,
    val values: Map<String, Any> = mapOf()
) {
    fun get(): List<String> {
        val template: String? = MessagesConfig.config.getString(path)

        template?.let {
            if (it.contains("\n"))
                return it.split("\n").map(::buildMessage)
            else
                return listOf(buildMessage(it))
        } ?: error("Template $path could not be found!")
    }

    private fun buildMessage(template: String) = values.entries
        .fold(template) { acc, (key, value) -> acc.replace("{$key}", value.toString()) }
}

fun CommandSender.sendTemplate(template: MessageTemplate) {
    template.get().forEach { message ->
        this.sendMessage(message)
    }
}

fun Player.sendActionBar(text: String) {
    (this as CraftPlayer).handle.playerConnection.sendPacket(
        PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"$text\"}"), 2)
    )
}
