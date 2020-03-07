package kotlinmud.loader.model

import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attributes
import kotlinmud.mob.Disposition
import kotlinmud.mob.JobType
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.Race
import kotlinmud.mob.skill.SkillType

class MobModel(
    override val id: Int,
    val name: String,
    val brief: String,
    val description: String,
    val disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    val specialization: SpecializationType,
    val attributes: Attributes,
    val job: JobType,
    val gold: Int = 0,
    var skills: Map<SkillType, Int> = mapOf(),
    val affects: MutableList<AffectInstance> = mutableListOf()
) : Model
