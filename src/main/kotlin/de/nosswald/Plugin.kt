package de.nosswald

import org.bukkit.plugin.java.JavaPlugin

// Sadly this can't be an object due to bukkit implementation
@Suppress("unused")
class Plugin : JavaPlugin() {
    override fun onEnable() {
        println("Working!!!11111elf!")
    }
}