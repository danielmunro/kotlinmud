package kotlinmud.mob.skill.impl

import kotlinmud.Noun
import kotlinmud.action.helper.mustBeAlert
import kotlinmud.action.service.ActionContextService
import kotlinmud.action.type.Command
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.skill.SpellAction
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.LearningDifficulty
import kotlinmud.mob.skill.type.SkillInvokesOn
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.type.Disposition
import kotlinmud.mob.type.Intent
import kotlinmud.mob.type.SpecializationType

class Invisibility : SpellAction {
    override val type: SkillType = SkillType.INVISIBILITY
    override val command: Command = Command.INVISIBILITY
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.MAGE, 5),
        Pair(SpecializationType.CLERIC, 35)
    )
    override val difficulty: Map<SpecializationType, LearningDifficulty> = mapOf(
        Pair(SpecializationType.MAGE, LearningDifficulty.EASY),
        Pair(SpecializationType.CLERIC, LearningDifficulty.NORMAL)
    )
    override val dispositions: List<Disposition> = mustBeAlert()
    override val costs: List<Cost> = listOf(
        Cost(CostType.MANA_AMOUNT, 80)
    )
    override val intent: Intent = Intent.PROTECTIVE
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.INPUT
    override val affect = InvisibilityAffect()
    override val syntax: List<Syntax> = listOf(Syntax.CAST, Syntax.SPELL, Syntax.OPTIONAL_TARGET)

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Noun>(Syntax.OPTIONAL_TARGET)
        target.affects().add(affect.createInstance(actionContextService.getLevel()))
        return actionContextService.createOkResponse(
            affect.messageFromInstantiation(actionContextService.getMob(), target),
            1
        )
    }
}
