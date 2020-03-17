package kotlinmud.affect.impl

import kotlinmud.Noun
import kotlinmud.affect.Affect
import kotlinmud.affect.AffectInstance
import kotlinmud.affect.AffectType
import kotlinmud.attributes.Attributes
import kotlinmud.io.Message
import kotlinmud.mob.Mob

class StunnedAffect : Affect {
    override val type: AffectType = AffectType.STUNNED

    override fun messageFromInstantiation(mob: Mob, target: Noun?): Message {
        TODO("Not yet implemented")
    }

    override fun messageFromWearOff(target: Noun): Message {
        TODO("Not yet implemented")
    }

    override fun createInstance(timeout: Int): AffectInstance {
        return AffectInstance(type, timeout, Attributes.Builder()
            .setInt(-1)
            .build())
    }
}
