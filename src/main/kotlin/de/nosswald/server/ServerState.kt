package de.nosswald.server

import de.nosswald.api.PlayerData

import java.util.UUID

object ServerState {
    val activePlayers = mutableListOf<PlayerData>()

    fun getPlayerData(uuid: UUID) =
        activePlayers.find { it.playerId == uuid } ?: error("Player data not found for $uuid")
}
