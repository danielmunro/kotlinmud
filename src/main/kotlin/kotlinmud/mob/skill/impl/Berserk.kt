package kotlinmud.mob.skill.impl

import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.dao.AffectDAO
import kotlinmud.affect.impl.BerserkAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.MessageBuilder
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillAction
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType

class Berserk : SkillAction {
    override val type: SkillType = SkillType.BERSERK
    override val command: Command = Command.BERSERK
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.WARRIOR, 1),
        Pair(SpecializationType.THIEF, 45)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.WARRIOR, LearningDifficulty.HARD),
        Pair(SpecializationType.THIEF, LearningDifficulty.VERY_HARD)
    )
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.MV_PERCENT, 20)
    )
    override val intent: Intent = Intent.NEUTRAL
    override val syntax: List<Syntax> = listOf(Syntax.COMMAND)
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.INPUT
    override val affect = BerserkAffect()

    override fun invoke(actionContextService: ActionContextService): Response {
        actionContextService.getMob().affects.plus(
            AffectDAO.new {
                type = AffectType.BERSERK
                timeout = actionContextService.getLevel() / 8
            }
        )
        return actionContextService.createOkResponse(
            MessageBuilder()
                .toActionCreator("Your pulse speeds up as you are consumed by rage!")
                .toObservers("${actionContextService.getMob()}'s pulse speeds up as they are consumed by rage!")
                .build(),
            2
        )
    }
}
