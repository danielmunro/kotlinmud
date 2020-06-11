package kotlinmud.player.mapper

import kotlinmud.fs.helper.str
import kotlinmud.player.model.Player

fun mapPlayer(player: Player): String {
    return """${str(player.email)}
${str(player.name)}
${str(player.created.toString())}
${str(player.mobs.joinToString(","))}
"""
}
