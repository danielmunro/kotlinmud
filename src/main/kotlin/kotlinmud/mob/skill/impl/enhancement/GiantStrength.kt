package kotlinmud.mob.skill.impl.enhancement

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.impl.GiantStrengthAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.attributes.type.Attribute
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.mob.model.Mob
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
        val target = actionContextService.get<Mob>(Syntax.TARGET_MOB)
        target.affects.add(
            Affect(
                AffectType.GIANT_STRENGTH,
                actionContextService.getLevel(),
                mapOf(Pair(Attribute.STR, (actionContextService.getLevel() - 10) / 10))
            )
        )
        return actionContextService.createSpellInvokeResponse(target, affect)
    }
}
