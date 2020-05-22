package kotlinmud.player.mapper

import kotlinmud.player.model.Player

fun mapPlayer(player: Player): String {
    return """${player.email}~
${player.name}~
${player.created}~
${player.mobs.joinToString(",")}~
"""
}
