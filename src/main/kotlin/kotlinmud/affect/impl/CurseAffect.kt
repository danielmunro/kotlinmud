package kotlinmud.affect.impl

import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.attributes.dao.AttributesDAO
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class CurseAffect : AffectInterface {
    override val type: AffectType = AffectType.CURSE

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("You are cursed.")
            .toObservers("$mob is cursed.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("You are no longer cursed.")
            .toObservers("$target is no longer cursed.")
            .build()
    }

    override fun createInstance(timeout: Int): Affect {
        return createAffect(
            type,
            timeout,
            AttributesDAO.new {
                strength = -1
                constitution = -1
            }
        )
    }
}
