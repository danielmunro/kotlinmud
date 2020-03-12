package kotlinmud.mob.skill.impl

import kotlin.random.Random
import kotlinmud.action.ActionContextService
import kotlinmud.action.mustBeAlert
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attributes
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

class Bash : Skill {
    override val type: SkillType = SkillType.BASH
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.WARRIOR, 1),
        Pair(SpecializationType.THIEF, 30),
        Pair(SpecializationType.CLERIC, 45)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.HARD),
        Pair(SpecializationType.CLERIC, LearningDifficulty.VERY_HARD)
    )
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
        val modifier = limit + if (target.savesAgainst(DamageType.POUND)) 0 else Random.nextInt(1, limit)
        target.hp -= Random.nextInt(1, modifier)
        target.affects.add(AffectInstance(
            AffectType.STUNNED, modifier / 5, Attributes(0, 0, 0, 0, -1)))
        return actionContextService.createResponse(Message(
            "you slam into $target and send them flying!",
            "${request.mob} slams into you and sends you flying!",
            "${request.mob} slams into $target and sends them flying!"
        ))
    }
}
