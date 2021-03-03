package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.factory.target
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.hardForThief
import kotlinmud.mob.skill.factory.mvCostOf
import kotlinmud.mob.skill.factory.normalForWarrior
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.veryHardForCleric
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Intent
import kotlin.random.Random

class Bash : SkillAction, Customization {
    override val type = SkillType.BASH
    override val command = Command.BASH
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "bash"
    override val points = 6
    override val levelObtained = mapOf(
        warriorAt(1),
        thiefAt(30),
        clericAt(45)
    )
    override val difficulty = mapOf(
        normalForWarrior(),
        hardForThief(),
        veryHardForCleric()
    )
    override val dispositions = mustBeAlert()
    override val costs = listOf(
        mvCostOf(20)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = target()
    override val argumentOrder = listOf(1, 2)
    override val invokesOn = SkillInvokesOn.INPUT
    override val affect = StunnedAffect()
    override val helpText = "tbd"

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Mob>(Syntax.TARGET_MOB)
        val limit = (actionContextService.getLevel() / 10).coerceAtLeast(2)
        val modifier = Random.nextInt(1, limit) +
            if (target.savesAgainst(DamageType.POUND)) 0 else Random.nextInt(1, limit)
        target.hp -= modifier
        target.affects.add(
            Affect(
                    AffectType.STUNNED,
                    modifier / 5,
                    mapOf(Pair(Attribute.INT, -1))
            )
        )
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("you slam into $target and send them flying!")
                .toTarget("${actionContextService.getMob()} slams into you and sends you flying!")
                .toObservers("${actionContextService.getMob()} slams into $target and sends them flying!")
                .build(),
            1
        )
    }
}
