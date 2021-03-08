package kotlinmud.quest.table

import kotlinmud.mob.table.Mobs
import org.jetbrains.exposed.dao.IntIdTable

object Quests : IntIdTable() {
    val mobId = reference("mobId", Mobs)
    val status = varchar("status", 255)
    val quest = varchar("quest", 255)
}
