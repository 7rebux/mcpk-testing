package de.nosswald.api

import org.bukkit.Location

data class Parkour(
    val id: Int,
    val name: String,
    val builder: List<String>,
    val difficulty: Difficulty,
    val location: Location,
    val resetHeight: Int,
) {
    enum class Difficulty { EASY, MEDIUM, HARD, ULTRA }
}
