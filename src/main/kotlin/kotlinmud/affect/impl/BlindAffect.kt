package kotlinmud.affect.impl

import kotlinmud.Noun
import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.AttributesBuilder
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class BlindAffect : Affect {
    override val type: AffectType = AffectType.BLIND

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        return Message("You are blinded.", "$mob is blinded.")
    }

    override fun messageFromWearOff(target: Noun): Message {
        return Message("You can see again.", "$target can see again.")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout, AttributesBuilder()
            .dexterity(-1)
            .build())
    }
}
