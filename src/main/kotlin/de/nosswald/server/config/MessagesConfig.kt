package de.nosswald.server.config

import de.nosswald.server.Instance
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import java.io.File
import java.io.InputStreamReader

object MessagesConfig {
    private const val FILE_NAME = "messages.yml"
    private val file = File(Instance.plugin.dataFolder, FILE_NAME)
    val config: FileConfiguration

    init {
        if (!Instance.plugin.dataFolder.exists()) Instance.plugin.dataFolder.mkdir()
        if (!file.exists()) file.createNewFile()
        config = YamlConfiguration.loadConfiguration(file)

        with(InputStreamReader(this.javaClass.getResourceAsStream("/$FILE_NAME")!!, "UTF8")) {
            config.defaults = YamlConfiguration.loadConfiguration(this)
        }

        config.options().copyDefaults(true)
        config.save(file)
    }
}
