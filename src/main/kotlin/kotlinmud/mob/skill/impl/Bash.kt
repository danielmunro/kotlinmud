package kotlinmud.mob.skill.impl

import kotlin.random.Random
import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.StunnedAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.fight.type.DamageType
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.thiefAt
import kotlinmud.mob.skill.factory.warriorAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.CreationGroupType
import kotlinmud.mob.skill.type.Customization
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent

class Bash : SkillAction, Customization {
    override val type: SkillType = SkillType.BASH
    override val command: Command = Command.BASH
    override val creationGroupType = CreationGroupType.SKILL
    override val name = "bash"
    override val points = 6
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        warriorAt(1),
        thiefAt(30),
        clericAt(45)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.NORMAL),
        Pair(SpecializationType.THIEF, LearningDifficulty.HARD),
        Pair(SpecializationType.CLERIC, LearningDifficulty.VERY_HARD)
    )
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.MV_AMOUNT, 20)
    )
    override val intent: Intent = Intent.OFFENSIVE
    override val syntax: List<Syntax> = listOf(Syntax.COMMAND, Syntax.TARGET_MOB)
    override val argumentOrder: List<Int> = listOf(1, 2)
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.INPUT
    override val affect = StunnedAffect()

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        val limit = (actionContextService.getLevel() / 10).coerceAtLeast(2)
        val modifier = Random.nextInt(1, limit) +
                if (target.savesAgainst(DamageType.POUND)) 0 else Random.nextInt(1, limit)
        target.hp -= modifier
        target.affects.plus(
            createAffect(AffectType.STUNNED, modifier / 5, AttributesDAO.new { intelligence = -1 })
        )
        return actionContextService.createOkResponse(MessageBuilder()
            .toActionCreator("you slam into $target and send them flying!")
            .toTarget("${actionContextService.getMob()} slams into you and sends you flying!")
            .toObservers("${actionContextService.getMob()} slams into $target and sends them flying!")
            .build(),
            1
        )
    }
}
