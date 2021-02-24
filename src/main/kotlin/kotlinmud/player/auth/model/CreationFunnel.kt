package kotlinmud.player.auth.model

import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.model.Mob
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class CreationFunnel(private val mobService: MobService, val email: String) {
    lateinit var mobName: String
    lateinit var mobRace: Race
    lateinit var specialization: Specialization
    lateinit var mobRoom: RoomDAO
    private val skills = mutableListOf<SkillType>()
    private val allSkills = createSkillList()

    fun build(): Mob {
        return createMob().also { mob ->
            skills.forEach { type ->
                val skill = SkillDAO.new {
                    this.type = type
                    level = 1
                }
                mob.skills.add(skill)
                val mobCard = mob.mobCard!!
                mobCard.experiencePerLevel += addExperienceForSkillType(type, mob.specialization!!.type)
            }
        }
    }

    private fun addExperienceForSkillType(skillType: SkillType, specializationType: SpecializationType): Int {
        return allSkills.find { it.type == skillType }?.let {
            when (it.difficulty[specializationType]) {
                LearningDifficulty.EASY -> 200
                LearningDifficulty.NORMAL -> 400
                LearningDifficulty.HARD -> 800
                LearningDifficulty.VERY_HARD -> 1600
                else -> 200
            }
        } ?: 0
    }

    private fun createMob(): Mob {
        val card = transaction {
            MobCardDAO.new {
                experiencePerLevel = 1000
                experience = 1000
                trains = 5
                practices = 5
                respawnRoom = mobRoom
            }
        }
        return MobBuilder(mobService)
            .name(mobName)
            .brief("a $mobRace is here")
            .description("a nondescript $mobRace is here")
            .race(mobRace)
            .room(mobRoom)
            .card(card)
            .build()
    }
}
