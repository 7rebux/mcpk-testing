package de.nosswald.server

class Instance(javaPlugin: Plugin) {
    init {
        plugin = javaPlugin
    }

    companion object {
        lateinit var plugin: Plugin
    }
}
