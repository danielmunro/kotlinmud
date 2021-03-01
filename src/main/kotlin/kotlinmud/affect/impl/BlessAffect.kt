package kotlinmud.affect.impl

import kotlinmud.affect.factory.createAffect
import kotlinmud.affect.model.Affect
import kotlinmud.affect.type.AffectInterface
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class BlessAffect : AffectInterface {
    override val type: AffectType = AffectType.BLESS

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("You feel blessed.")
            .toObservers("$mob is blessed.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("You no longer feel blessed.")
            .toObservers("$target no longer is blessed.")
            .build()
    }

    override fun createInstance(timeout: Int): Affect {
        return createAffect(type, timeout)
    }
}
