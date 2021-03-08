package kotlinmud.player.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object FactionScores : IntIdTable() {
    val faction = varchar("faction", 255)
    val score = integer("score")
    val mobId = reference("mobId", Mobs)
}
