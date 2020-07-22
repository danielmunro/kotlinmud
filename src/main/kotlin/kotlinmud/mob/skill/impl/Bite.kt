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
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType
import org.jetbrains.exposed.sql.transactions.transaction

class Bite : SkillAction {
    override val type: SkillType = SkillType.BITE
    override val command: Command = Command.BITE
    override val levelObtained: Map<SpecializationType, Int> = mapOf()
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf()
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.MV_AMOUNT, 20)
    )
    override val intent: Intent = Intent.OFFENSIVE
    override val syntax: List<Syntax> = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.INPUT
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
