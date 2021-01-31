package kotlinmud.quest.dao

import kotlinmud.quest.table.Quests
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.player.table.MobCards
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class QuestDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestDAO>(Quests)

    var mobCard by MobCardDAO referencedOn MobCards.id
    var status by Quests.status.transform(
        { it.toString() },
        { QuestStatus.valueOf(it) }
    )
    var quest by Quests.quest.transform(
        { it.toString() },
        { QuestType.valueOf(it) }
    )
}