package kotlinmud.loader.model

import kotlinmud.affect.AffectInstance
import kotlinmud.attributes.Attributes
import kotlinmud.item.Inventory
import kotlinmud.mob.Disposition
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.race.Race
import kotlinmud.mob.skill.SkillType

class MobModel(
    val id: Int,
    val name: String,
    val description: String,
    val disposition: Disposition,
    var hp: Int,
    var mana: Int,
    var mv: Int,
    var level: Int,
    val race: Race,
    val specialization: SpecializationType,
    val attributes: Attributes,
    var skills: Map<SkillType, Int> = mapOf(),
    val affects: MutableList<AffectInstance> = mutableListOf())
