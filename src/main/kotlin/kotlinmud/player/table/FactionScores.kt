package kotlinmud.player.table

import org.jetbrains.exposed.dao.IntIdTable

object FactionScores : IntIdTable() {
    val faction = varchar("faction", 255)
    val score = integer("score")
    val mobCardId = reference("mobCardId", MobCards)
}