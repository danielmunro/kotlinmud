package kotlinmud.mob.skill.impl.benediction

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.BlessAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class Bless : SpellAction {
    override val affect = BlessAffect()
    override val type = SkillType.BLESS
    override val levelObtained = mapOf(
        clericAt(5)
    )
    override val difficulty = mapOf(
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        createAffect(AffectType.BLESS, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
