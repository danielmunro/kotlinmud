package kotlinmud.quest.repository

import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.repository.findMobByCanonicalId
import kotlinmud.mob.type.CanonicalId
import kotlinmud.mob.type.JobType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.quest.dao.QuestDAO
import kotlinmud.quest.table.Quests
import kotlinmud.quest.type.QuestType
import kotlinmud.room.repository.findRoomById
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

fun findOrCreatePraetorianRecruiter1(): MobDAO {
    return findMobByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_1)
        ?: transaction {
            MobDAO.new {
                name = "Recruiter Bartok"
                room = findRoomById(1)
                job = JobType.QUEST
                canonicalId = CanonicalId.PRAETORIAN_RECRUITER_1
            }
        }
}

fun findOrCreatePraetorianRecruiter2(): MobDAO {
    return findMobByCanonicalId(CanonicalId.PRAETORIAN_RECRUITER_2)
        ?: transaction {
            MobDAO.new {
                name = "Recruiter Bartok"
                room = findRoomById(1)
                job = JobType.QUEST
                canonicalId = CanonicalId.PRAETORIAN_RECRUITER_2
            }
        }
}
