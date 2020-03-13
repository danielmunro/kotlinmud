package kotlinmud.mob.skill.impl

import kotlinmud.action.ActionContextService
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.Intent
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.skill.*

class ShieldBlock : Skill {
    override val type: SkillType = SkillType.SHIELD_BLOCK
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.WARRIOR, 1),
        Pair(SpecializationType.THIEF, 15)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.NORMAL)
    )
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf()
    override val intent: Intent = Intent.PROTECTIVE
    override val syntax: List<Syntax> = listOf()

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        TODO()
    }
}
