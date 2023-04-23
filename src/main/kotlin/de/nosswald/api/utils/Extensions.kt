package de.nosswald.api.utils

import net.minecraft.server.v1_8_R3.AxisAlignedBB
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.util.NumberConversions

fun AxisAlignedBB.blocksTouchingBaseFace(
    getBlockAt: (x: Int, y: Int, z: Int) -> Block
): Set<Block> {
    val minX = NumberConversions.floor(this.a)
    val maxX = NumberConversions.ceil(this.d)
    val minZ = NumberConversions.floor(this.c)
    val maxZ = NumberConversions.ceil(this.f)
    val y = NumberConversions.ceil(this.b) - 1

    return buildSet {
        for (x in minX until maxX) {
            for (z in minZ until maxZ)
                add(getBlockAt(x, y, z))
        }
    }
}

fun Player.getBlocksStandingOn(): Set<Block> = (this as CraftPlayer).handle.boundingBox
    .blocksTouchingBaseFace { x, y, z -> this.world.getBlockAt(x, y, z) }
    .filter { it.type != Material.AIR }
    .sortedBy { this.location.distanceSquared(it.location) }
    .toSet()
