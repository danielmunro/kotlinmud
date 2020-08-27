package kotlinmud.mob.skill.impl.malediction

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.CurseAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.factory.offensiveSpell
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.delayCostOf
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.manaCostOf
import kotlinmud.mob.skill.model.Cost
import kotlinmud.mob.skill.type.CostType
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Curse : SpellAction {
    override val affect = CurseAffect()
    override val type = SkillType.CURSE
    override val levelObtained = mapOf(
        mageAt(15)
    )
    override val difficulty = mapOf(
        easyForMage()
    )
    override val costs = listOf(
        delayCostOf(2),
        manaCostOf(50)
    )
    override val intent = Intent.OFFENSIVE
    override val syntax = offensiveSpell()

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        createAffect(AffectType.CURSE, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
