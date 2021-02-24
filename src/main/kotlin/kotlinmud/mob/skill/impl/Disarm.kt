package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.type.Affect
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.item.type.Position
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Disarm : SkillAction, Customization {
    override val type = SkillType.DISARM
    override val command = Command.DISARM
    override val levelObtained = mapOf(
        warriorAt(5),
        thiefAt(30)
    )
    override val difficulty = mapOf(
        easyForWarrior(),
        normalForThief()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(20)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = target()
    override val argumentOrder = listOf(0, 1)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect: Affect? = null
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "disarm"
    override val points = 6
    override val helpText = "tbd"

    override fun invoke(actionContextService: ActionContextService): Response {
        val mob = actionContextService.getMob()
        val target = actionContextService.get<Mob>(Syntax.TARGET_MOB)
        return target.getEquippedByPosition(Position.WEAPON)?.let {
            transaction {
                it.room = actionContextService.getRoom()
                it.mobEquipped = null
                it.mobInventory = null
            }
            actionContextService.createOkResponse(
                MessageBuilder()
                    .toActionCreator("You disarm $target and send their weapon flying!")
                    .toTarget("$mob disarms you and sends your weapon flying!")
                    .toObservers("$mob disarms $target and sends their weapon flying!")
                    .build()
            )
        } ?: run {
            actionContextService.createErrorResponse(
                MessageBuilder()
                    .toActionCreator("They are not equipped with a weapon.")
                    .build()
            )
        }
    }
}
