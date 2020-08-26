package kotlinmud.mob.skill.impl.malediction

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.BlindAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.offensiveSpell
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.easyForCleric
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Blind : SpellAction {
    override val affect = BlindAffect()
    override val type = SkillType.BLIND
    override val levelObtained = mapOf(
        mageAt(5)
    )
    override val difficulty = mapOf(
        easyForCleric()
    )
    override val costs = listOf(
        Cost(CostType.DELAY, 1),
        Cost(CostType.MANA_AMOUNT, 50)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = offensiveSpell()

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        createAffect(AffectType.BLIND, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
