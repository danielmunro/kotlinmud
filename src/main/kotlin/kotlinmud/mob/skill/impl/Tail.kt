package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.type.Attribute
import kotlinmud.helper.math.dice
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.easyForWarrior
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Tail : SkillAction {
    override val affect: AffectInterface? = null
    override val type = SkillType.TAIL
    override val levelObtained = mapOf(
        warriorAt(1)
    )
    override val difficulty = mapOf(
        easyForWarrior()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(10)
    )
    override val intent = Intent.OFFENSIVE
    override val invokesOn = SkillInvokesOn.INPUT
    override val syntax = target()
    override val argumentOrder = listOf(1, 2)
    override val command = Command.TAIL

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Mob>(Syntax.TARGET_MOB)

        if (dice(1, 4) == 1) {
            target.affects.add(Affect(AffectType.STUNNED, 0, mapOf(Pair(Attribute.STR, -1))))
        }

        val damage = dice(2, 4)
        target.hp -= damage
        val mob = actionContextService.getMob()

        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("you slap $target with your tail!")
                .toTarget("$mob slaps you with its tail!")
                .toObservers("$mob slaps $target with its tail!")
                .build()
        )
    }
}
