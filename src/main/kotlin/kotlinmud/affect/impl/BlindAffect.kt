package kotlinmud.affect.impl

import kotlinmud.Noun
import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.io.Message
import kotlinmud.io.MessageBuilder
import kotlinmud.mob.Mob

class BlindAffect : Affect {
    override val type: AffectType = AffectType.BLIND

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return MessageBuilder()
            .toActionCreator("You are blinded.")
            .toObservers("$mob is blinded.")
            .build()
    }

    override fun messageFromWearOff(target: Noun): Message {
        return MessageBuilder()
            .toActionCreator("You can see again.")
            .toObservers("$target can see again.")
            .build()
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout, AttributesBuilder()
            .dexterity(-1)
            .build())
    }
}
