package kotlinmud.mob.quest.repository

import kotlinmud.mob.quest.dao.QuestDAO
import kotlinmud.mob.quest.table.Quests
import kotlinmud.mob.quest.type.QuestType
import kotlinmud.player.dao.MobCardDAO
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

fun findQuestByMobCardAndType(mobCard: MobCardDAO, type: QuestType): QuestDAO? {
    return transaction {
        Quests.select {
            Quests.mobCardId eq mobCard.id and (Quests.quest eq type.toString())
        }.firstOrNull()?.let { QuestDAO.wrapRow(it) }
    }
}
