package kotlinmud.mob.skill.impl.illusion

import kotlinmud.action.service.ActionContextService
import kotlinmud.affect.impl.InvisibilityAffect
import kotlinmud.helper.Noun
import kotlinmud.io.model.Response
import kotlinmud.io.type.Syntax
import kotlinmud.item.model.Item
import kotlinmud.mob.model.Mob
import kotlinmud.mob.skill.factory.clericAt
import kotlinmud.mob.skill.factory.easyForMage
import kotlinmud.mob.skill.factory.mageAt
import kotlinmud.mob.skill.factory.normalForCleric
import kotlinmud.mob.skill.type.SkillType
import kotlinmud.mob.skill.type.SpellAction
import kotlinmud.mob.specialization.type.SpecializationType
import kotlinmud.mob.type.Intent

class Invisibility : SpellAction {
    override val type: SkillType = SkillType.INVISIBILITY
    override val levelObtained: Map<SpecializationType, Int> = mapOf(
        mageAt(5),
        clericAt(35)
    )
    override val difficulty = mapOf(
        easyForMage(),
        normalForCleric()
    )
    override val intent = Intent.PROTECTIVE
    override val affect = InvisibilityAffect()

    override fun invoke(actionContextService: ActionContextService): Response {
        val target = actionContextService.get<Any>(Syntax.OPTIONAL_TARGET)
        val instance = affect.createInstance(actionContextService.getLevel())
        if (target is Mob) {
            target.affects.add(instance)
        } else if (target is Item) {
            target.affects.add(instance)
        } else {
            throw Exception()
        }
        return actionContextService.createSpellInvokeResponse(target as Noun, affect)
    }
}
