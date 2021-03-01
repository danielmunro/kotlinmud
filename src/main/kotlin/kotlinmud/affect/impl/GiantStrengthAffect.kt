package kotlinmud.affect.impl

import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class GiantStrengthAffect : AffectInterface {
    override val type: AffectType = AffectType.GIANT_STRENGTH

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("$target's muscles surge with heightened powers.")
            .toTarget("your muscles surge with heightened powers.")
            .toObservers("$target's muscles surge with heightened powers.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("your strength returns to normal.")
            .toObservers("$target's strength returns to normal.")
            .build()
    }

    override fun createInstance(timeout: Int): Affect {
        return createAffect(type, timeout)
    }
}
