package kotlinmud.player.auth.model

import kotlinmud.attributes.constant.startingHp
import kotlinmud.attributes.constant.startingMana
import kotlinmud.attributes.constant.startingMv
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.helper.MobBuilder
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.skill.dao.SkillDAO
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.player.dao.MobCardDAO
import kotlinmud.room.dao.RoomDAO
import org.jetbrains.exposed.sql.transactions.transaction

class CreationFunnel(val email: String) {
    lateinit var mobName: String
    lateinit var mobRace: Race
    lateinit var specialization: Specialization
    lateinit var mobRoom: RoomDAO
    private val skills = mutableListOf<SkillType>()
    private val allSkills = createSkillList()

    fun build(): MobDAO {
        return createMob().also { mob ->
            skills.forEach { type ->
                SkillDAO.new {
                    this.mob = mob
                    this.type = type
                    level = 1
                }
                val mobCard = mob.mobCard!!
                mobCard.experiencePerLevel += addExperienceForSkillType(type, mob.specialization!!)
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

    private fun createMob(): MobDAO {
        return transaction {
            MobBuilder()
                .name(mobName)
                .brief("a $mobRace is here")
                .description("a nondescript $mobRace is here")
                .race(mobRace)
                .room(mobRoom)
                .build()
            .also {
                it.mobCard = MobCardDAO.new {
                    experiencePerLevel = 1000
                    experience = 1000
                    trains = 5
                    practices = 5
                    mob = it
                    respawnRoom = mobRoom
                }
            }
        }
    }
}
