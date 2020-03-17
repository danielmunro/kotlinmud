package kotlinmud.affect.impl

import kotlinmud.Noun
import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class InvisibilityAffect : Affect {
    override val type: AffectType = AffectType.INVISIBLE

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return Message(
            "$target fades out of existence.",
            "you fade out of existence",
            "$target fades out of existence."
        )
    }

    override fun messageFromWearOff(target: Noun): Message {
        return Message(
            "you shimmer back into existence.",
            "you shimmer back into existence.",
            "$target shimmers back into existence."
        )
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout)
    }
}
