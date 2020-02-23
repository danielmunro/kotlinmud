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

class Bash : Skill {
    override val type: SkillType = SkillType.BASH
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.WARRIOR, 1)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL)
    )
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MV_AMOUNT, 20)
    )
    override val intent: Intent = Intent.OFFENSIVE
    override val syntax: List<Syntax> = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        TODO("not implemented") // To change body of created functions use File | Settings | File Templates.
    }
}
