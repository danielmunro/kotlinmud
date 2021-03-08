package kotlinmud.quest.dao

import kotlinmud.mob.dao.MobDAO
import kotlinmud.quest.table.Quests
import kotlinmud.quest.type.QuestStatus
import kotlinmud.quest.type.QuestType
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass

class QuestDAO(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<QuestDAO>(Quests)

    var mob by MobDAO referencedOn Quests.mobId
    var status by Quests.status.transform(
        { it.toString() },
        { QuestStatus.valueOf(it) }
    )
    var quest by Quests.quest.transform(
        { it.toString() },
        { QuestType.valueOf(it) }
    )
}
