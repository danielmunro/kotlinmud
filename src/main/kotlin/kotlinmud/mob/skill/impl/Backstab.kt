package kotlinmud.mob.skill.impl

import kotlin.random.Random
import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.Affect
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Backstab : SkillAction {
    override val type = SkillType.BACK_STAB
    override val command = Command.BACK_STAB
    override val levelObtained = mapOf(
        thiefAt(1)
    )
    override val difficulty = mapOf(
        hardForThief()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(100)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)
    override val argumentOrder = listOf(0, 1)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect: Affect? = null

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        val limit = (actionContextService.getLevel() / 10).coerceAtLeast(2)
        transaction {
            target.hp -= Random.nextInt(1, limit) +
                    if (target.savesAgainst(DamageType.PIERCE)) 0 else Random.nextInt(1, limit)
        }
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
