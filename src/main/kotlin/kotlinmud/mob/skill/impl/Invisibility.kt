package kotlinmud.mob.skill.impl

import kotlinmud.Noun
import kotlinmud.action.ActionContextService
import kotlinmud.action.mustBeAlert
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.io.Request
import kotlinmud.io.Response
import kotlinmud.io.Syntax
import kotlinmud.mob.Disposition
import kotlinmud.mob.Intent
import kotlinmud.mob.SpecializationType
import kotlinmud.mob.skill.Cost
import kotlinmud.mob.skill.CostType
import kotlinmud.mob.skill.LearningDifficulty
import kotlinmud.mob.skill.SkillInvokesOn
import kotlinmud.mob.skill.SkillType
import kotlinmud.mob.skill.SpellAction

class Invisibility : SpellAction {
    override val type: SkillType = SkillType.INVISIBILITY
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
        Cost(CostType.DELAY, 1),
        Cost(CostType.MANA_AMOUNT, 80)
    )
    override val intent: Intent = Intent.PROTECTIVE
    override val invokesOn: SkillInvokesOn = SkillInvokesOn.INPUT
    override val affect = InvisibilityAffect()
    override val syntax: List<Syntax> = listOf(Syntax.CAST, Syntax.SPELL, Syntax.OPTIONAL_TARGET)

    override fun invoke(actionContextService: ActionContextService, request: Request): Response {
        val target = actionContextService.get<Noun>(Syntax.OPTIONAL_TARGET)
        target.affects().add(affect.createInstance(request.mob.level))
        return actionContextService.createResponse(
            affect.messageFromInstantiation(request.mob, target)
        )
    }
}
