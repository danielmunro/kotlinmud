package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.easyForThief
import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Trip : SkillAction, Customization {
    override val type = SkillType.TRIP
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "trip"
    override val points = 4
    override val affect = StunnedAffect()
    override val syntax = target()
    override val command = Command.TRIP
    override val argumentOrder = listOf(0, 1)
    override val levelObtained = mapOf(
        thiefAt(15),
        warriorAt(5)
    )
    override val difficulty = mapOf(
        easyForWarrior(),
        easyForThief()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(20)
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.ATTACK_ROUND

    override fun invoke(actionContextService: ActionContextService): Response {
        val mob = actionContextService.getMob()
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        transaction {
            affect.createInstance(1).mob = target
        }
        transaction { target.hp -= dice(2, 4) }
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("you trip $target and they go down hard.")
                .toTarget("$mob trips you!")
                .toObservers("$mob trips $target.")
                .build()
        )
    }
}
