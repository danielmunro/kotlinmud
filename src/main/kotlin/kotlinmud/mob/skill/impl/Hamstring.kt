package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.normalForThief
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent

class Hamstring : SkillAction, Customization {
    override val type = SkillType.HAMSTRING
    override val command = Command.HAMSTRING
    override val levelObtained = mapOf(
        thiefAt(15)
    )
    override val difficulty = mapOf(
        normalForThief()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(100)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = target()
    override val argumentOrder = listOf(0, 1)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect: Affect = StunnedAffect()
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "hamstring"
    override val points = 6

    override fun invoke(actionContextService: ActionContextService): Response {
        createAffect(AffectType.STUNNED, Math.max(actionContextService.getLevel() / 10, 3))
        val mob = actionContextService.getMob()
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("You slash $target's hamstring, disabling them.")
                .toTarget("$mob slashes your hamstring, disabling your movement.")
                .toObservers("$mob slashes $target's hamstring, disabling them.")
                .build()
        )
    }
}
