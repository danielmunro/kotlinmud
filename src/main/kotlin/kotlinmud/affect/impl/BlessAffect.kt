package kotlinmud.affect.impl

import kotlinmud.Noun
import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class BlessAffect : Affect {
    override val type: AffectType = AffectType.BLESS

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return Message(
            "You feel blessed.",
            "$mob is blessed.")
    }

    override fun messageFromWearOff(target: Noun): Message {
        return Message(
            "You no longer feel blessed.",
            "$target no longer is blessed.")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout)
    }
}
