package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.AffectInterface
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent
import kotlin.random.Random

class Bite : SkillAction {
    override val type = SkillType.BITE
    override val command = Command.BITE
    override val levelObtained: Map<SpecializationType, Int> = mapOf()
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf()
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(20)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)
    override val argumentOrder = listOf(0, 1)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect: AffectInterface? = null

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Mob>(Syntax.TARGET_MOB)
        val limit = (actionContextService.getLevel() / 10).coerceAtLeast(2)
        target.hp -= Random.nextInt(1, limit) +
            if (target.savesAgainst(DamageType.PIERCE)) 0 else Random.nextInt(1, limit)
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("You bite $target.")
                .toTarget("${actionContextService.getMob()} bites you.")
                .toObservers("${actionContextService.getMob()} bites $target.")
                .build(),
            1
        )
    }
}
