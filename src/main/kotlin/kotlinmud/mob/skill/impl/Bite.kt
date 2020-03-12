package kotlinmud.mob.skill.impl

import kotlin.random.Random
import kotlinmud.action.ActionContextService
import kotlinmud.action.mustBeAlert
import kotlinmud.io.Message
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.Intent
import kotlinmud.mob.Mob
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.fight.DamageType
import kotlinmud.mob.skill.*

class Bite : Skill {
    override val type: SkillType = SkillType.BITE
    override val levelObtained: Map<SpecializationType, Int> = mapOf()
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf()
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MV_AMOUNT, 20)
    )
    override val intent: Intent = Intent.OFFENSIVE
    override val syntax: List<Syntax> = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        val target: Mob = actionContextService.get(Syntax.TARGET_MOB)
        val limit = (request.mob.level / 10).coerceAtLeast(2)
        target.hp -= Random.nextInt(1, limit) +
                if (target.savesAgainst(DamageType.PIERCE)) 0 else Random.nextInt(1, limit)
        return actionContextService.createResponse(
            Message(
            "You bite $target.",
                "${request.mob} bites you.",
                "${request.mob} bites $target."))
    }
}
