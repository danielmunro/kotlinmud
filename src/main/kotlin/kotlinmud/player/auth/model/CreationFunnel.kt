package kotlinmud.player.auth.model

import kotlinmud.mob.builder.PlayerMobBuilder
import kotlinmud.mob.model.PlayerMob
import kotlinmud.mob.race.type.Race
import kotlinmud.mob.service.MobService
import kotlinmud.mob.skill.helper.createSkillList
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.Specialization
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Gender
import kotlinmud.player.dao.PlayerDAO
import kotlinmud.room.model.Room
import org.jetbrains.exposed.sql.transactions.transaction

class CreationFunnel(private val mobService: MobService, val name: String) {
    lateinit var mobName: String
    lateinit var mobRace: Race
    lateinit var specialization: Specialization
    lateinit var mobRoom: Room
    lateinit var gender: Gender
    lateinit var email: String
    private val skills = mutableListOf<SkillType>()
    private val allSkills = createSkillList()

    fun build(player: PlayerDAO): PlayerMob {
        transaction { player.email = email }
        return createMob(player).also { mob ->
            skills.forEach { type ->
                mob.skills[type] = 1
                mob.experienceToLevel += addExperienceForSkillType(type, mob.specialization!!.type)
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

    private fun createMob(player: PlayerDAO): PlayerMob {
        return PlayerMobBuilder(mobService).also {
            it.accountName = player.name
            it.name = mobName
            it.brief = "a $mobRace is here"
            it.description = "a nondescript ${mobRace.type.toString().toLowerCase()} is here"
            it.race = mobRace
            it.room = mobRoom
            it.maxItems = 100
            it.maxWeight = 1000
        }.build()
    }
}
