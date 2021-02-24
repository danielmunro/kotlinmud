package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.BerserkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.factory.hardForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Berserk : SkillAction, Customization {
    override val type = SkillType.BERSERK
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "berserk"
    override val points = 8
    override val command = Command.BERSERK
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(45)
    )
    override val difficulty = mapOf(
        hardForWarrior()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        Cost(CostType.MV_PERCENT, 20)
    )
    override val intent = Intent.NEUTRAL
    override val syntax = listOf(Syntax.COMMAND)
    override val argumentOrder = listOf(0)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect = BerserkAffect()
    override val helpText = "tbd"

    override fun invoke(actionContextService: ActionContextService): Response {
        val affect = createAffect(AffectType.BERSERK)
        actionContextService.getMob().affects.add(affect)
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("Your pulse speeds up as you are consumed by rage!")
                .toObservers("${actionContextService.getMob()}'s pulse speeds up as they are consumed by rage!")
                .build(),
            2
        )
    }
}
