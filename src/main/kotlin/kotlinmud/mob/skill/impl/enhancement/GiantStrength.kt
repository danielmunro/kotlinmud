package kotlinmud.mob.skill.impl.enhancement

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.impl.GiantStrengthAffect
import kotlinmud.affect.type.AffectType
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.dao.MobDAO
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.type.Intent
import org.jetbrains.exposed.sql.transactions.transaction

class GiantStrength : SpellAction {
    override val affect = GiantStrengthAffect()
    override val type = SkillType.GIANT_STRENGTH
    override val levelObtained = mapOf(
        mageAt(15),
        clericAt(25)
    )
    override val difficulty = mapOf(
        easyForMage(),
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<MobDAO>(Syntax.TARGET_MOB)
        createAffect(AffectType.GIANT_STRENGTH, actionContextService.getLevel()).also {
            transaction {
                it.mob = actionContextService.getMob()
            }
        }
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
