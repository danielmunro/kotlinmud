package kotlinmud.mob.skill.impl.illusion

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.helper.Noun
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.item.dao.ItemDAO
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Invisibility : SpellAction {
    override val type: SkillType = SkillType.INVISIBILITY
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        Pair(SpecializationType.MAGE, 5),
        Pair(SpecializationType.CLERIC, 35)
    )
    override val difficulty = mapOf(
        easyForMage(),
        normalForCleric()
    )
    override val costs = listOf(
        Cost(CostType.MANA_AMOUNT, 80)
    )
    override val intent = Intent.PROTECTIVE
    override val affect = InvisibilityAffect()

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Noun>(Syntax.OPTIONAL_TARGET)
        val instance = affect.createInstance(actionContextService.getLevel())
        transaction {
            if (target is MobDAO) {
                instance.mob = target
            } else if (target is ItemDAO) {
                instance.item = target
            }
        }
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
