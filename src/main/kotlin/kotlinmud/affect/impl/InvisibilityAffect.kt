package kotlinmud.affect.impl

import kotlinmud.affect.type.Affect
import kotlinmud.affect.model.AffectInstance
import kotlinmud.affect.type.AffectType
import kotlinmud.helper.Noun
import kotlinmud.io.model.Message
import kotlinmud.io.model.MessageBuilder
import kotlinmud.mob.model.Mob

class InvisibilityAffect : Affect {
    override val type: AffectType = AffectType.INVISIBILITY

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("$target fades out of existence.")
            .toTarget("you fade out of existence")
            .toObservers("$target fades out of existence.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("you shimmer back into existence.")
            .toObservers("$target shimmers back into existence.")
            .build()
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout)
    }
}
