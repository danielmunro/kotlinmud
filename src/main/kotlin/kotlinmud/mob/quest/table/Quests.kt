package kotlinmud.mob.quest.table

import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.dao.IntIdTable

object Quests : IntIdTable() {
    val mobCardId = reference("mobCardId", MobCards)
    val status = varchar("status", 255)
    val quest = varchar("quest", 255)
}